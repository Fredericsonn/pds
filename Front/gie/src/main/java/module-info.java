module uiass.gisiba.eia {
    requires javafx.controls;
    requires javafx.fxml;

    opens uiass.gisiba.eia to javafx.fxml;
    exports uiass.gisiba.eia;
}
