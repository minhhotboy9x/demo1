package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Controller implements Initializable{
    @FXML
    Pane myPane;
    @FXML
    Button lockButton;
    @FXML
    Button clueButton;
    //-----------------
    @FXML
    MediaView mediaView;
    private Media media;
    private File file;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask task;
    @FXML
    Button music;
    //-----------------
    PlayerMap myMap;
    EnemyMap enemyMap;
    ArrayList<PlayerShip> playerShips = new ArrayList<PlayerShip>();
    EnemyBot enemyBot;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myMap = new PlayerMap(50, 310, 250, 10, myPane);

        enemyMap = new EnemyMap(50, 50, 250, 10, myPane, enemyBot);

        enemyBot = new EnemyBot(myMap, enemyMap);

        enemyMap.setEnemyBot(enemyBot);

        playerShips = new ArrayList<PlayerShip>();

        playerShips.add(new PlayerShip(400,50,5, myMap.getSquareSize(), 0, myPane, myMap));
        playerShips.add(new PlayerShip(550,50,4, myMap.getSquareSize(), 0, myPane, myMap));
        playerShips.add(new PlayerShip(400,200,3, myMap.getSquareSize(), 0, myPane, myMap));
        playerShips.add(new PlayerShip(500,200,3, myMap.getSquareSize(), 0, myPane, myMap));
        playerShips.add(new PlayerShip(590,200,2, myMap.getSquareSize(), 0, myPane, myMap));

        //enemyShip = new EnemyShip(4, enemyMap.getSquareSize(), 0, myPane, enemyMap);
        file = new File("src/media/Winno - Say Đắm Trong Lần Đầu (SĐTLĐ) (Official Lyric Video).mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.play();
        });
    }

    public void lockShip(ActionEvent event){
        if(enemyBot.playerLeft==0 || enemyBot.enemyLeft==0)
            return;
        int ok=1;
        for (PlayerShip n : playerShips) {
            if (!n.inMap()) {
                ok=0;
                break;
            }
        }

        if(ok==0) {
            return;
        } else {
            playerShips.forEach(n -> n.setLocked(true));
        }

        enemyMap.setLocked(true);
        enemyBot.createEnemyShip(4);
        enemyBot.createEnemyShip(3);
        enemyBot.createEnemyShip(5);
        enemyBot.createEnemyShip(3);
        enemyBot.createEnemyShip(2);
    }

    public void resetGame(ActionEvent event) throws IOException {
        mediaPlayer.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) (((Node)event.getSource()).getScene()).getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeToScene2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("instruction.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) (((Node)event.getSource()).getScene()).getWindow(); //lay stage hien tại
        Scene scene = new Scene(root);
        Scene scene1 = stage.getScene(); //lay scene1 hien tai
        Controller2 controller2 = loader.getController();
        controller2.getScene1(scene1); // send scene1 to controller2
        stage.setScene(scene);
        mediaPlayer.pause();
        scene1.setUserData(mediaPlayer);
        stage.show();
    }
}
