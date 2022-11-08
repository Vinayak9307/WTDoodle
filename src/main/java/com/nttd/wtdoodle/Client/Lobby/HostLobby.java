package com.nttd.wtdoodle.Client.Lobby;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.Player.Player;
import com.nttd.wtdoodle.Client.Game.Player.PtoSBridge;
import com.nttd.wtdoodle.Client.Game.Server.GameServer;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class HostLobby extends Application implements Initializable {

    public Label lb_hostIp;
    public Label lb_portNo;
    public Label lb_players;
    public AnchorPane ap_main;
    public Button bt_start;
    String[] joinCode;
    PtoSBridge ptoSBridge;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("HostLobby.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("HostLobby");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameServer gameServer = new GameServer(ap_main);
        joinCode = gameServer.getJoinCode().split(",");
        Thread gameServerThread = new Thread(gameServer);
        gameServerThread.start();
        String hostIp = removeLeadingFSlash(joinCode[0]);
        lb_hostIp.setText(hostIp);
        String portNo = joinCode[1];
        lb_portNo.setText(portNo);

        bt_start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });

        try {
            ptoSBridge = new PtoSBridge(new Socket(hostIp,Integer.parseInt(portNo)),true,ap_main);
            ptoSBridge.receiveMessagesFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bt_start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameServer.sendMessageToAll(new Message(Message.TYPE.START_GAME,0,""));
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Player.fxml"));
                Player.setPtoSBridge(ptoSBridge);
                Stage gameScreen = (Stage)ap_main.getScene().getWindow();
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 800, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gameScreen.setTitle("GameScreen");
                gameScreen.setScene(scene);
                gameScreen.show();
                gameServer.startNewGame();
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

    String removeLeadingFSlash(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && sb.charAt(0) == '/') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch();
    }

    public void stop(){
        exit(0);
    }

    public void goToDashboard(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Dashboard.fxml"));
        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 950, 570);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setScene(scene);
        stage.show();
    }
}
