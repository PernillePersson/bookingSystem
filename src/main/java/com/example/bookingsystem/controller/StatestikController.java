package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;
import com.example.bookingsystem.model.DAO.DataCountDAO;
import com.example.bookingsystem.model.DAO.DataCountDAOImpl;
import com.example.bookingsystem.model.DataCount;
import com.example.bookingsystem.model.objects.Booking;
import com.example.bookingsystem.model.thread.StatsThread;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class StatestikController {

    private int listSize; //længde på notifikationslisten
    private ListView recent = new ListView<>();
    private ListView upcoming = new ListView<>();

    @FXML
    private DatePicker fChartSlut, fChartStart, oChartSlut, oChartStart;

    @FXML
    private PieChart forløbChart;

    @FXML
    private Image greyNo, redNo;

    @FXML
    private ImageView notits;

    @FXML
    private BarChart<Number, String> orgChart;

    private final StatsThread statsThread;

    public StatestikController() throws SQLException, FileNotFoundException {
        statsThread = new StatsThread(this);
    }

    public void initialize(){
        statsThread.start();
        setTime();
    }

    public void passListsize(int size){
        listSize = size; //Overfører længde på notifikationsliste fra andre scener
    }

    public void weekviewKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("weekView.fxml"));
        Scene weekScene = new Scene(fxmlLoader.load());
        BookingController boController = fxmlLoader.getController();
        boController.passListsize(listSize); //Overfører listsize fra denne scene til den nye
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(weekScene); //Åbner scene med kalenderoversigt
    }

    public void dashboardKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("dashboard.fxml"));
        Scene dashScene = new Scene(fxmlLoader.load());
        DashboardController dbController = fxmlLoader.getController();
        dbController.passListsize(listSize); //Overfører listsize fra denne scene til den nye
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(dashScene); //Åbner scene med kalenderoversigt
    }

    @FXML
    void notifikationKnap(ActionEvent event) {
        //Samme medtode som i BookingController
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
        l1.setPadding(new javafx.geometry.Insets(4,0,0,4));
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

    public void updateNotifications(){
        //Samme medtode som i BookingController
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

    public void seKontaktInfo(ListView l){ //Samme medtode som i BookingController
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

    public void åbenKontaktInfo(Booking b){ //Samme medtode som i BookingController
        //Labels
        Label n = new Label();
        n.setFont(javafx.scene.text.Font.font("ARIAL", FontWeight.BOLD, 13.0));
        n.setText("Navn: ");
        Label e = new Label();
        e.setFont(javafx.scene.text.Font.font("ARIAL", FontWeight.BOLD, 13.0));
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

    public void mailKnap(ActionEvent event) throws IOException {
        Desktop.getDesktop().mail(); //Åbner default mailprogram på pc
    }

    public void opretBookingKnap(ActionEvent event) throws IOException {
        //Samme medtode som i BookingController
        opretBooking(LocalDate.now(), Time.valueOf("07:00:00"), Time.valueOf("12:00:00"));
    }

    public void opretBooking(LocalDate d, Time st, Time et) throws IOException {
        //Samme medtode som i BookingController
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

    @FXML
    void fSlutDatoValgt(ActionEvent event) {

        forløbChart.getData().clear();

        //Indsætter antal af forløb en booking har i lister
        List<DataCount> ideData = dc.importData("Idéfabrikken",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> laserData = dc.importData("Digital fabrikation med laserskærer",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotPåJobData = dc.importData("Robot på job",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotOprydData = dc.importData("Robotten rydder op",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> naturData = dc.importData("Naturturisme ved Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> sikkerhedData = dc.importData("Skab sikkerhed i Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> ingenData = dc.importData("Ingen",fChartStart.getValue(),fChartSlut.getValue());

        //sætter antal forløb til 0
        int ide = 0;
        int laser = 0;
        int rpj = 0;
        int ro = 0;
        int natur = 0;
        int sik = 0;
        int ing = 0;

        //For hver booking der har bestemt forløb i databasen, tælles antal af forløb op
        for(DataCount i : ideData)
            ide++;

        for(DataCount l : laserData)
            laser++;

        for(DataCount r : robotPåJobData)
            rpj++;

        for(DataCount rod : robotOprydData)
            ro++;

        for(DataCount n : naturData)
            natur++;

        for(DataCount s : sikkerhedData)
            sik++;

        for(DataCount in : ingenData)
            ing++;

        //Opsætter antal af forløb i pieChart
        ObservableList<PieChart.Data> forløbData = FXCollections.observableArrayList(
                new PieChart.Data("Idéfabrikken",ide),
                new PieChart.Data("Digital fabrikation med laserskærer",laser),
                new PieChart.Data("Robot på job",rpj),
                new PieChart.Data("Robotten rydder op",ro),
                new PieChart.Data("Naturturisme ved Vadehavet",natur),
                new PieChart.Data("Skab sikkerhed i Vadehavet",sik),
                new PieChart.Data("Ingen",ing)
        );

        //Opætter piechartet visuelt
        forløbChart.setLegendVisible(true);
        forløbChart.setLabelsVisible(true);
        forløbChart.titleProperty().set("Bookings af forløb");
        forløbChart.setData(forløbData);
        forløbChart.setStartAngle(180);
        forløbChart.getLabelsVisible();

        final Label pieLabel = new Label("");
        pieLabel.setTextFill(Color.BLACK);
        pieLabel.setStyle("-fx-font: 20 ARIAL;");

        createToolTips(forløbChart); //Kører man musen hen over en "pie", vises forløbnavn og antal

        forløbData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
    }

    @FXML
    void fStartDatoValgt(ActionEvent event) {

        forløbChart.getData().clear();

        //Indsætter antal af forløb en booking har i lister
        List<DataCount> ideData = dc.importData("Idéfabrikken",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> laserData = dc.importData("Digital fabrikation med laserskærer",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotPåJobData = dc.importData("Robot på job",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotOprydData = dc.importData("Robotten rydder op",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> naturData = dc.importData("Naturturisme ved Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> sikkerhedData = dc.importData("Skab sikkerhed i Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> ingenData = dc.importData("Ingen",fChartStart.getValue(),fChartSlut.getValue());

        //sætter antal forløb til 0
        int ide = 0;
        int laser = 0;
        int rpj = 0;
        int ro = 0;
        int natur = 0;
        int sik = 0;
        int ing = 0;

        //For hver booking der har bestemt forløb i databasen, tælles antal af forløb op
        for(DataCount i : ideData)
            ide++;

        for(DataCount l : laserData)
            laser++;

        for(DataCount r : robotPåJobData)
            rpj++;

        for(DataCount rod : robotOprydData)
            ro++;

        for(DataCount n : naturData)
            natur++;

        for(DataCount s : sikkerhedData)
            sik++;

        for(DataCount in : ingenData)
            ing++;

        //Opsætter antal af forløb i pieChart
        ObservableList<PieChart.Data> forløbData = FXCollections.observableArrayList(
                new PieChart.Data("Idéfabrikken",ide),
                new PieChart.Data("Digital fabrikation med laserskærer",laser),
                new PieChart.Data("Robot på job",rpj),
                new PieChart.Data("Robotten rydder op",ro),
                new PieChart.Data("Naturturisme ved Vadehavet",natur),
                new PieChart.Data("Skab sikkerhed i Vadehavet",sik),
                new PieChart.Data("Ingen",ing)
                );

        //Opætter piechartet visuelt
        forløbChart.setLegendVisible(true);
        forløbChart.setLabelsVisible(true);
        forløbChart.setTitle("Bookede forløb");
        forløbChart.setData(forløbData);
        forløbChart.setStartAngle(180);
        forløbChart.getLabelsVisible();

        final Label pieLabel = new Label("");
        pieLabel.setTextFill(Color.BLACK);
        pieLabel.setStyle("-fx-font: 20 ARIAL;");


        createToolTips(forløbChart); //Kører man musen hen over en "pie", vises forløbnavn og antal

        forløbData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
    }

    @FXML
    void oSlutDatoValgt(ActionEvent event) {

        orgChart.getData().clear();

        //Opsætter barchart
        Axis<Number> xAxis = orgChart.getXAxis();
        xAxis.setLabel("Bookede forløb");
        Axis<String> yAxis = orgChart.getYAxis();
        yAxis.setLabel("Organisationer");

        xAxis.setTickLength(1.0);
        yAxis.setTickLength(1.0);

        //Indsætter organisationer i lister for hver gang de har booket
        List<DataCount> eccoData = dc.importOrgStatData("Ecco", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> folkeskoleData = dc.importOrgStatData("Folkeskole", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderGymData = dc.importOrgStatData("Tønder Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> detBlåGymData = dc.importOrgStatData("Det Blå Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderKomData = dc.importOrgStatData("Tønder Kommune", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> andetData = dc.importOrgStatData("Andet", oChartStart.getValue(), oChartSlut.getValue());

        //Laver serie med organisationer
        XYChart.Series<Number, String> eccoBar = new XYChart.Series<>();
        XYChart.Series<Number, String> folkeskoleBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> detBlåGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderKomBar = new XYChart.Series<>();
        XYChart.Series<Number, String> andetBar = new XYChart.Series<>();

        //Sætter antal gange organisation har booket til 0
        int ecco = 0;
        int folkeskole = 0;
        int tønderGym = 0;
        int detBlå = 0;
        int tønderKom = 0;
        int andet = 0;

        //Tæller antal gange organisationer har booket op, for hver der er i hver liste,
        //og indsætter det i chart data
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
        //Indsætter alt chart data i barchartet for hver organisation
        orgChart.getData().addAll(eccoBar,folkeskoleBar,tønderGymBar,detBlåGymBar,tønderKomBar,andetBar);

    }

    @FXML
    void oStartDatoValgt(ActionEvent event) {

        orgChart.getData().clear();

        //Opsætter barchart
        Axis<Number> xAxis = orgChart.getXAxis();
        xAxis.setLabel("Bookede forløb");
        Axis<String> yAxis = orgChart.getYAxis();
        yAxis.setLabel("Organisationer");

        xAxis.setTickLength(1.0);
        yAxis.setTickLength(1.0);

        //Indsætter organisationer i lister for hver gang de har booket
        List<DataCount> eccoData = dc.importOrgStatData("Ecco", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> folkeskoleData = dc.importOrgStatData("Folkeskole", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderGymData = dc.importOrgStatData("Tønder Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> detBlåGymData = dc.importOrgStatData("Det Blå Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderKomData = dc.importOrgStatData("Tønder Kommune", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> andetData = dc.importOrgStatData("Andet", oChartStart.getValue(), oChartSlut.getValue());

        //Laver serie med organisationer
        XYChart.Series<Number, String> eccoBar = new XYChart.Series<>();
        XYChart.Series<Number, String> folkeskoleBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> detBlåGymBar = new XYChart.Series<>();
        XYChart.Series<Number, String> tønderKomBar = new XYChart.Series<>();
        XYChart.Series<Number, String> andetBar = new XYChart.Series<>();

        //Sætter antal gange organisation har booket til 0
        int ecco = 0;
        int folkeskole = 0;
        int tønderGym = 0;
        int detBlå = 0;
        int tønderKom = 0;
        int andet = 0;

        //Tæller antal gange organisationer har booket op, for hver der er i hver liste,
        //og indsætter det i chart data
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
        //Indsætter alt chart data i barchartet for hver organisation
        orgChart.getData().addAll(eccoBar,folkeskoleBar,tønderGymBar,detBlåGymBar,tønderKomBar,andetBar);
    }

    public void setTime(){
        oChartSlut.setValue(LocalDate.now());
        oChartStart.setValue(LocalDate.now());
        fChartSlut.setValue(LocalDate.now());
        fChartStart.setValue(LocalDate.now());
        orgChart.setAnimated(false);
        forløbChart.setAnimated(false);
    } // Sætter dato for datepickers til dags dato

    private void createToolTips(PieChart pc) {
        for (PieChart.Data data: pc.getData()) {
            String msg = data.getName() + ": " + data.getPieValue();

            Tooltip tp = new Tooltip(msg);
            tp.setShowDelay(Duration.seconds(0));

            Tooltip.install(data.getNode(), tp);

            //updater data når musen kører hen på ny "pie"
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
            {
                tp.setText(newValue.toString());
            });
        } //Når muser hoverer hen over en "pie", viser den navn og værdi for data på den pågældene "pie"
    }


    DataCountDAO dc = new DataCountDAOImpl();
    BookingDAO bdi = new BookingDAOImpl();

}
