package uiass.eia.gisiba;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class Controller {

    @FXML
    private MenuItem personsMenuItem;

    @FXML
    private void switchToPersonsScene() throws IOException {
        // Call the method to switch to the "Person_Search_Page" scene
        App.setRoot("Person_Search_Page");
    }
}
