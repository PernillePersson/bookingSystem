package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.model.DAO.DataCountDAO;
import com.example.bookingsystem.model.DAO.DataCountDAOImpl;
import com.example.bookingsystem.model.DataCount;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatestikController {

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

    public StatestikController() throws SQLException {
    }

    public void initialize(){
        setTime();
    }


    public void weekviewKnap(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookingApplication.class.getResource("weekView.fxml"));
        Scene statestikScene = new Scene(fxmlLoader.load());
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setScene(statestikScene);
    }

    public void notifikationKnap(ActionEvent event) {
    }

    public void mailKnap(ActionEvent event) {
    }

    public void opretBookingKnap(ActionEvent event) {
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

        forløbChart.getData().clear();



        List<DataCount> ideData = dc.importData("Idéfabrikken",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> laserData = dc.importData("Digital fabrikation med laserskærer",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotPåJobData = dc.importData("Robot på job",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotOprydData = dc.importData("Robotten rydder op",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> naturData = dc.importData("Naturturisme ved Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> sikkerhedData = dc.importData("Skab sikkerhed i Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> ingenData = dc.importData("Ingen",fChartStart.getValue(),fChartSlut.getValue());

        int ide = 0;
        int laser = 0;
        int rpj = 0;
        int ro = 0;
        int natur = 0;
        int sik = 0;
        int ing = 0;

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


        ObservableList<PieChart.Data> forløbData = FXCollections.observableArrayList(
                new PieChart.Data("Idéfabrikken",ide),
                new PieChart.Data("Digital fabrikation med laserskærer",laser),
                new PieChart.Data("Robot på job",rpj),
                new PieChart.Data("Robotten rydder op",ro),
                new PieChart.Data("Naturturisme ved Vadehavet",natur),
                new PieChart.Data("Skab sikkerhed i Vadehavet",sik),
                new PieChart.Data("Ingen",ing)
        );


        forløbChart.setLegendVisible(true);
        forløbChart.setLabelsVisible(true);
        forløbChart.titleProperty().set("Bookings af forløb");
        forløbChart.setData(forløbData);
        forløbChart.setStartAngle(180);
        forløbChart.getLabelsVisible();

        final Label pieLabel = new Label("");
        pieLabel.setTextFill(Color.BLACK);
        pieLabel.setStyle("-fx-font: 20 ARIAL;");

        createToolTips(forløbChart);

        forløbData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
    }

    @FXML
    void fStartDatoValgt(ActionEvent event) {

        forløbChart.getData().clear();

        List<DataCount> ideData = dc.importData("Idéfabrikken",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> laserData = dc.importData("Digital fabrikation med laserskærer",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotPåJobData = dc.importData("Robot på job",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> robotOprydData = dc.importData("Robotten rydder op",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> naturData = dc.importData("Naturturisme ved Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> sikkerhedData = dc.importData("Skab sikkerhed i Vadehavet",fChartStart.getValue(),fChartSlut.getValue());
        List<DataCount> ingenData = dc.importData("Ingen",fChartStart.getValue(),fChartSlut.getValue());

        int ide = 0;
        int laser = 0;
        int rpj = 0;
        int ro = 0;
        int natur = 0;
        int sik = 0;
        int ing = 0;

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


        ObservableList<PieChart.Data> forløbData = FXCollections.observableArrayList(
                new PieChart.Data("Idéfabrikken",ide),
                new PieChart.Data("Digital fabrikation med laserskærer",laser),
                new PieChart.Data("Robot på job",rpj),
                new PieChart.Data("Robotten rydder op",ro),
                new PieChart.Data("Naturturisme ved Vadehavet",natur),
                new PieChart.Data("Skab sikkerhed i Vadehavet",sik),
                new PieChart.Data("Ingen",ing)
                );

        forløbChart.setLegendVisible(true);
        forløbChart.setLabelsVisible(true);
        forløbChart.setTitle("Bookede forløb");
        forløbChart.setData(forløbData);
        forløbChart.setStartAngle(180);
        forløbChart.getLabelsVisible();

        final Label pieLabel = new Label("");
        pieLabel.setTextFill(Color.BLACK);
        pieLabel.setStyle("-fx-font: 20 ARIAL;");


        createToolTips(forløbChart);

        forløbData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
    }

    @FXML
    void oSlutDatoValgt(ActionEvent event) {

        orgChart.getData().clear();

        Axis<Number> xAxis = orgChart.getXAxis();
        xAxis.setLabel("Bookede forløb");
        Axis<String> yAxis = orgChart.getYAxis();
        yAxis.setLabel("Organisationer");

        xAxis.setTickLength(1.0);
        yAxis.setTickLength(1.0);

        List<DataCount> eccoData = dc.importOrgStatData("Ecco", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> folkeskoleData = dc.importOrgStatData("Folkeskole", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderGymData = dc.importOrgStatData("Tønder Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> detBlåGymData = dc.importOrgStatData("Det Blå Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderKomData = dc.importOrgStatData("Tønder Kommune", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> andetData = dc.importOrgStatData("Andet", oChartStart.getValue(), oChartSlut.getValue());

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
        orgChart.getData().addAll(eccoBar,folkeskoleBar,tønderGymBar,detBlåGymBar,tønderKomBar,andetBar);

    }

    @FXML
    void oStartDatoValgt(ActionEvent event) {

        orgChart.getData().clear();

        Axis<Number> xAxis = orgChart.getXAxis();
        xAxis.setLabel("Bookede forløb");
        Axis<String> yAxis = orgChart.getYAxis();
        yAxis.setLabel("Organisationer");

        xAxis.setTickLength(1.0);
        yAxis.setTickLength(1.0);


        List<DataCount> eccoData = dc.importOrgStatData("Ecco", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> folkeskoleData = dc.importOrgStatData("Folkeskole", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderGymData = dc.importOrgStatData("Tønder Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> detBlåGymData = dc.importOrgStatData("Det Blå Gymnasium", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> tønderKomData = dc.importOrgStatData("Tønder Kommune", oChartStart.getValue(), oChartSlut.getValue());
        List<DataCount> andetData = dc.importOrgStatData("Andet", oChartStart.getValue(), oChartSlut.getValue());

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

        orgChart.getData().addAll(eccoBar,folkeskoleBar,tønderGymBar,detBlåGymBar,tønderKomBar,andetBar);
    }

    public void setTime(){
        oChartSlut.setValue(LocalDate.now());
        oChartStart.setValue(LocalDate.now());
        fChartSlut.setValue(LocalDate.now());
        fChartStart.setValue(LocalDate.now());
        orgChart.setAnimated(false);
        forløbChart.setAnimated(false);
    }

    private void createToolTips(PieChart pc) {

        for (PieChart.Data data: pc.getData()) {
            String msg = data.getName() + ": " + data.getPieValue();

            Tooltip tp = new Tooltip(msg);
            tp.setShowDelay(Duration.seconds(0));

            Tooltip.install(data.getNode(), tp);

            //update tooltip data when changed
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
            {
                tp.setText(newValue.toString());
            });
        }
    }


    DataCountDAO dc = new DataCountDAOImpl();

}
