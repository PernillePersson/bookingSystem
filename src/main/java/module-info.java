module com.example.bookingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;


    opens com.example.bookingsystem to javafx.fxml;
    exports com.example.bookingsystem;
}