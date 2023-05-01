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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class BookingController {

    @FXML
    private Pane fredagPane, lørdagPane, mandagPane, onsdagPane,
            søndagPane, tirsdagPane, torsdagPane;

    @FXML
    private Label monthLabel, yearLabel;


    private double y_start,y_end;

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
        n.setText("Navn: ");
        Label e = new Label();
        e.setText("e-mail: ");
        Label t = new Label();
        t.setText("tlf.: ");
        VBox vb1 = new VBox(n, e, t);
        vb1.setSpacing(4.0);
        Label navn = new Label();
        navn.setText(b.getFirstName() + " " + b.getLastName());
        Label email = new Label();
        email.setText(b.getEmail());
        Label tlf = new Label();
        tlf.setText(String.valueOf(b.getPhoneNumber()));
        VBox vb2 = new VBox(navn, email, tlf);
        vb2.setSpacing(4.0);
        HBox hb = new HBox(vb1, vb2);
        hb.setSpacing(4.0);

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

        Rectangle r = new Rectangle();
        Label l = new Label();

        double[] yValues = {0, 45, 90, 135, 180, 225, 270, 315, 360, 405, 450, 495, 540, 585, 630, 675, 720, 765};

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
            r.setY(yValues[startIndex]);
            r.setX(0);
            r.setWidth(133);
            r.setHeight(yValues[endIndex] - yValues[startIndex]);
            l.setText("Hygge hejsa");
            l.setLayoutY(yValues[endIndex] - r.getHeight()/2);
        }

        r.setOpacity(0.3);
        p.getChildren().add(r);
        p.getChildren().add(l);
    } // Tilføjer en rektangel til der hvor brugeren har klikket vha. mouse drag events.

    BookingDAO bdi = new BookingDAOImpl();
}