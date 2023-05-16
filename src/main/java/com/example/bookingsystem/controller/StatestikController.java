package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import com.example.bookingsystem.model.DAO.DataCountDAO;
import com.example.bookingsystem.model.DAO.DataCountDAOImpl;
import com.example.bookingsystem.model.DataCount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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

    }

    @FXML
    void fStartDatoValgt(ActionEvent event) {



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
        fChartSlut.setValue(LocalDate.now());
        orgChart.setAnimated(false);
    }


    DataCountDAO dc = new DataCountDAOImpl();

}
