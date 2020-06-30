package GraphTheory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class welcomePageController implements Initializable {

    @FXML
    ImageView welcomeImage;
    @FXML
    Button startProgram;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = new Image("/imgs/team.png", 1900, 980, false, true);

        welcomeImage.setPreserveRatio(true);
        welcomeImage.setImage(image);
    }

    public void startProgram() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.stage.getScene().setRoot(pane);
    }
}
