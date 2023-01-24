module com.example.toyinterpretergui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.toyinterpretergui to javafx.fxml;
    exports com.example.toyinterpretergui;
}