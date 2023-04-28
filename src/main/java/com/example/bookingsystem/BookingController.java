package com.example.bookingsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    @FXML
    void opretBookingKnap(ActionEvent event) {

    }

    @FXML
    void mailKnap(ActionEvent event) {

    }

    @FXML
    void notifikationKnap(ActionEvent event) {

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
        y_end = event.getY();
        addStack(mandagPane);
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
        y_end = event.getY();
        addStack(tirsdagPane);
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
        y_end = event.getY();
        addStack(onsdagPane);
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
        y_end = event.getY();
        addStack(torsdagPane);
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
        y_end = event.getY();
        addStack(fredagPane);
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
        y_end = event.getY();
        addStack(lørdagPane);
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
        y_end = event.getY();
        addStack(søndagPane);
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
}