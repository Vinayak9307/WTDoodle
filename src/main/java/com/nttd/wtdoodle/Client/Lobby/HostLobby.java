package com.nttd.wtdoodle.Client.Lobby;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Game.GameObjects.GameMessage;
import com.nttd.wtdoodle.Client.Game.Player.Player;
import com.nttd.wtdoodle.Client.Game.Player.PtoSBridge;
import com.nttd.wtdoodle.Client.Game.Server.GameServer;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.SharedObjects.GameSharable;
import com.nttd.wtdoodle.SharedObjects.Message;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;
//Host lobby class
public class HostLobby implements Initializable {

    public Label lb_players;
    public Button bt_start;
    public AnchorPane ap_main;
    public Label lb_gameCode;
    public GridPane gp_inviteList;
    public Label lb_update;
    GameSharable joinCode;
    PtoSBridge ptoSBridge;
    GameServer gameServer;
    Thread gameServerThread;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameData gameData = GameData.getInstance();
        gameServer = new GameServer(ap_main,gameData.getMaxPlayers(),gameData.getGuessingTime(),gameData.getNumberOfRounds());   //Initializing new game server.

        joinCode = gameServer.getJoinCode();    //join code is returned which has random gameCode and hostIp and portNo

        System.out.println("Game Ip = " + joinCode.getIpAddress()) ;

        gameServerThread = new Thread(gameServer);  //Creating the game server Thread
        gameServerThread.start();                   //Starting the game server Thread

        //Now since Host is also a player so initializing the PtoSBridge and connecting it to game server.
        String gameCode = joinCode.getGameCode();
        lb_gameCode.setText(gameCode);
        String hostIp = removeLeadingFSlash(joinCode.getIpAddress());
        joinCode.setIpAddress(hostIp);
        int portNo = joinCode.getPortNo();

        //Accessing the cToSBridge instance and adding the joinCode to the main server.
        CToSBridge cToSBridge = CToSBridge.getInstance();
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.ADD_GAME, User.getInstance().getUserId(),joinCode.toString()));


        try {
            //Starting the PtoSBridge with host access.
            ptoSBridge = PtoSBridge.getInstance();
            ptoSBridge.startBridge(new Socket(hostIp,portNo),true,ap_main);
            ptoSBridge.receiveMessagesFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bt_start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (gameServer.getNumberOfPlayers() > 1) {
                    gameServer.sendMessageToAll(new GameMessage(GameMessage.TYPE.START_GAME, 0, ""));
                    FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Player.fxml"));
                    Player.setPtoSBridge(ptoSBridge);
                    cToSBridge.sendMessageToServer(new Message(Message.TYPE.DELETE_GAME, 0, joinCode.getGameCode()));
                    Stage gameScreen = (Stage) ap_main.getScene().getWindow();
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
                else{
                    lb_update.setText("Minimum two players required to start game.");
                }
            }
        });



        User user = User.getInstance();
        int count = 0;
        for(String onlineFriend : user.getOnlineFriends()){
            showInvites(count,onlineFriend);
            count++;
        }
    }

    private void showInvites(int count, String onlineFriend) {
        Label l = new Label(onlineFriend);
        Button btn_invite = new Button();
        btn_invite.setText("INVITE");
        CToSBridge cToSBridge = CToSBridge.getInstance();
        User user = User.getInstance();
        btn_invite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // send server query to add element in friend table and remove the entry from request
                cToSBridge.sendMessageToServer(new Message(Message.TYPE.SEND_GAME_INVITE, user.getUserId(), user.getUserName() + ";" + onlineFriend+";"+joinCode.getGameCode()));
                gp_inviteList.getChildren().removeIf(node -> GridPane.getRowIndex(node) == (count));
            }
        });
        gp_inviteList.addRow(count,l,btn_invite);
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


    public void stop(){
        exit(0);
    }

    public void goToDashboard(ActionEvent event) {
        gameServer.close();
        gameServerThread.interrupt();
        CToSBridge cToSBridge = CToSBridge.getInstance();
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.DELETE_GAME, User.getInstance().getUserId(),joinCode.getGameCode()));
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
