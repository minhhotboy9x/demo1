package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {

    Scene scene1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void getScene1(Scene scene1) {
        this.scene1 = scene1;
    }
    public void changeToScene1(ActionEvent event) {
        Stage stage = (Stage) (((Node)event.getSource()).getScene()).getWindow();
        stage.setScene(scene1);
        MediaPlayer mediaPlayer = (MediaPlayer) scene1.getUserData();
        mediaPlayer.play();
        stage.show();
    }
}
