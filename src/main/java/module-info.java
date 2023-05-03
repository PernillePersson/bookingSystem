module com.example.bookingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;

    exports userBooking.Controllers;
    opens userBooking.Controllers to javafx.fxml;
    exports userBooking;
    opens userBooking to javafx.fxml;
    opens com.example.bookingsystem to javafx.fxml;
    exports com.example.bookingsystem;
}