package com.example.bookingsystem.controller;

import com.example.bookingsystem.Gmail.GEmail;
import com.example.bookingsystem.model.*;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.DAO.OrganisationDAO;
import com.example.bookingsystem.model.DAO.OrganisationDAOImpl;
import com.example.bookingsystem.model.objects.Forløb;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.objects.Organisation;
import javafx.event.ActionEvent;
import java.awt.Desktop;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.TRANSPARENT;

public class OpretFormularController {

    @FXML
    private TextField eNavn,  email, fNavn, org, tlf;

    @FXML
    private DatePicker bookingDato;

    @FXML
    private ToggleGroup bookingType, forplejning;

    @FXML
    private Hyperlink forplejningLink;

    @FXML
    private RadioButton midlType, permType, noToggle, yesToggle;

    @FXML
    private Button opretBookingKnap;

    @FXML
    private ComboBox orgBox, formål, forløb, slutTid, startTid;

    @FXML
    private Text bemærkning, optagetTekst;

    @FXML
    private Spinner antalDeltagere;

    private char type;
    private char forp;

    private String bKode;

    private Boolean midlertidig;

    private Booking b;
    private Forløb f1;
    private Organisation o1;

    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    GEmail gmailSender = new GEmail();


    public OpretFormularController() throws SQLException {

    }

    public void opsæt(LocalDate d, Time st, Time et) { //Opsætter felter med dato og tid udfyldt
        startTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        slutTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        startTid.setValue(String.valueOf(st).substring(0, 5));
        slutTid.setValue(String.valueOf(et).substring(0, 5));

        formål.getItems().addAll("Lokaleleje", "Åbent skoleforløb", "Andet");
        formål.setValue("Lokaleleje");

        List<Organisation> org = odi.getAllOrg();
        for (Organisation o : org){
            orgBox.getItems().add(o);
        }

        List<Forløb> forl = bdi.getAllForløb();
        for (Forløb f : forl){
            forløb.getItems().add(f);
        }
        f1 = (Forløb) forløb.getItems().get(6);//Det index hvor forløb er "ingen"
        forplejningLink.setVisible(false);
        bookingDato.setValue(d);
        forp = 'n';
        type = 'p';
    }

