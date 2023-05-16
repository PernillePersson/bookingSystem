package com.example.bookingsystem.model.thread;
import com.example.bookingsystem.controller.DashboardController;
import javafx.application.Platform;

public class DashThread extends Thread {

    private final DashboardController controller;
    private boolean isRunning;

    public DashThread(DashboardController controller) {
        this.controller = controller;
        isRunning = true;
    }

    public void run() {
        while (isRunning) {
            Platform.runLater(() -> {
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