package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.Gmail.GEmail;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.thread.SimpleThread;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingController {

    @FXML //Panes for hver dag i kalenderoversigtet
    private Pane fredagPane, lørdagPane, mandagPane, onsdagPane,
            søndagPane, tirsdagPane, torsdagPane;

    @FXML
    private Label monthLabel, yearLabel, mandagDato, tirsdagDato,
            onsdagDato, torsdagDato, fredagDato, lørdagDato, søndagDato;

    @FXML //Ikon for notikfation rød/grå
    private Image greyNo, redNo;

    @FXML //Til notifikationikon
    private ImageView notits;

    private LocalDate shownDate, today, lde;

    private int listSize;

    //Images til ikoner for forløb
    InputStream robot = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/robot.png");
    Image robotImg = new Image(robot);
    InputStream idea = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/idea.png");
    Image ideaImg = new Image(idea);
    InputStream laser = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/laser.png");
    Image laserImg = new Image(laser);
    InputStream recycle = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/recycle.png");
    Image recycleImg = new Image(recycle);
    InputStream lifering = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/lifering.png");
    Image liferingImg = new Image(lifering);
    InputStream sea = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/sea.png");
    Image seaImg = new Image(sea);
    InputStream other = new FileInputStream("src/main/resources/com/example/bookingsystem/icon/andet.png");
    Image otherImg = new Image(other);

    //Koordinater og lister til rektangler der visualliserer bookings
    private double y_start,y_end;
    ArrayList<Rectangle> manRectangles = new ArrayList<>();
    ArrayList<Rectangle> tirsRectangles = new ArrayList<>();
    ArrayList<Rectangle> onsRectangles = new ArrayList<>();
    ArrayList<Rectangle> torsRectangles = new ArrayList<>();
    ArrayList<Rectangle> freRectangles = new ArrayList<>();
    ArrayList<Rectangle> lørRectangles = new ArrayList<>();
    ArrayList<Rectangle> sønRectangles = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    HashMap<Rectangle, Booking> rectangleBooking = new HashMap<>();

    //Lsitview til notifikationer
    ListView recent = new ListView<>();
    ListView upcoming = new ListView<>();

    private final SimpleThread simpleThread;

    GEmail ge = new GEmail();

    public BookingController() throws SQLException, IOException {
        simpleThread = new SimpleThread(this);
    }

    public void initialize(){
        shownDate = LocalDate.now();
        today = LocalDate.now();
        opsætDato();
        insertSystemBookings();
        simpleThread.start();
        // sendNotificationEmails();
    }

    public void passListsize(int size){
        listSize = size; //Overfører længde på notifikationsliste fra andre scener
    }

    @FXML
    void opretBookingKnap(ActionEvent event) throws IOException {
        //Åbner bookingformular med dagsdato og tid fra 7-12
        opretBooking(LocalDate.now(), Time.valueOf("07:00:00"), Time.valueOf("12:00:00"));
    }

    public void opretBooking(LocalDate d, Time st, Time et) throws IOException {
        //åben formular med felter til udfylde af bookinginfo
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("bookingFormular.fxml"));
        Scene oversigtScene = new Scene(fxmlLoader.load());
        OpretFormularController formController = fxmlLoader.getController();
        Stage oversigtStage = new Stage();
        oversigtStage.setScene(oversigtScene);
        formController.opsæt(d, st, et); //Fylder dato og tid ud, alt efter hvor der klikkes i kalenderoversigt

        // Hvis der klikkes udenfor vinduet, lukkes det
        oversigtStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                oversigtStage.hide();
                insertSystemBookings();
            }
        });

        oversigtStage.show();
    }

    @FXML
    void mailKnap(ActionEvent event) throws IOException {
        //åbner standart mailprogram på pc
        Desktop.getDesktop().mail();
    }

    public void updateNotifications(){
        recent.getItems().clear();
        List<Booking> notiBooking = bdi.recentlyCreated();
        for (Booking b : notiBooking)
        {
            recent.getItems().add(b); //indsætter nyoprettede bookings i listview
        }
        //Hvis længden på listen bliver større end tidligere længde, bliver notifikationsikonet rødt
        if(recent.getItems().size() > listSize){
            notits.setImage(redNo);
        }
        else{
            notits.setImage(greyNo); //Er listen ikke længere end listsize, bliver det gråt
        }
    }

    @FXML
    void notifikationKnap(ActionEvent event) {
        recent.setPrefHeight(200.0);
        recent.setStyle("-fx-font-family: monospace"); //Listview supporter ikke string.format uden monospace
        recent.setOnMouseClicked(mouseEvent ->{
            seKontaktInfo(recent); //Åbner kontaktinfo hvis der klikkes på en booking i listviewet
        });

        listSize = recent.getItems().size(); //Listsize bliver længden af recentlisten igen, så ikonet bliver gråt

        upcoming.getItems().clear();
        upcoming.setPrefHeight(200.0);
        upcoming.setStyle("-fx-font-family: monospace");
        upcoming.setOnMouseClicked(mouseEvent ->{
            seKontaktInfo(upcoming);
        });

        List<Booking> ucBooking = bdi.upcoming();
        for (Booking b : ucBooking)
        {
            upcoming.getItems().add(b); //indsætter kommende bookings i listview
        }

        Label l1 = new Label("Nyoprettede bookings");
        l1.setPadding(new Insets(4,0,0,4));
        Label l2 = new Label("Kommende bookings");
        l2.setPadding(new Insets(4,0,0,4));

        VBox vb = new VBox(l1, recent, l2, upcoming); //Indsætter overskifter og listviews i vbox
        vb.setSpacing(2);
        Scene notiScene = new Scene(vb); //Laver ny scene med vboxen
        Stage notiStage = new Stage();
        notiStage.setTitle("Notifikationer");
        notiStage.setScene(notiScene);

        // Set position of second window, related to primary window.
        notiStage.setX(950.0);

        notiStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                notiStage.hide(); // Hvis der klikkes udenfor vinduet, lukkes det
            }
        });

        notiStage.show();
    } //Viser seneste oprettet booking og kommende bookings

    public void seKontaktInfo(ListView l){
        ObservableList valgtBooking = l.getSelectionModel().getSelectedIndices();
        if (valgtBooking.isEmpty()){
            System.out.println("Vælg booking");
        }else
            for (Object indeks : valgtBooking){
                Booking b = (Booking) l.getItems().get((int) indeks);
                åbenKontaktInfo(b); //Åben scene med kontakt info for valgt booking
            }
    } // Får object fra notifikationer - åbner vindue

    public void åbenKontaktInfo(Booking b){
        //Labels
        Label n = new Label();
        n.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        n.setText("Navn: ");
        Label e = new Label();
        e.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        e.setText("e-mail: ");
        Label t = new Label();
        t.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        t.setText("tlf.: ");

        //Space + knap
        StackPane space = new StackPane();
        space.setPrefHeight(10);
        Button mail = new Button("Send mail");

        //opsætning i vbox
        VBox vb1 = new VBox(n, e, t, space, mail);
        vb1.setSpacing(4.0);
        vb1.setAlignment(Pos.CENTER_LEFT);
        vb1.setPadding(new Insets(0, 20, 0, 0));

        //Labels - henter info fra valgt booking
        Label navn = new Label();
        navn.setText(b.getFirstName() + " " + b.getLastName());
        Label email = new Label();
        email.setText(b.getEmail());
        Label tlf = new Label();
        tlf.setText(String.valueOf(b.getPhoneNumber()));

        //Space + knap
        StackPane space1 = new StackPane();
        space1.setPrefHeight(10);
        Button ændre = new Button("Ændre booking");

        //Opsætning i vbox
        VBox vb2 = new VBox(navn, email, tlf, space1, ændre);
        vb2.setSpacing(4.0);
        vb2.setAlignment(Pos.CENTER_LEFT);

        //opsætning i hbox
        HBox hb = new HBox(vb1, vb2);
        hb.setSpacing(4.0);
        hb.setAlignment(Pos.CENTER);
        hb.setPrefWidth(250);
        hb.setPrefHeight(150);

        //Opsæt stage + scene
        Scene contScene = new Scene(hb, 400, 200);
        //Henter styling fra stylesheet til at style knapper på samme måde som resten af programmet
        contScene.getStylesheets().add(String.valueOf(this.getClass().getResource("stylesheet.css")));
        Stage contStage = new Stage();
        contStage.setTitle("Kontaktinformation");
        contStage.setScene(contScene); //Contact information

        // Hvis der klikkes udenfor vinduet, lukkes det
        contStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                contStage.hide();
            }
        });
        contStage.show();

        //Åbner nyt vindue med mail
        mail.setOnAction(event -> {
            try {
                URI mailto = new URI("mailto:" + b.getEmail() + "?subject=Booking");
                Desktop.getDesktop().mail(mailto); //Åbner mailprogram med email og emne udfyldt
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Åbner nyt vindue med bookingsoversigt
        ændre.setOnAction(event -> {
            try {
                ændreBooking(b); //Åbner bookingforumlar med alt udfyldt
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    } //Åbner vindue med kontaktinfo fra object

    public void ændreBooking(Booking b) throws IOException {
        //åben formular med alt booking info, og knap der opdaterer
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("bookingsoversigt.fxml"));
        Scene oversigtScene = new Scene(fxmlLoader.load());
        FormularController formController = fxmlLoader.getController();
        Stage oversigtStage = new Stage();
        oversigtStage.setScene(oversigtScene);
        formController.passBooking(b); //Udfylder felter med info fra pågældende booking

        // Hvis der klikkes udenfor vinduet, lukkes det
        oversigtStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                oversigtStage.hide();
                insertSystemBookings(); //opdaterer kalenderoversigt
            }
        });
        oversigtStage.show();
    }

    @FXML
    void statestikKnap(ActionEvent event) throws IOException {
        //Åben ny scene med statestikker
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("statestik.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        StatestikController stController = fxmlLoader.getController();
        stController.passListsize(listSize); //Overfører listsize fra denne scene til den nye
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    public void dashboardKnap(ActionEvent event) throws IOException {
        //åbner scene med dashboard
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("dashboard.fxml"));
        Scene dashScene = new Scene(fxmlLoader.load());
        DashboardController dbController = fxmlLoader.getController();
        dbController.passListsize(listSize); //overfører listsize fra denne scene til den nye
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(dashScene);
    }

    @FXML
    void forrigeUgeKnap(ActionEvent event) {
        shownDate = shownDate.minusWeeks(1); //Sætter dato der bliver vist i øjeblikket, en uge tilbage
        opsætDato(); //Ændrer labels så de viser korrekt dato, en uge tilbage
        insertSystemBookings(); //Indsætter bookings passende til vist dato (en uge tilbage)
    } //Viser forrige uge i kalenderoversigt

    @FXML
    void todayKnap(ActionEvent event) {
        shownDate = today; //Sætter dato der bliver vist i øjeblikket, til dags dato
        opsætDato(); //Ændrer labels så de viser korrekt dato, for dags dato
        insertSystemBookings(); //Indsætter bookings passende til vist dato (dags dato)
    } //Viser denne uge i kalenderoversigt

    @FXML
    void næsteUgeKnap(ActionEvent event) {
        shownDate = shownDate.plusWeeks(1); //Sætter dato der bliver vist i øjeblikket, en uge frem
        opsætDato(); //Ændrer labels så de viser korrekt dato, en uge frem
        insertSystemBookings(); //Indsætter bookings passende til vist dato (en uge frem)
        System.out.println(shownDate);
    } //Viser næste uge i kalenderoversigt

    public void opsætDato(){
        oversætMdr();
        yearLabel.setText(String.valueOf(shownDate.getYear()));
        mandagDato.setText(String.valueOf(shownDate.with(DayOfWeek.MONDAY).getDayOfMonth()));
        tirsdagDato.setText(String.valueOf(shownDate.with(DayOfWeek.TUESDAY).getDayOfMonth()));
        onsdagDato.setText(String.valueOf(shownDate.with(DayOfWeek.WEDNESDAY).getDayOfMonth()));
        torsdagDato.setText(String.valueOf(shownDate.with(DayOfWeek.THURSDAY).getDayOfMonth()));
        fredagDato.setText(String.valueOf(shownDate.with(DayOfWeek.FRIDAY).getDayOfMonth()));
        lørdagDato.setText(String.valueOf(shownDate.with(DayOfWeek.SATURDAY).getDayOfMonth()));
        søndagDato.setText(String.valueOf(shownDate.with(DayOfWeek.SUNDAY).getDayOfMonth()));
    } //Sætter alle labels til at passe med datoer der vises i øjeblikket

    public void oversætMdr(){
        if (shownDate.getMonth() == Month.JANUARY){
            monthLabel.setText("Jan");
        } else if (shownDate.getMonth() == Month.FEBRUARY) {
            monthLabel.setText("Feb");
        } else if (shownDate.getMonth() == Month.MARCH) {
            monthLabel.setText("Mar");
        } else if (shownDate.getMonth() == Month.APRIL) {
            monthLabel.setText("Apr");
        } else if (shownDate.getMonth() == Month.MAY) {
            monthLabel.setText("Maj");
        } else if (shownDate.getMonth() == Month.JUNE) {
            monthLabel.setText("Jun");
        } else if (shownDate.getMonth() == Month.JULY) {
            monthLabel.setText("Jul");
        } else if (shownDate.getMonth() == Month.AUGUST) {
            monthLabel.setText("Aug");
        } else if (shownDate.getMonth() == Month.SEPTEMBER) {
            monthLabel.setText("Sep");
        } else if (shownDate.getMonth() == Month.OCTOBER) {
            monthLabel.setText("Oct");
        } else if (shownDate.getMonth() == Month.NOVEMBER) {
            monthLabel.setText("Nov");
        } else if (shownDate.getMonth() == Month.DECEMBER) {
            monthLabel.setText("Dec");
        }
    } //Sætter måned-label til at være dansk

    @FXML
    void mondayPress(MouseEvent event) { // Denne metode er præcis den samme for alle de andre ugedagPress events
        event.setDragDetect(true);
        y_start = event.getY();
    }// Sætter drag til true og får en y_værdi ud fra musens lokation.

    @FXML
    void mondayRelease(MouseEvent event) { // Denne metode er præcis den samme for alle de andre ugedagRelease events.
        try { // Får endnu en y_værdi ud fra hvor musen stopper med at være holdt nede.
            y_end = event.getY();
            addStack(mandagPane, manRectangles); //Tilføjer et rektangel på dagens pane
        }catch (IndexOutOfBoundsException e){ //Giver man slip udenfor oversigtet, catcher vi en exception
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void tuesdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void tuesdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(tirsdagPane, tirsRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void wednesdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void wednesdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(onsdagPane, onsRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void thursdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void thursdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(torsdagPane, torsRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void fridayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void fridayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(fredagPane, freRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void saturdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void saturdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(lørdagPane, lørRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void sundayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void sundayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(søndagPane, sønRectangles);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    public void addStack(Pane p, ArrayList<Rectangle> rect){

        HashMap<Double, Time> locationMap = new HashMap<>();

        // Tjekker hvilken pane tingene foregår i. Bliver brugt til at indsætte dato til vores opretBooking metode
        if(p == mandagPane) {
            lde = shownDate.with(DayOfWeek.MONDAY);
        } else if(p == tirsdagPane){
            lde = shownDate.with(DayOfWeek.TUESDAY);
        } else if(p == onsdagPane){
            lde = shownDate.with(DayOfWeek.WEDNESDAY);
        } else if(p == torsdagPane){
            lde = shownDate.with(DayOfWeek.THURSDAY);
        } else if(p == fredagPane){
            lde = shownDate.with(DayOfWeek.FRIDAY);
        } else if(p == lørdagPane){
            lde = shownDate.with(DayOfWeek.SATURDAY);
        } else if(p == søndagPane){
            lde = shownDate.with(DayOfWeek.SUNDAY);
        }

        // Et HashMap med tidsværdier og deres korresponderende y-aksis værdier. Bruges til at indsætte bookings på de rigtige pladser
        locationMap.put(0.0,Time.valueOf("07:00:00")); locationMap.put(44.0,Time.valueOf("08:00:00")); locationMap.put(89.0,Time.valueOf("09:00:00"));
        locationMap.put(134.0,Time.valueOf("10:00:00")); locationMap.put(179.0,Time.valueOf("11:00:00")); locationMap.put(224.0,Time.valueOf("12:00:00"));
        locationMap.put(269.0,Time.valueOf("13:00:00")); locationMap.put(314.0,Time.valueOf("14:00:00")); locationMap.put(359.0,Time.valueOf("15:00:00"));
        locationMap.put(404.0,Time.valueOf("16:00:00")); locationMap.put(449.0,Time.valueOf("17:00:00")); locationMap.put(494.0,Time.valueOf("18:00:00"));
        locationMap.put(539.0,Time.valueOf("19:00:00")); locationMap.put(584.0,Time.valueOf("20:00:00")); locationMap.put(629.0,Time.valueOf("21:00:00"));
        locationMap.put(674.0,Time.valueOf("22:00:00")); locationMap.put(719.0,Time.valueOf("23:00:00")); locationMap.put(764.0,Time.valueOf("24:00:00"));

        // Array med de værdier som vi skal bruge mht. at indsætte rektangel på det korrekte sted
        double[] yValues = {0, 44, 89, 134, 179, 224, 269, 314, 359, 404, 449, 494, 539, 584, 629, 674, 719, 764};

        // Find start og slut indekset for den nye rektangel
        int startIndex = -1;
        int endIndex = -1;

        // For-loop der finder start og slut index.
        for (int i = 0; i < yValues.length; i++) {
            if (y_start >= yValues[i] && y_start < yValues[i + 1]) {
                startIndex = i;
            }
            if (y_end >= yValues[i] && y_end < yValues[i + 1]) {
                endIndex = i + 1;
                break;
            }
        }

        // Hvis startIndexet ikke er -1 og endIndex ikke er -1, så laver vi en ny rektangel
        if (startIndex != -1 && endIndex != -1) {
            Rectangle r = new Rectangle();
            Label l = new Label();

            r.setY(yValues[startIndex] + 1);
            r.setX(0);
            r.setWidth(133);
            r.setHeight(yValues[endIndex] - yValues[startIndex] - 1);
            r.setOpacity(0.3);

            r.layoutYProperty().addListener((obs,oldVal,newVal) -> {
                l.setLayoutY(newVal.doubleValue() / 2);
            });

            boolean intersects = false;

            // Tjekker om rektangelen overlapper allerede eksisterende renktangler.
            for (Rectangle rec : rect) {
                if (rec.getY() + rec.getHeight() >= r.getY() && rec.getY() <= r.getY() + r.getHeight()) {
                    intersects = true;
                    break;
                }
            }
            // Hvis den ikke overlapper allerede eksisterende rektangler, så bliver den tilføjet
            // til kalenderen
            if (!intersects) {
                try {
                    //Prøver at oprette en booking, med tidspunkt for hvor man har klikket
                    //via musens y værdi
                    opretBooking(lde,locationMap.get(yValues[startIndex]),locationMap.get(yValues[endIndex]));
                } catch (IOException e){
                    System.err.println("Noget gik galt " + e.getMessage());
                }
            }
        }
    } // Tilføjer en rektangel til der hvor brugeren har klikket vha. mouse drag events.

    public void insertSystemBookings(){
        removeVisuals(); //Fjerner bookings før vi sætter ind på ny
        //Laver liste af bookings for pågældende uge - (mandag og 7 dage frem)
        List<Booking> bookings = bdi.showBooking(shownDate.with(DayOfWeek.MONDAY));

        HashMap<Time, Double> locationMap = new HashMap<>();

        // Et HashMap med tidsværdier og deres korresponderende y-aksis værdier. Bruges til at indsætte bookings på de rigtige placeringer
        locationMap.put(Time.valueOf("07:00:00"),0.0); locationMap.put(Time.valueOf("08:00:00"),44.0); locationMap.put(Time.valueOf("09:00:00"),89.0);
        locationMap.put(Time.valueOf("10:00:00"),134.0); locationMap.put(Time.valueOf("11:00:00"),179.0); locationMap.put(Time.valueOf("12:00:00"),224.0);
        locationMap.put(Time.valueOf("13:00:00"),269.0); locationMap.put(Time.valueOf("14:00:00"),314.0); locationMap.put(Time.valueOf("15:00:00"),359.0);
        locationMap.put(Time.valueOf("16:00:00"),404.0); locationMap.put(Time.valueOf("17:00:00"),449.0); locationMap.put(Time.valueOf("18:00:00"),494.0);
        locationMap.put(Time.valueOf("19:00:00"),539.0); locationMap.put(Time.valueOf("20:00:00"),584.0); locationMap.put(Time.valueOf("21:00:00"),629.0);
        locationMap.put(Time.valueOf("22:00:00"),674.0); locationMap.put(Time.valueOf("23:00:00"),719.0); locationMap.put(Time.valueOf("24:00:00"),764.0);

        // Indsætter rektangler
        for(Booking book : bookings){

            Rectangle r = new Rectangle();
            Label l = new Label();
            ImageView iv = new ImageView();

            double yStart = locationMap.get(book.getStartTid()); //Sætter værdier passende med hashmap og bookingstid
            double yEnd = locationMap.get(book.getSlutTid());

            // Opsætter rektanglen for hvor det skal være i det pågældende pane
            r.setY(yStart + 1);
            r.setHeight(yEnd - yStart - 1);
            r.setX(0);
            r.setWidth(133);
            r.setOpacity(0.3);

            // Sætter farven alt efter booking type (temporary eller permanent)
            if (book.getBookingType() == 't'){ r.setFill(Color.RED);}
            else {r.setFill(Color.DODGERBLUE);}

            l.setText(book.toString()); //Sætter navn og tidspunkt for booking
            l.setLayoutY(r.getY() + 5); //Placerer label øverst til venstre på rektanget

            //Opsætter imageview der skal vise ikon for forløb
            iv.setLayoutY(r.getY() + 5); //I højre hjørne ud for navn + tidspunkt
            iv.setLayoutX(r.getWidth() - 25);
            iv.setFitHeight(20);
            iv.setFitWidth(20);

            Image img; //Sætter image til at være ikon tilsvarende forløb
            if (book.getForløb().getId() == 1){
                img = ideaImg; //Er forløb id'et 1, er forløbet "Idefabrikken"
            } else if (book.getForløb().getId() == 2) {
                img = laserImg;
            } else if (book.getForløb().getId() == 3) {
                img = robotImg;
            } else if (book.getForløb().getId() == 4) {
                img = recycleImg;
            } else if (book.getForløb().getId() == 5) {
                img = seaImg;
            } else if (book.getForløb().getId() == 6) {
                img = liferingImg;
            } else {
                img = otherImg;
            }
            iv.setImage(img); //Sætter det tilsvarende ikon i imageviewet

            labels.add(l);
            imageViews.add(iv);

            // Tilføjer rektangel og book til HashMap der bruges til at tjekke at der ikke er overlap
            rectangleBooking.put(r,book);

            r.onMouseClickedProperty().set(mouseEvent -> {
                //Booking bk = bookings.get(book); // Får information om lige præcis den Booking der hører til objektet
                System.out.println("Clicked on: " + book.getFirstName());
                åbenKontaktInfo(book);
            });
            //Sætter rektanglet m. label + imageview ind på det pane som passer til datoen der vises i kalenderoversigt
            if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.MONDAY))){
                manRectangles.add(r);
                mandagPane.getChildren().add(r);
                mandagPane.getChildren().add(l);
                mandagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.TUESDAY))){
                tirsRectangles.add(r);
                tirsdagPane.getChildren().add(r);
                tirsdagPane.getChildren().add(l);
                tirsdagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.WEDNESDAY))){
                onsRectangles.add(r);
                onsdagPane.getChildren().add(r);
                onsdagPane.getChildren().add(l);
                onsdagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.THURSDAY))){
                torsRectangles.add(r);
                torsdagPane.getChildren().add(r);
                torsdagPane.getChildren().add(l);
                torsdagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.FRIDAY))){
                freRectangles.add(r);
                fredagPane.getChildren().add(r);
                fredagPane.getChildren().add(l);
                fredagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.SATURDAY))){
                lørRectangles.add(r);
                lørdagPane.getChildren().add(r);
                lørdagPane.getChildren().add(l);
                lørdagPane.getChildren().add(iv);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.SUNDAY))){
                sønRectangles.add(r);
                søndagPane.getChildren().add(r);
                søndagPane.getChildren().add(l);
                søndagPane.getChildren().add(iv);
            }
        }
    } //Indsætter bookings fra database i kalenderoversigt

    public void removeVisuals(){ //Fjerner alt visuelt der er indsat i kalenderoversigtet
        mandagPane.getChildren().removeAll(manRectangles);
        manRectangles.clear();
        tirsdagPane.getChildren().removeAll(tirsRectangles);
        tirsRectangles.clear();
        onsdagPane.getChildren().removeAll(onsRectangles);
        onsRectangles.clear();
        torsdagPane.getChildren().removeAll(torsRectangles);
        torsRectangles.clear();
        fredagPane.getChildren().removeAll(freRectangles);
        freRectangles.clear();
        lørdagPane.getChildren().removeAll(lørRectangles);
        lørRectangles.clear();
        søndagPane.getChildren().removeAll(sønRectangles);
        sønRectangles.clear();
        mandagPane.getChildren().removeAll(labels);
        tirsdagPane.getChildren().removeAll(labels);
        onsdagPane.getChildren().removeAll(labels);
        torsdagPane.getChildren().removeAll(labels);
        fredagPane.getChildren().removeAll(labels);
        lørdagPane.getChildren().removeAll(labels);
        søndagPane.getChildren().removeAll(labels);
        labels.clear();

        mandagPane.getChildren().removeAll(imageViews);
        tirsdagPane.getChildren().removeAll(imageViews);
        onsdagPane.getChildren().removeAll(imageViews);
        torsdagPane.getChildren().removeAll(imageViews);
        fredagPane.getChildren().removeAll(imageViews);
        lørdagPane.getChildren().removeAll(imageViews);
        søndagPane.getChildren().removeAll(imageViews);
        imageViews.clear();
    } // Fjerner alt det visuelle. Dvs. rektangler og labels.

    public void sendNotificationEmails(){

        // Laver en liste med de Bookings der passer til det vi efterlyser
        List<Booking> sendEmails = bdi.sendEmailNotification();

        // Laver en ny GEmail
        GEmail gmailSender = new GEmail();

        // For hver booking der opfylder vores betingelser, sender vi en mail til den person
        for(Booking book : sendEmails){
            String start = String.valueOf(book.getStartTid()).substring(0,2);
            String slut = String.valueOf(book.getSlutTid()).substring(0,2);

            gmailSender.sendNotification(book.getEmail(),book.getFirstName(),String.valueOf(book.getBookingDate()),start,slut);
        }
    } // Sender mails med påmindelse om at de har en booking 1 uge inden.
    BookingDAO bdi = new BookingDAOImpl();


}