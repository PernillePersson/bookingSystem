package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.DAO.DataCountDAO;
import com.example.bookingsystem.model.DAO.DataCountDAOImpl;
import com.example.bookingsystem.model.DashThread;
import com.example.bookingsystem.model.DataCount;
import com.example.bookingsystem.model.SimpleThread;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.objects.SlideImage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private int listSize;
    ListView recent = new ListView<>();
    ListView upcoming = new ListView<>();
    ListView dashBookingList = new ListView<>();

    @FXML
    private Image greyNo, redNo;

    @FXML
    private ImageView notits,dashImage;

    @FXML
    private BarChart dashboardChart;

    private final DashThread dashThread;

    private final List<File> images = new ArrayList<>();

    private int currentImageIndex = 0;

    private SlideImage shownImage = null;


    public DashboardController() throws SQLException {
        dashThread = new DashThread(this);
    }

    public void initialize(){
        dashThread.start();
        imageShow();
        generateData();
        visKommende();
    }

    public void statestikKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("statestik.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    public void weekviewKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("weekView.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    public void opretBookingKnap(ActionEvent event) {

    }

    public void mailKnap(ActionEvent event) {
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

    public void visKommende(){
        List<Booking> ucBooking = bdi.upcoming();
        dashBookingList.setStyle("-fx-font-family: monospace");
        for (Booking b : ucBooking)
        {
            dashBookingList.getItems().add(b);
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

    public void imageShow() {

        File f1 = new File("src/main/resources/com/example/bookingsystem/image.png");
        File f2 = new File("src/main/resources/com/example/bookingsystem/image (1).png");
        File f3 = new File("src/main/resources/com/example/bookingsystem/image (2).png");
        File f4 = new File("src/main/resources/com/example/bookingsystem/image (3).png");
        File f5 = new File("src/main/resources/com/example/bookingsystem/image (4).png");

        images.add(f1);
        images.add(f2);
        images.add(f3);
        images.add(f4);
        images.add(f5);


        if (shownImage == null) {

            shownImage = new SlideImage(images, currentImageIndex);
            shownImage.valueProperty().addListener((ov, oldImage, newImage) -> {
                dashImage.setImage(newImage);

                if(newImage != oldImage){
                    currentImageIndex = shownImage.getIndex();
                }
            });

            Thread imageThread = new Thread(shownImage);
            imageThread.setDaemon(true);
            imageThread.start();
        }
    }

    public void generateData(){

        Axis<Number> xAxis = dashboardChart.getXAxis();
        xAxis.setLabel("Bookede forløb");
        Axis<String> yAxis = dashboardChart.getYAxis();
        yAxis.setLabel("Organisationer");


        List<DataCount> eccoData = dc.importOrgData("Ecco");
        List<DataCount> folkeskoleData = dc.importOrgData("Folkeskole");
        List<DataCount> tønderGymData = dc.importOrgData("Tønder Gymnasium");
        List<DataCount> detBlåGymData = dc.importOrgData("Det Blå Gymnasium");
        List<DataCount> tønderKomData = dc.importOrgData("Tønder Kommune");
        List<DataCount> andetData = dc.importOrgData("Andet");

        XYChart.Series<Number, String> eccoBar = new XYChart.Series<>();
        XYChart.Series<Number, String> folkeskoleBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> detBlåGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderKomBar = new XYChart.Series<>();
        XYChart.Series<Number, String> andetBar = new XYChart.Series<>();

        int ecco = 0;
        int folkeskole = 0;
        int tønderGym = 0;
        int detBlå = 0;
        int tønderKom = 0;
        int andet = 0;

        for(DataCount e : eccoData){
            ecco++;
            eccoBar.getData().add(new XYChart.Data<>(ecco,"Ecco"));
        }

        for(DataCount f : folkeskoleData){
            folkeskole++;
            folkeskoleBar.getData().add(new XYChart.Data<>(folkeskole,"Folkeskole"));
        }

        for(DataCount tg : tønderGymData){
            tønderGym++;
            tønderGymBar.getData().add(new XYChart.Data<>(tønderGym,"Tønder Gymnasium"));
        }

        for(DataCount dbg : detBlåGymData){
            detBlå++;
            detBlåGymBar.getData().add(new XYChart.Data<>(detBlå,"Det Blå Gymnasium"));
        }

        for(DataCount tk : tønderKomData){
            tønderKom++;
            tønderKomBar.getData().add(new XYChart.Data<>(tønderKom,"Tønder Kommune"));
        }

        for(DataCount a : andetData){
            andet++;
            andetBar.getData().add(new XYChart.Data<>(andet,"Andet"));
        }

        System.out.println(ecco + " " + tønderGym);


        dashboardChart.getData().addAll(eccoBar,folkeskoleBar,tønderGymBar,detBlåGymBar,tønderKomBar,andetBar);
    }


    BookingDAO bdi = new BookingDAOImpl();
    DataCountDAO dc = new DataCountDAOImpl();

    public void todayPress(MouseEvent mouseEvent) {
    }

    public void todayRelease(MouseEvent mouseEvent) {
    }
}