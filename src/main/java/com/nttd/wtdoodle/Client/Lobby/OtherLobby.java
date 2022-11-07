package com.nttd.wtdoodle.Client.Lobby;

import com.nttd.wtdoodle.Client.Game.Player.Player;
import com.nttd.wtdoodle.Client.Game.Player.PtoSBridge;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class OtherLobby extends Application implements Initializable {
    public Label lb_players;
    public TextField tf_hostIp;
    public TextField tf_portNo;
    public Button bt_join;
    public Label lb_update;
    public AnchorPane ap_main;
    static PtoSBridge ptoSBridge;

    public static void startGame(AnchorPane ap_main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Player.fxml"));
                Player.setPtoSBridge(ptoSBridge);
                Stage gameScreen = (Stage)ap_main.getScene().getWindow();
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 800, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gameScreen.setScene(scene);
                gameScreen.show();
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("OtherLobby.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Lobby");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bt_join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String hostIp = tf_hostIp.getText();
                String portNo = tf_portNo.getText();
                if(!hostIp.isEmpty() && !portNo.isEmpty()){
                    try {
                        ptoSBridge = new PtoSBridge(new Socket(hostIp,Integer.parseInt(portNo)),false,ap_main);
                        ptoSBridge.receiveMessagesFromServer();
                    } catch (IOException e) {
                        lb_update.setText("Enter a valid IpAddress and Port No.");
                    }
                    ap_main.getChildren().removeAll(ap_main.lookup("#tf_hostIp"));
                    ap_main.getChildren().removeAll(ap_main.lookup("#tf_portNo"));
                    ap_main.getChildren().removeAll(ap_main.lookup("#bt_join"));
                }
                else{
                    lb_update.setText("Enter a valid IpAddress and Port No.");
                }

            }

        });
    }

    public static void updatePlayerLabel(String players , AnchorPane anchorPane){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label lb = (Label) anchorPane.lookup("#lb_players");
                lb.setText(players);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

}
