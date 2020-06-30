package GraphTheory;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
        primaryStage.setTitle("Graph Representation");
        primaryStage.setScene(new Scene(root, 1920, 1000));
        primaryStage.show();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("imgs/graph2.png"))));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
