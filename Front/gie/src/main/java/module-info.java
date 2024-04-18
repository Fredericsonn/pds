module uiass.eia.gisiba {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires okio;
    requires javafx.base;


    opens uiass.eia.gisiba to javafx.fxml;
    exports uiass.eia.gisiba;
}
