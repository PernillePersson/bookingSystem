package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.Gmail.GEmail;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.SimpleThread;
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
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingController {

    @FXML
    private Pane fredagPane, lørdagPane, mandagPane, onsdagPane,
            søndagPane, tirsdagPane, torsdagPane;

    @FXML
    private Label monthLabel, yearLabel, mandagDato, tirsdagDato,
            onsdagDato, torsdagDato, fredagDato, lørdagDato, søndagDato;

    @FXML
    private Image greyNo, redNo;

    @FXML
    private ImageView notits;

    private LocalDate shownDate, today, lde;

    private int listSize;

    private double y_start,y_end;
    ArrayList<Rectangle> manRectangles = new ArrayList<>();
    ArrayList<Rectangle> tirsRectangles = new ArrayList<>();
    ArrayList<Rectangle> onsRectangles = new ArrayList<>();
    ArrayList<Rectangle> torsRectangles = new ArrayList<>();
    ArrayList<Rectangle> freRectangles = new ArrayList<>();
    ArrayList<Rectangle> lørRectangles = new ArrayList<>();
    ArrayList<Rectangle> sønRectangles = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    HashMap<Rectangle, Booking> rectangleBooking = new HashMap<>();

    ListView recent = new ListView<>();
    ListView upcoming = new ListView<>();

    private final SimpleThread simpleThread;

    GEmail ge = new GEmail();

    public BookingController() throws SQLException {
        simpleThread = new SimpleThread(this);
    }

    public void initialize(){
        shownDate = LocalDate.now();
        today = LocalDate.now();
        opsætDato();
        insertSystemBookings();
        simpleThread.start();
        // sendNotificationEmails(); // Aktiver den her når vi får sat en værdi på db der tjekker om der er blevet
        // sendt en notifikation før.
    }
    @FXML
    void opretBookingKnap(ActionEvent event) throws IOException {
        opretBooking(LocalDate.now(), Time.valueOf("07:00:00"), Time.valueOf("12:00:00"));
    }

    public void opretBooking(LocalDate d, Time st, Time et) throws IOException {
        //åben formular med alt booking info, og knap der opdaterer
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("bookingFormular.fxml"));
        Scene oversigtScene = new Scene(fxmlLoader.load());
        OpretFormularController formController = fxmlLoader.getController();
        Stage oversigtStage = new Stage();
        oversigtStage.setScene(oversigtScene);
        formController.opsæt(d, st, et);

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
    void mailKnap(ActionEvent event) {
        //Skift scene
    }

    public void updateNotifications(){

        recent.getItems().clear();
        List<Booking> notiBooking = bdi.recentlyCreated();
        for (Booking b : notiBooking)
        {
            recent.getItems().add(b);
        }

        if(recent.getItems().size() > listSize){
            notits.setImage(redNo);
        }
        else{
            notits.setImage(greyNo);
        }
    }

    @FXML
    void notifikationKnap(ActionEvent event) {

        recent.setPrefHeight(200.0);
        recent.setStyle("-fx-font-family: monospace"); //Listview supporter ikke string.format uden monospace
        recent.setOnMouseClicked(mouseEvent ->{
            seKontaktInfo(recent);
        });

        listSize = recent.getItems().size();

        upcoming.getItems().clear();
        upcoming.setPrefHeight(200.0);
        upcoming.setStyle("-fx-font-family: monospace");
        upcoming.setOnMouseClicked(mouseEvent ->{
            seKontaktInfo(upcoming);
        });

        List<Booking> ucBooking = bdi.upcoming();
        for (Booking b : ucBooking)
        {
            upcoming.getItems().add(b);
        }

        Label l1 = new Label("Nyoprettede bookings");
        l1.setPadding(new Insets(4,0,0,4));
        Label l2 = new Label("Kommende bookings");
        l2.setPadding(new Insets(4,0,0,4));

        VBox vb = new VBox(l1, recent, l2, upcoming);
        vb.setSpacing(2);
        Scene notiScene = new Scene(vb);
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
                //Åben scene med kontakt info
                åbenKontaktInfo(b);
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

        //Labels
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
        contScene.getStylesheets().add(String.valueOf(this.getClass().getResource("stylesheet.css")));
        Stage contStage = new Stage();
        contStage.setTitle("Kontaktinformation");
        contStage.setScene(contScene);

        // Hvis der klikkes udenfor vinduet, lukkes det
        contStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                contStage.hide();
            }
        });
        contStage.show();

        //Åbner nyt vindue med mail
        mail.setOnAction(event -> {
            
        });

        //Åbner nyt vindue med bookingsoversigt
        ændre.setOnAction(event -> {
            try {
                ændreBooking(b);
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
        formController.passBooking(b);

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
    void statestikKnap(ActionEvent event) throws IOException {
        //Åben ny scene med statestikker
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("statestik.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);

    }

    public void dashboardKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("dashboard.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    @FXML
    void forrigeUgeKnap(ActionEvent event) {
        shownDate = shownDate.minusWeeks(1);
        opsætDato();
        insertSystemBookings();
    } //Viser forrige uge i kalenderoversigt

    @FXML
    void todayKnap(ActionEvent event) {
        shownDate = today;
        opsætDato();
        insertSystemBookings();
    } //Viser denne uge i kalenderoversigt

    @FXML
    void næsteUgeKnap(ActionEvent event) {
        shownDate = shownDate.plusWeeks(1);
        opsætDato();
        insertSystemBookings();
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
    } //Sætter alle labels til at passe med datoer

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
    } //Sætter mdr labels til at være dansk

    @FXML
    void mondayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void mondayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(mandagPane, manRectangles);
        }catch (IndexOutOfBoundsException e){
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

        // For loop der finder start og slut index.
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
                    opretBooking(lde,locationMap.get(yValues[startIndex]),locationMap.get(yValues[endIndex]));
                } catch (IOException e){
                    System.err.println("Noget gik galt " + e.getMessage());
                }
            }
        }
    } // Tilføjer en rektangel til der hvor brugeren har klikket vha. mouse drag events.

    public void insertSystemBookings(){
        removeVisuals();

        List<Booking> bookings = bdi.showBooking(shownDate.with(DayOfWeek.MONDAY));

        HashMap<Time, Double> locationMap = new HashMap<>();

        // Et HashMap med tidsværdier og deres korresponderende y-aksis værdier. Bruges til at indsætte bookings på de rigtige placeringer
        locationMap.put(Time.valueOf("07:00:00"),0.0); locationMap.put(Time.valueOf("08:00:00"),44.0); locationMap.put(Time.valueOf("09:00:00"),89.0);
        locationMap.put(Time.valueOf("10:00:00"),134.0); locationMap.put(Time.valueOf("11:00:00"),179.0); locationMap.put(Time.valueOf("12:00:00"),224.0);
        locationMap.put(Time.valueOf("13:00:00"),269.0); locationMap.put(Time.valueOf("14:00:00"),314.0); locationMap.put(Time.valueOf("15:00:00"),359.0);
        locationMap.put(Time.valueOf("16:00:00"),404.0); locationMap.put(Time.valueOf("17:00:00"),449.0); locationMap.put(Time.valueOf("18:00:00"),494.0);
        locationMap.put(Time.valueOf("19:00:00"),539.0); locationMap.put(Time.valueOf("20:00:00"),584.0); locationMap.put(Time.valueOf("21:00:00"),629.0);
        locationMap.put(Time.valueOf("22:00:00"),674.0); locationMap.put(Time.valueOf("23:00:00"),719.0); locationMap.put(Time.valueOf("24:00:00"),764.0);

        // Begynder at indsætte rektangler
        for(Booking book : bookings){

            Rectangle r = new Rectangle();
            Label l = new Label();

            double yStart = locationMap.get(book.getStartTid());
            double yEnd = locationMap.get(book.getSlutTid());

            // Opsæætter formular for hvor at rektanglen skal være i det pane den skal ind i
            r.setY(yStart + 1);
            r.setHeight(yEnd - yStart - 1);
            r.setX(0);
            r.setWidth(133);
            r.setOpacity(0.3);

            // Sætter farven alt efter booking type
            if (book.getBookingType() == 't'){ r.setFill(Color.RED);}
            else {r.setFill(Color.DODGERBLUE);}

            l.setText(book.toString());
            l.setLayoutY(r.getY() + r.getHeight() / 2 - 5);

            labels.add(l);

            // Tilføjer rektangel og book til HashMap der bruges til at tjekke at der ikke er overlap
            rectangleBooking.put(r,book);

            r.onMouseClickedProperty().set(mouseEvent -> {
                //Booking bk = bookings.get(book); // Får information om lige præcis den Booking der hører til objektet
                System.out.println("Clicked on: " + book.getFirstName());
                åbenKontaktInfo(book);
            });
            //Sætter rektanglen ind på det pane som passer til datoen
            if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.MONDAY))){
                manRectangles.add(r);
                mandagPane.getChildren().add(r);
                mandagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.TUESDAY))){
                tirsRectangles.add(r);
                tirsdagPane.getChildren().add(r);
                tirsdagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.WEDNESDAY))){
                onsRectangles.add(r);
                onsdagPane.getChildren().add(r);
                onsdagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.THURSDAY))){
                torsRectangles.add(r);
                torsdagPane.getChildren().add(r);
                torsdagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.FRIDAY))){
                freRectangles.add(r);
                fredagPane.getChildren().add(r);
                fredagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.SATURDAY))){
                lørRectangles.add(r);
                lørdagPane.getChildren().add(r);
                lørdagPane.getChildren().add(l);
            } else if (book.getBookingDate().isEqual(shownDate.with(DayOfWeek.SUNDAY))){
                sønRectangles.add(r);
                søndagPane.getChildren().add(r);
                søndagPane.getChildren().add(l);
            }
        }
    } //Indsætter bookings fra database i kalenderoversigt

    public void removeVisuals(){
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