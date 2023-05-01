package com.example.bookingsystem;

import com.example.bookingsystem.model.Booking;
import com.example.bookingsystem.model.BookingCode;
import com.example.bookingsystem.model.BookingDAO;
import com.example.bookingsystem.model.BookingDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.lang.Math;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingController {

    @FXML
    private Pane fredagPane, lørdagPane, mandagPane, onsdagPane,
            søndagPane, tirsdagPane, torsdagPane;

    @FXML
    private Label monthLabel, yearLabel;


    private double y_start,y_end;
    ArrayList<Rectangle> rectangles = new ArrayList<>();
    HashMap<Rectangle, Booking> rectangleBooking = new HashMap<>();

    ListView recent = new ListView<>();
    ListView upcoming = new ListView<>();

    public BookingController() throws SQLException {
    }

    public void initialize(){

    }
    @FXML
    void opretBookingKnap(ActionEvent event) {

    }

    @FXML
    void mailKnap(ActionEvent event) {

    }

    @FXML
    void notifikationKnap(ActionEvent event) {
        recent.getItems().clear();
        recent.setPrefHeight(200.0);
        recent.setStyle("-fx-font-family: monospace"); //Listview supporter ikke string.format uden monospace
        recent.setOnMouseClicked(mouseEvent ->{
            seKontaktInfo(recent);
        });

        List<Booking> rBooking = bdi.recentlyCreated();
        for (Booking b : rBooking)
        {
            recent.getItems().add(b);
        }

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

    }

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
    }

    public void åbenKontaktInfo(Booking b){
        Label n = new Label();
        n.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        n.setText("Navn: ");
        Label e = new Label();
        e.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        e.setText("e-mail: ");
        Label t = new Label();
        t.setFont(Font.font("ARIAL", FontWeight.BOLD, 13.0));
        t.setText("tlf.: ");
        VBox vb1 = new VBox(n, e, t);
        vb1.setSpacing(4.0);
        vb1.setAlignment(Pos.CENTER_LEFT);
        vb1.setPadding(new Insets(0, 20, 0, 0));
        Label navn = new Label();
        navn.setText(b.getFirstName() + " " + b.getLastName());
        Label email = new Label();
        email.setText(b.getEmail());
        Label tlf = new Label();
        tlf.setText(String.valueOf(b.getPhoneNumber()));
        VBox vb2 = new VBox(navn, email, tlf);
        vb2.setSpacing(4.0);
        vb2.setAlignment(Pos.CENTER_LEFT);
        HBox hb = new HBox(vb1, vb2);
        hb.setSpacing(4.0);
        hb.setAlignment(Pos.CENTER);
        hb.setPrefWidth(250);
        hb.setPrefHeight(150);

        Scene contScene = new Scene(hb, 400, 200);
        Stage contStage = new Stage();
        contStage.setTitle("Kontaktinformation");
        contStage.setScene(contScene);

        contStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                contStage.hide(); // Hvis der klikkes udenfor vinduet, lukkes det
            }
        });

        contStage.show();
    }
    @FXML
    void statestikKnap(ActionEvent event) {

    }

    @FXML
    void forrigeUgeKnap(ActionEvent event) {

    }

    @FXML
    void todayKnap(ActionEvent event) {

    }

    @FXML
    void næsteUgeKnap(ActionEvent event) {

    }

    @FXML
    void mondayDrag(MouseEvent event) {}

    @FXML
    void mondayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void mondayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(mandagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void tuesdayDrag(MouseEvent event) {}

    @FXML
    void tuesdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void tuesdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(tirsdagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }
    @FXML
    void wednesdayDrag(MouseEvent event) {}

    @FXML
    void wednesdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void wednesdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(onsdagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    @FXML
    void thursdayDrag(MouseEvent event) {}

    @FXML
    void thursdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void thursdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(torsdagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }
    @FXML
    void fridayDrag(MouseEvent event) {}

    @FXML
    void fridayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void fridayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(fredagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }


    @FXML
    void saturdayDrag(MouseEvent event) {}

    @FXML
    void saturdayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void saturdayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(lørdagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }


    @FXML
    void sundayDrag(MouseEvent event) {}

    @FXML
    void sundayPress(MouseEvent event) {
        event.setDragDetect(true);
        y_start = event.getY();
    }

    @FXML
    void sundayRelease(MouseEvent event) {
        try {
            y_end = event.getY();
            addStack(søndagPane);
        }catch (IndexOutOfBoundsException e){
            System.err.println("Kan ikke oprette booking udenfor kalenderen");
        }
    }

    public void addStack(Pane p){

        Label l = new Label();
        ArrayList<String> aList = new ArrayList<>();
        aList.add("Magnus");
        aList.add("Marc");
        aList.add("Pernille");

        int rand = (int) (Math.random() * aList.size());

        // Array med de værdier som vi skal bruge mht. at indsætte rektangel på det korrekte sted

        double[] yValues = {0, 44, 89, 134, 179, 224, 269, 314, 359, 404, 449, 494, 539, 584, 629, 674, 719, 764};

        // Find start og slut indekset for den nye rektangel
        
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < yValues.length; i++) {
            if (y_start >= yValues[i] && y_start < yValues[i + 1]) {
                startIndex = i;
            }
            if (y_end >= yValues[i] && y_end < yValues[i + 1]) {
                endIndex = i + 1;
                break;
            }
        }

        if (startIndex != -1 && endIndex != -1) {
            Rectangle r = new Rectangle();
            Booking book = new Booking((int) Math.random(),aList.get(rand),"Hansen","EASV","madmedmig@gmail.com",1234,'t','y', LocalDate.of(2023,04,03),LocalDate.of(2023,03,30),"131231",Time.valueOf("10:00:00"),Time.valueOf("15:00:00"));

            r.setY(yValues[startIndex] + 1);
            r.setX(0);
            r.setWidth(133);
            r.setHeight(yValues[endIndex] - yValues[startIndex] - 1);
            l.setLayoutY(yValues[endIndex] - r.getHeight() / 2);
            r.setOpacity(0.3);
            l.setText(book.toString());

            r.layoutYProperty().addListener((obs,oldVal,newVal) -> {
                l.setLayoutY(newVal.doubleValue() / 2);
            });


            boolean intersects = false;
            for (Rectangle rec : rectangles) {
                if (rec.getY() + rec.getHeight() >= r.getY() && rec.getY() <= r.getY() + r.getHeight()) {
                    intersects = true;
                    break;
                }
            }
            if (!intersects) {
                // indsæt en tilføjelse af booking her eller i vores MandagPane release event
                rectangleBooking.put(r,book);
                rectangles.add(r);
                p.getChildren().add(r);
                p.getChildren().add(l);
            }
        }
    } // Tilføjer en rektangel til der hvor brugeren har klikket vha. mouse drag events.

    public void insertSystemBookings(){

        List<Booking> bookings = bdi.getAllBooking();
        HashMap<Time, Double> locationMap = new HashMap<>();

        locationMap.put(Time.valueOf("7:00:00"),0.0); locationMap.put(Time.valueOf("8:00:00"),44.0); locationMap.put(Time.valueOf("9:00:00"),89.0);
        locationMap.put(Time.valueOf("10:00:00"),134.0); locationMap.put(Time.valueOf("11:00:00"),179.0); locationMap.put(Time.valueOf("12:00:00"),224.0);
        locationMap.put(Time.valueOf("13:00:00"),269.0); locationMap.put(Time.valueOf("14:00:00"),314.0); locationMap.put(Time.valueOf("15:00:00"),359.0);
        locationMap.put(Time.valueOf("16:00:00"),404.0); locationMap.put(Time.valueOf("17:00:00"),449.0); locationMap.put(Time.valueOf("18:00:00"),494.0);
        locationMap.put(Time.valueOf("19:00:00"),539.0); locationMap.put(Time.valueOf("20:00:00"),584.0); locationMap.put(Time.valueOf("21:00:00"),629.0);
        locationMap.put(Time.valueOf("22:00:00"),674.0); locationMap.put(Time.valueOf("23:00:00"),719.0); locationMap.put(Time.valueOf("24:00:00"),764.0);

        for(Booking book : bookings){

            Rectangle r = new Rectangle();
            Label l = new Label();

            double yStart = locationMap.get(book.getStartTid());
            double yEnd = locationMap.get(book.getSlutTid());

            l.setText(book.toString());

            r.setY(yStart + 1);
            r.setHeight(yEnd - yStart - 1);
            r.setX(0);
            r.setWidth(133);
            r.setOpacity(0.3);

            l.setLayoutY(r.getY() + r.getHeight() / 2 - 5);

            rectangles.add(r);
            rectangleBooking.put(r,book);

            System.out.println(rectangleBooking);
            System.out.println(locationMap);

            r.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //Booking bk = bookings.get(book); // Får information om lige præcis den Booking der hører til objektet
                    System.out.println("Clicked on: ");
                }
            });
            mandagPane.getChildren().add(r);
            mandagPane.getChildren().add(l);
        }
        // for each (lav ny rectangle). Når vi laver en metode der sætter alt ind, så skal vi sørge for at vi tager datoen fra i dag af. Dvs, vi loader kun
        // de bookinger ind der har relevans for den uge vi er i.
    }

    BookingDAO bdi = new BookingDAOImpl();
}