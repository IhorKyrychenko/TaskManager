package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/mainScene.fxml"));
            primaryStage.setTitle("Main Menu");
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(660);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("An unexpected situation happened, try to come back later.");
            alert.show();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}