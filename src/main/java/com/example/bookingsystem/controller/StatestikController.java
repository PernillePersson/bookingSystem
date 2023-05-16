package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.thread.DashThread;
import com.example.bookingsystem.model.thread.StatsThread;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class StatestikController {

    private int listSize;
    ListView recent = new ListView<>();
    ListView upcoming = new ListView<>();

    @FXML
    private DatePicker fChartSlut, fChartStart, oChartSlut, oChartStart;

    @FXML
    private PieChart forløbChart;

    @FXML
    private Image greyNo, redNo;

    @FXML
    private ImageView notits;

    @FXML
    private BarChart<?, ?> orgChart;

    private final StatsThread statsThread;

    public StatestikController() throws SQLException {
        statsThread = new StatsThread(this);
    }

    public void initialize(){
        statsThread.start();
    }

    public void weekviewKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("weekView.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    public void notifikationKnap(ActionEvent event) {
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


        //opsætning i vbox
        VBox vb1 = new VBox(n, e, t);
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


        //Opsætning i vbox
        VBox vb2 = new VBox(navn, email, tlf);
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

    } //Åbner vindue med kontaktinfo fra object

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

    public void mailKnap(ActionEvent event) {
    }

    public void opretBookingKnap(ActionEvent event) throws IOException {
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
            }
        });

        oversigtStage.show();
    }


    public void dashboardKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("dashboard.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    @FXML
    void fSlutDatoValgt(ActionEvent event) {

    }

    @FXML
    void fStartDatoValgt(ActionEvent event) {

    }



    @FXML
    void oSlutDatoValgt(ActionEvent event) {

    }

    @FXML
    void oStartDatoValgt(ActionEvent event) {

    }


    BookingDAO bdi = new BookingDAOImpl();

}
