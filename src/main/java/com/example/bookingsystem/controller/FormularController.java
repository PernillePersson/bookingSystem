package com.example.bookingsystem.controller;

import com.example.bookingsystem.Gmail.GEmail;
import com.example.bookingsystem.model.DAO.OrganisationDAO;
import com.example.bookingsystem.model.DAO.OrganisationDAOImpl;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.objects.Note;
import com.example.bookingsystem.model.objects.Organisation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class FormularController {

    @FXML
    private VBox changeBox;

    private TextArea noteText = new TextArea();

    @FXML
    private DatePicker bookingDato;

    @FXML
    private ToggleGroup forplejning;

    @FXML
    private RadioButton yesToggle, noToggle;

    @FXML
    private Label mailLabel, navnLabel, organisationLabel, tlfLabel, godkendLabel;

    @FXML
    private ComboBox slutTid, startTid;

    @FXML
    private Button godkendKnap, opdaterBookingKnap, noteKnap;

    private Booking booking;

    GEmail gmailSender = new GEmail();

    public FormularController() throws SQLException {
    }


    public void passBooking(Booking b){ //Henter valgt bookingobjekt fra forrige scene
        booking = b;
        setDetails(booking);
    }

    public void setDetails(Booking b){
        //Indsætter detaljer fra valgt booking i alle felter
        startTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        slutTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");

        navnLabel.setText(b.getFirstName() + " " + b.getLastName());
        mailLabel.setText(b.getEmail());

        try {
            Organisation o = odi.getOrg(b.getId());
            if (o.getId() == 6){
                organisationLabel.setText(odi.getCompany(b.getId()).getCompany());
            }else{
                organisationLabel.setText(o.getOrganisation());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        tlfLabel.setText(String.valueOf(b.getPhoneNumber()));

        if (b.getCatering() == 'y'){
            forplejning.selectToggle(yesToggle);
        } else {
            forplejning.selectToggle(noToggle);
        }

        bookingDato.setValue(b.getBookingDate());
        startTid.setValue(String.valueOf(b.getStartTid()).substring(0,5));
        slutTid.setValue(String.valueOf(b.getSlutTid()).substring(0,5));

        if (b.getBookingType() == 'p'){
            godkendLabel.setVisible(false);
            godkendKnap.setVisible(false);
        }

        try {
            Note n = bdi.getNote(b.getId());
            if (n != null){ //Hvis der er en tilhørende note, skal den sættes ind istedet for "tilføj note"-knappen
                noteText.setPrefWidth(159);
                noteText.setPrefHeight(60);
                changeBox.getChildren().add(noteText);
                changeBox.getChildren().remove(noteKnap);
                noteText.setText(n.getNote());
            }
        }catch (NullPointerException e){
            System.out.println("Denne booking har ingen note");
        }

    }
    @FXML
    void forplejningToggle(ActionEvent event) { //Sætter forplejning for booking til y eller n (yes eller no)
        if (forplejning.getSelectedToggle() == yesToggle){
            booking.setCatering('y');
        } else if (forplejning.getSelectedToggle() == noToggle) {
            booking.setCatering('n');
        }
    }

    @FXML
    void opdaterStartTid(MouseEvent event) { //Opdaterer starttid for booking
        startTid.setOnAction((e) -> {
            if (startTid.getSelectionModel().getSelectedIndex() >= slutTid.getSelectionModel().getSelectedIndex()){
                startTid.setValue(startTid.getItems().get(slutTid.getSelectionModel().getSelectedIndex() -1));
            }
            booking.setStartTid(Time.valueOf(startTid.getValue() + ":00"));
        });
    }

    @FXML
    void opdaterSlutTid(MouseEvent event) { //Opdaterer sluttid for booking
        slutTid.setOnAction((e) -> {
            if (slutTid.getSelectionModel().getSelectedIndex() <= startTid.getSelectionModel().getSelectedIndex()){
                slutTid.setValue(slutTid.getItems().get(startTid.getSelectionModel().getSelectedIndex() +1));
            }
            booking.setSlutTid(Time.valueOf(slutTid.getValue() + ":00"));
        });
    }

    @FXML
    void ændreDato(ActionEvent event) { //Opdaterer dato for booking
        booking.setBookingDate(bookingDato.getValue());
    }

    @FXML
    void godkendBooking(ActionEvent event) { //hvis booking er temporary, bliver den permanent
        booking.setBookingType('p');
    }

    @FXML
    void tilføjNote(ActionEvent event) { //Tilføjer tekstfelt til note, og fjerne "tilføj note"-knappen
        noteText.setPrefWidth(159);
        noteText.setPrefHeight(60);
        changeBox.getChildren().add(noteText);
        changeBox.getChildren().remove(noteKnap);

    }

    @FXML
    void opdaterBooking(ActionEvent event) throws SQLException {

        List<Booking> allBookings = bdi.getAllBooking();
        boolean overlaps = false;

        for(Booking b : allBookings){
            //Tjekker om der allerede er en booking på valgt date + tidpunkt
            String value = String.valueOf(b.getStartTid());
            String strt = value.substring(0,2);
            int start = Integer.valueOf(strt);

            String value2 = String.valueOf(b.getSlutTid());
            String slt = value2.substring(0,2);
            int slut = Integer.valueOf(slt);

            String value3 = String.valueOf(startTid.getValue());
            String comboStart = value3.substring(0,2);
            int comboStrt = Integer.valueOf(comboStart);

            String value4 = String.valueOf(slutTid.getValue());
            String comboSlut = value4.substring(0,2);
            int comboSlt = Integer.valueOf(comboSlut);

            if(bookingDato.getValue().equals(b.getBookingDate()) & b.getId() != b.getId() && comboSlt >= start && comboStrt <= slut){
                //Overlapper hvis en booking i databasen har same dato og tidpunkt som den nye booking
                //Tjekker ikke på det id som er ens med den booking vi opdaterer
                overlaps = true;
                break;
            }
        }

        if(!overlaps) { //Hvis ny booking ikke overlapper en allerede eksisterende booking opdaterer vi den nye

            bdi.updateBooking(booking.getId(), booking.getBookingType(), booking.getCatering(),
                    booking.getBookingDate(), booking.getStartTid(), booking.getSlutTid());

            if (!noteText.getText().isEmpty()){ //Hvis note-tekstfeltet ikke er tomt, opretter vi en note til booking
                bdi.addNote(booking, noteText.getText());
            }

            //gmailSender.ændringsMail(booking.getEmail());

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close(); //lukker formularen efter opdatering
        }
    }

    @FXML
    void sletBooking(ActionEvent event) throws SQLException {
        Dialog<ButtonType> dialog = new Dialog(); //Åbner dialog for man kan nå at fortryde

        dialog.setTitle("Slet booking");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label infoLabel = new Label("Er du sikker på, at denne booking skal slettes?");
        dialog.getDialogPane().setContent(infoLabel);

        Optional<ButtonType> knap = dialog.showAndWait();

        if (knap.get() == ButtonType.OK)
            try {
                //gmailSender.aflystMail(booking.getEmail());
                bdi.cancelBooking(booking);

            } catch (Exception e) {
                System.err.println("Noget gik galt");
                System.err.println(e.getMessage());
            }
    } //Sletter booking

    BookingDAO bdi = new BookingDAOImpl();
    OrganisationDAO odi = new OrganisationDAOImpl();
}