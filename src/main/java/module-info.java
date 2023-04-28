module com.example.bookingsystem {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.bookingsystem to javafx.fxml;
    exports com.example.bookingsystem;
}