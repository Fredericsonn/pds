module uiass.eia.gisiba {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires okio;
    requires javafx.base;

    opens uiass.eia.gisiba to javafx.fxml;
    opens uiass.eia.gisiba.controller to javafx.fxml; // Add this line to open the controller package
    exports uiass.eia.gisiba;
}
