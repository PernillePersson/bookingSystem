package com.example.bookingsystem.model.thread;
import com.example.bookingsystem.controller.StatestikController;
import javafx.application.Platform;

public class StatsThread extends Thread {

    private final StatestikController controller;
    private boolean isRunning;

    public StatsThread(StatestikController controller) {
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