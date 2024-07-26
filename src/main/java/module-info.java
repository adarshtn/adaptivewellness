module com.example.adaptivewellness {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.adaptivewellness to javafx.fxml;
    exports com.example.adaptivewellness;
}