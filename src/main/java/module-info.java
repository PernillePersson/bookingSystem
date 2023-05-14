module com.example.bookingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;
    requires java.desktop;


    opens com.example.bookingsystem to javafx.fxml;
    exports com.example.bookingsystem;
    exports com.example.bookingsystem.controller;
    opens com.example.bookingsystem.controller to javafx.fxml;
}