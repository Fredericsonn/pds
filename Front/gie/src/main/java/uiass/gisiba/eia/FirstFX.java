package uiass.gisiba.eia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FirstFX extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("gieFront.fxml"));
        Scene scene = new Scene(root,450,450) ;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("HI THERE");
        stage.show();
    }

}
