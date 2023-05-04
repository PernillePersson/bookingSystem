package com.example.bookingsystem;

import com.example.bookingsystem.model.BookingDAO;
import com.example.bookingsystem.model.BookingDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class OpretFormularController {

    @FXML
    private TextField eNavn,  email, fNavn, org, tlf;

    @FXML
    private DatePicker bookingDato;

    @FXML
    private ToggleGroup bookingType, forplejning;

    @FXML
    private Hyperlink forplejningLink;

    @FXML
    private RadioButton midlType, permType, noToggle, yesToggle;

    @FXML
    private Button opretBookingKnap;

    @FXML
    private ComboBox slutTid, startTid;

    @FXML
    private Text bemærkning;

    private char type;
    private char forp;

    private Boolean midlertidig;

    public OpretFormularController() throws SQLException {
    }

    public void opsæt(){
        startTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        slutTid.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        startTid.setValue("07:00");
        slutTid.setValue("12:00");
        forplejningLink.setVisible(false);
        bookingDato.setValue(LocalDate.now());
        forp = 'n';
        type = 'p';

    }

    @FXML
    void forplejningToggle(ActionEvent event) {
        if (forplejning.getSelectedToggle() == yesToggle){
            forplejningLink.setVisible(true);
            forp = 'y';
        } else if (forplejning.getSelectedToggle() == noToggle) {
            forplejningLink.setVisible(false);
            forp = 'n';
        }
    }

    @FXML
    void tjekDato(ActionEvent event) {
        bookingDato.setOnAction((e) -> {
            if (slutTid.getSelectionModel().getSelectedIndex() >= 11 ||
                    bookingDato.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    bookingDato.getValue().getDayOfWeek() == DayOfWeek.SUNDAY){
                opretBookingKnap.setText("Anmod om booking");
                bemærkning.setVisible(true);
                type = 't';
                midlertidig = true;

            }else {
                opretBookingKnap.setText("Opret booking");
                bemærkning.setVisible(false);
                type = 'p';
                midlertidig = false;
            }
        });
    }

    @FXML
    void opdaterSlutTid(MouseEvent event) {
        slutTid.setOnAction((e) -> {
            if (slutTid.getSelectionModel().getSelectedIndex() <= startTid.getSelectionModel().getSelectedIndex()){
                slutTid.setValue(slutTid.getItems().get(startTid.getSelectionModel().getSelectedIndex() +1));
            }
            if (slutTid.getSelectionModel().getSelectedIndex() >= 11 ||
                    bookingDato.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    bookingDato.getValue().getDayOfWeek() == DayOfWeek.SUNDAY){
                opretBookingKnap.setText("Anmod om booking");
                bemærkning.setVisible(true);
                type = 't';
                midlertidig = true;
            }else {
                opretBookingKnap.setText("Opret booking");
                bemærkning.setVisible(false);
                type = 'p';
                midlertidig = false;
            }
        });
    }

    @FXML
    void opdaterStartTid(MouseEvent event) {
        startTid.setOnAction((e) -> {
            if (startTid.getSelectionModel().getSelectedIndex() >= slutTid.getSelectionModel().getSelectedIndex()){
                startTid.setValue(startTid.getItems().get(slutTid.getSelectionModel().getSelectedIndex() -1));
            }
        });
    }

    @FXML
    void typeToggle(ActionEvent event) {
        if (bookingType.getSelectedToggle() == midlType){
            type = 't';
        } else if (bookingType.getSelectedToggle() == permType) {
            if (!midlertidig){
                type = 'p';
            }
        }
    }

    @FXML
    void opretBooking(ActionEvent event) {
        int nr = 12345678;
        String organisation = org.getText();
        if (org.getText().isEmpty()){
            organisation = "Ingen";
        }
        bdi.addBooking(fNavn.getText(), eNavn.getText(), organisation, email.getText(), nr,
                type, forp, bookingDato.getValue(), Time.valueOf(startTid.getValue() + ":00"),
                Time.valueOf(slutTid.getValue() + ":00"));
    }

    @FXML
    void anullerBooking(ActionEvent event) {

    }

    BookingDAO bdi = new BookingDAOImpl();
}
