package com.example.bookingsystem.model;


import com.example.bookingsystem.BookingController;
import javafx.application.Platform;

import java.sql.SQLException;

public class SimpleThread extends Thread {

    private final BookingController controller;
    private boolean isRunning;

    public SimpleThread(BookingController controller) {
        this.controller = controller;
        isRunning = true;
    }

    public void run() {
        while (isRunning) {
            Platform.runLater(() -> {
                controller.insertSystemBookings();
                controller.updateNotifications();
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
