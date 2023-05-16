package com.example.bookingsystem.controller;

import com.example.bookingsystem.BookingApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

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
    private BarChart<?, ?> orgChart;


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

    }

    @FXML
    void oStartDatoValgt(ActionEvent event) {

    }



}
