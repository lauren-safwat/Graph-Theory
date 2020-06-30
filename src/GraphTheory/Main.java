package GraphTheory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    public static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
        primaryStage.setTitle("Graph Representation");
        primaryStage.setScene(new Scene(root, 1920, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
