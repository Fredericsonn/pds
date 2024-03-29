package uiass.eia.gisiba;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SplitMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Options");

        MenuItem menuItem1 = new MenuItem("Option 5");
        menuItem1.setOnAction(event -> {
            try {
                switchToSecondary();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        MenuItem menuItem2 = new MenuItem("Option 2");
        menuItem2.setOnAction(event -> handleOption2());

        splitMenuButton.getItems().addAll(menuItem1, menuItem2);

        VBox root = new VBox(splitMenuButton);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SplitMenuButton Example");
        primaryStage.show();
    }

    private void handleOption1() {
        // Handle action for Option 1
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Option 1");
        alert.setContentText("Option 1 selected!");
        alert.showAndWait();
    }

    private void handleOption2() {
        // Handle action for Option 2
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Option 2");
        alert.setContentText("Option 2 selected!");
        alert.showAndWait();
    }
    private void switchToSecondary() throws IOException {
        App.setRoot("Person_Search_Page");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
