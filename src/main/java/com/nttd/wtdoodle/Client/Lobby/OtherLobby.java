package com.nttd.wtdoodle.Client.Lobby;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Game.Player.Player;
import com.nttd.wtdoodle.Client.Game.Player.PtoSBridge;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.SharedObjects.GameSharable;
import com.nttd.wtdoodle.SharedObjects.Message;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class OtherLobby extends Application implements Initializable {
    public Label lb_players;
    public Button bt_join;
    public AnchorPane ap_main;
    static PtoSBridge ptoSBridge;
    public TextField tf_gameCode;
    public Label lb_status;

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
    public static void joinGame(GameSharable g,Node node){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AnchorPane ap_main = (AnchorPane)node;
                Label lb_status = (Label) ap_main.lookup("#lb_status");
                lb_status.setTextFill(Color.RED);
                if(!g.getIpAddress().isEmpty() && g.getPortNo()!=0){
                    PtoSBridge ptoSBridge = PtoSBridge.getInstance();
                    try {
                        ptoSBridge.startBridge(new Socket(g.getIpAddress(),g.getPortNo()),false,(AnchorPane)node);
                        ptoSBridge.receiveMessagesFromServer();
                    } catch (IOException e) {
                        lb_status.setText("Enter a valid IpAddress and Port No.");
                    }
                    ap_main.getChildren().removeAll(ap_main.lookup("#tf_hostIp"));
                    ap_main.getChildren().removeAll(ap_main.lookup("#tf_portNo"));
                    ap_main.getChildren().removeAll(ap_main.lookup("#bt_join"));
                }
                else{
                    lb_status.setText("Enter a valid IpAddress and Port No.");
                }
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CToSBridge cToSBridge = CToSBridge.getInstance();
        cToSBridge.setHolder(ap_main);
        User user = User.getInstance();
        if(user.isJoinedViaInvite()){
            tf_gameCode.setText(user.getInviteCode());
        }
        bt_join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String gameCode = tf_gameCode.getText();
                if(!gameCode.isEmpty()){
                    User user = User.getInstance();
                    CToSBridge cToSBridge = CToSBridge.getInstance();
                    cToSBridge.sendMessageToServer(new Message(Message.TYPE.SEARCH_GAME,user.getUserId(),gameCode));
                    cToSBridge.setHolder(ap_main);
                }
            }

        });
    }

    public static void updatePlayerLabel(String players , AnchorPane anchorPane){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label lb = (Label) anchorPane.lookup("#lb_players");
                StringBuilder playerUserName = new StringBuilder();
                String[] playersArray = players.split(";");
                for(String s:playersArray){
                    playerUserName.append(s).append("\n");
                }
                lb.setText("JOINED PLAYERS \n" + playerUserName.toString());
            }
        });
    }
    public static void updateStatusLabel(String s , Node node){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AnchorPane anchorPane = (AnchorPane) node;
                Label lb_update = (Label) anchorPane.lookup("#lb_status");
                lb_update.setTextFill(Color.RED);
                lb_update.setText(s);
            }
        });
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
