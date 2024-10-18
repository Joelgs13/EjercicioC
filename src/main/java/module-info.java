module com.example.ejercicioc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejercicioc to javafx.fxml;
    exports com.example.ejercicioc;
}