    public void tjekCharacter(){ //Tjekker om der er blevet indsat de korrekte værdier i felterne
        eNavn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("a-zæøå A-ZÆØÅ -*")) {
                eNavn.setText(newValue.replaceAll("[^a-zæøå A-ZÆØÅ]", ""));
            }
        }); //Indsættes der andet end bagstaver her, vil de blive erstattet med ingenting

        fNavn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("a-zæøå A-ZÆØÅ*")) {
                fNavn.setText(newValue.replaceAll("[^a-zæøå A-ZÆØÅ]", ""));
            }
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("a-zæøå A-ZÆØÅ \\d @ - .*")) {
                email.setText(newValue.replaceAll("[^a-zæøå A-ZÆØÅ \\d . @ -]", ""));
            }
        });

        org.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("a-zæøå A-ZÆØÅ -*")) {
                org.setText(newValue.replaceAll("[^a-zæøå A-ZÆØÅ -]", ""));
            }
        });

        tlf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tlf.setText(newValue.replaceAll("[^\\d]", ""));
            } //Indsættes der andet en tal, erstattes det med ingenting
            if (tlf.getLength() > 8) { //Hvis længden på nr overskrider 8, erstatters det med ingenting
                String MAX = tlf.getText().substring(0,8);
                tlf.setText(MAX);
            }
        });
    }

    public void initialize() {
        tjekCharacter();
    }
        
    @FXML
    void forplejningToggle(ActionEvent event) {
        if (forplejning.getSelectedToggle() == yesToggle){
            forplejningLink.setVisible(true);
            forp = 'y';
        } else if (forplejning.getSelectedToggle() == noToggle) {
            forplejningLink.setVisible(false);
            forp = 'n';
        }
    } //Sætter forplejning til at være y eller n (yes eller no), ud fra radiobuttons

    @FXML
    void orgValgt(ActionEvent event) {
        if (orgBox.getSelectionModel().getSelectedIndex() == 5){
            org.setVisible(true); //Er "andet" valgt, tilføjes et tekstfelt hvor man kan indtaste virksomhednavn
        } else {
            org.setVisible(false);
        }
        o1 = (Organisation) orgBox.getValue();
    } //Sætter organisation for booking

    @FXML
    void formålValgt(ActionEvent event) {
        f1 = (Forløb) forløb.getItems().get(6); //Det index hvor forløb er "ingen"
        if (formål.getSelectionModel().getSelectedIndex() == 1){
            forløb.setVisible(true); //Hvis formålet er åbent skoleforløb, skal forløbne vises
        } else {
            forløb.setVisible(false);
            f1 = (Forløb) forløb.getItems().get(6); //Det index hvor forløb er "ingen"
        }
    } //Vælger formål med booking

    @FXML
    void forløbValgt(ActionEvent event){
        f1 = (Forløb) forløb.getValue();
    } //Er et skoleforløb valgt, indsætter vi det ved bookingen

    @FXML
    void hentForplejning(ActionEvent event) {
        try {
            File pdf = new File("src/main/resources/com/example/bookingsystem/forplejning.pdf");
            Desktop.getDesktop().open(pdf);
        } catch (Exception e){
            System.out.println("Kunne ikke hente pdf" + e.getMessage());
        }
    } //Åbner forplejningsformular med standart pdf program på pc

    @FXML
    void tjekDato(ActionEvent event) {
        if (slutTid.getSelectionModel().getSelectedIndex() >= 11 ||
                bookingDato.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                bookingDato.getValue().getDayOfWeek() == DayOfWeek.SUNDAY){
            //Ligger booking efter kl 18, eller i weekenden skal man anmode om booking, og typen sættes til temporary
            opretBookingKnap.setText("Anmod om booking");
            bemærkning.setVisible(true);
            type = 't';
            midlertidig = true;

        }else { //Hvis booking er inden for normal åbningtid, kan den frit oprettes og bliver permanent
            opretBookingKnap.setText("Opret booking");
            bemærkning.setVisible(false);
            type = 'p';
            midlertidig = false;
        }
    }

    @FXML
    void opdaterSlutTid(ActionEvent event) {
        if (slutTid.getSelectionModel().getSelectedIndex() <= startTid.getSelectionModel().getSelectedIndex()){
            slutTid.setValue(slutTid.getItems().get(startTid.getSelectionModel().getSelectedIndex() +1));
        } //Hvis sluttid bliver sat lavere end start tid, ændres den til en time frem fra starttid

        if (slutTid.getSelectionModel().getSelectedIndex() >= 11 ||
                bookingDato.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                bookingDato.getValue().getDayOfWeek() == DayOfWeek.SUNDAY){
            opretBookingKnap.setText("Anmod om booking");
            bemærkning.setVisible(true);
            type = 't';
            midlertidig = true;
        }else {
            opretBookingKnap.setText("Opret booking");
            bemærkning.setVisible(false);
            type = 'p';
            midlertidig = false;
        }
    }

    @FXML
    void opdaterStartTid(ActionEvent event) {
        if (startTid.getSelectionModel().getSelectedIndex() >= slutTid.getSelectionModel().getSelectedIndex()){
            startTid.setValue(startTid.getItems().get(slutTid.getSelectionModel().getSelectedIndex() -1));
        } //Hvis starttid sættes til efter sluttid, ændres startid til en time før sluttid
    }

    @FXML
    void typeToggle(ActionEvent event) {
        if (bookingType.getSelectedToggle() == midlType){
            type = 't'; //temporary
        } else if (bookingType.getSelectedToggle() == permType) {
            if (!midlertidig){
                type = 'p'; //Permanent
            }
        }
    } //Radiobuttons vælger om man ønsker midlertidg eller permanent booking

    @FXML
    void opretBooking(ActionEvent event) throws InterruptedException, URISyntaxException {
        //Er felter ikke udfyldt korrekt, sættes der en rød border på, og man får ikke lov at oprette
        if (fNavn.getLength() == 0) {
            fNavn.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else if (eNavn.getLength() == 0) {
            eNavn.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else if (tlf.getLength() != 8) {
            tlf.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else if (email.getLength() == 0) {
            email.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else if (antalDeltagere.getValue().equals(0)) {
            antalDeltagere.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else if (formål.getValue().equals("Åbent skoleforløb") && forløb.getValue().equals("Intet")) {
            forløb.setBorder(new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, null, null)));

        } else { //Er felter udfyldt korrekt opretter vi booking med detajler fra udfyldte felter
            int nr = Integer.parseInt(tlf.getText());
            bKode = BookingCode.generateBookingCode();
            String organisation = org.getText();
            if (org.getText().isEmpty()) {
                organisation = "Ingen";
            }
            List<Booking> allBookings = bdi.getAllBooking();
            boolean overlaps = false;

            for (Booking b : allBookings) {
                //Tjekker om der allerede er en booking på valgt date + tidpunkt
                String value = String.valueOf(b.getStartTid());
                String strt = value.substring(0, 2);
                int start = Integer.valueOf(strt);

                String value2 = String.valueOf(b.getSlutTid());
                String slt = value2.substring(0, 2);
                int slut = Integer.valueOf(slt);

                String value3 = String.valueOf(startTid.getValue());
                String comboStart = value3.substring(0, 2);
                int comboStrt = Integer.valueOf(comboStart);

                String value4 = String.valueOf(slutTid.getValue());
                String comboSlut = value4.substring(0, 2);
                int comboSlt = Integer.valueOf(comboSlut);

                if (bookingDato.getValue().equals(b.getBookingDate()) && comboSlt >= start && comboStrt <= slut) {
                    //Overlapper hvis en booking i databasen har same dato og tidpunkt som den nye booking
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) { //Hvis ny booking ikke overlapper en allerede eksisterende booking opretter vi den nye

                bdi.addBooking(fNavn.getText(), eNavn.getText(), email.getText(), nr,
                        type, forp, bookingDato.getValue(), bKode, Time.valueOf(startTid.getValue() + ":00"),
                        Time.valueOf(slutTid.getValue() + ":00"), (Integer) antalDeltagere.getValue());

                bdi.addForløb(bKode, f1.getId());
                odi.addOrg(bKode, o1.getId());

                if (o1.getId() == 6) { //Hvis organisation ikke er valgt, opretter vi virksomhed
                    odi.addCompany(bKode, org.getText());
                }

                Dialog<ButtonType> dialog = new Dialog(); //Åbner vindue med bookingkode for ny booking

                dialog.setTitle("Din bookingkode");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

                Label l1 = new Label("Din bookingkode:");
                Label kodeLabel = new Label(bKode);
                kodeLabel.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                Label infoLabel = new Label("Gem denne kode til senere brug");

                //GEmail gmailSender = new GEmail();
                //gmailSender.sendBookingCode(email.getText(),fNavn.getText(),bKode);

                VBox vb = new VBox(l1, kodeLabel, infoLabel);
                vb.setSpacing(10);
                vb.setPadding(new Insets(10, 30, 10, 30));

                dialog.getDialogPane().setContent(vb);

                Optional<ButtonType> knap = dialog.showAndWait();

                if (knap.get() == ButtonType.OK)
                    try {
                        content.putString(bKode);
                        clipboard.setContent(content); //Kopirerer bookingkode til udklipsholder
                    } catch (Exception e) {
                    }
            } else { //Hvis booking overlapper en eksisterende, vises label der foreslår at vælge anden dato
                optagetTekst.setVisible(true);
            }
        }
    }

    @FXML
    void anullerBooking(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    } //Lukker formularen ned uden at oprette booking

    BookingDAO bdi = new BookingDAOImpl();
    OrganisationDAO odi = new OrganisationDAOImpl();

}
