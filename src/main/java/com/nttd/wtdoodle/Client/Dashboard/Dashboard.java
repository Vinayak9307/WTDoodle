package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.*;
import com.nttd.wtdoodle.Client.Utility.KeyPressHandler;
import com.nttd.wtdoodle.ResourceLocator;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    public ScrollPane friendList;
    public GridPane gridPane;
    public Button bt_search;
    public Button btn_FriendRequest;
    public Button btn_logout;
    public AnchorPane ap_main;
    public GridPane gp_inviteList;
    CToSBridge cToSBridge;
    User user;

    public static void updateFriendList(String[] friends, Node node) {
        AnchorPane ap_main = (AnchorPane)node;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                User user = User.getInstance();
                user.getFriends().clear();
                GridPane gridPane = (GridPane) ap_main.lookup("#gridPane");
                int count = 1;
                for(String friend : friends){
                    String[] friendData = friend.split(" ");
                    user.getFriends().add(friendData[0]);
                    gridPane.addRow(count,isOnline(friendData[1],friendData[0]),createLabel(friendData[0]));
                    count++;
                }
            }
        });
    }

    public static void goToHostLobby() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                CToSBridge cToSBridge = CToSBridge.getInstance();
                AnchorPane anchorPane = (AnchorPane) cToSBridge.getHolder();
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("HostLobby.fxml"));
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.setScene(scene);
                stage.show();
            }
        });
    }

    public void hostButtonOnAction(ActionEvent event) {
        // System.out.println("host button has been clicked");
        if (!KeyPressHandler.getInstance().isHostButtonClicked())
        {
            KeyPressHandler.getInstance().setHostButtonClicked(true);
            FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("GameCreator.fxml"));
            Stage stage = new Stage();

            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.setScene(scene);
            stage.show();
    }

    }

    public void joinButtonOnAction(ActionEvent event){
        //System.out.println("join button has been clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("OtherLobby.fxml"));
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
    public void profileImageClicked(MouseEvent event){
        //System.out.println("Profile Image has been clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("ProfileView.fxml"));
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
    public void leaderboardImageClicked(MouseEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("LeaderBoard.fxml"));
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

    void addInviteData(){
        int count = 0;
        for(Pair<String ,String> invitePair : user.getInvites()){
            showInvite(count++,invitePair.getKey(),invitePair.getValue());
        }
    }

    private void showInvite(int i, String key, String value) {
        Label friendName = new Label(key);
        Button accept = new Button();
        accept.setText("Y");
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                user.setJoinedViaInvite(true);
                user.setInviteCode(value);
                user.getInvites().removeIf(Pair -> Pair.getKey().equals(key));
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("OtherLobby.fxml"));
                Stage stage=(Stage)(gp_inviteList).getScene().getWindow();

                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 950, 570);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.setScene(scene);
                stage.show();
            }
        });
        Button reject = new Button();
        reject.setText("X");
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gp_inviteList.getChildren().removeIf(node -> GridPane.getRowIndex(node) == i);
            }
        });
        gp_inviteList.addRow(i,friendName,accept,reject);
    }

    public static void refresh(Node node){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Dashboard.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 950, 570);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage dashboardStage = (Stage)node.getScene().getWindow();
                dashboardStage.setScene(scene);
                dashboardStage.show();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        RequestClass.getInstance().setSenderUserName(user.getUserName());
        cToSBridge = CToSBridge.getInstance();
        cToSBridge.setHolder(ap_main);
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.REQUEST_FRIEND_LIST, user.getUserId(), user.getUserName()));
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.REQUEST_FRIEND_REQUESTS, user.getUserId(), user.getUserName()));
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.REQUEST_USER_GAME_HISTORY, user.getUserId(), user.getUserName()));
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.REQUEST_LEADERBOARD, user.getUserId(), user.getUserName()));
        cToSBridge.sendMessageToServer(new Message(Message.TYPE.REQUEST_USER_INFO,user.getUserId(), user.getUserName()));
        addInviteData();
        /*
        bring friend list and add in scroll pane  , make necessary changes in the dummy structure
         */
        gridPane.add(createLabel("Friends"),1,0);
        bt_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!KeyPressHandler.getInstance().isSearchButtonClicked()) {
                    KeyPressHandler.getInstance().setSearchButtonClicked(true);
                    FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("searchFriend.fxml"));
                    Stage stage = new Stage();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            KeyPressHandler.getInstance().setSearchButtonClicked(false);
                        }
                    });
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });

        btn_FriendRequest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!KeyPressHandler.getInstance().isFriendRequestButtonClicked()) {
                    KeyPressHandler.getInstance().setFriendRequestButtonClicked(true);
                    FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("FriendRequests.fxml"));
                    Stage stage = new Stage();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            KeyPressHandler.getInstance().setFriendRequestButtonClicked(false);
                        }
                    });
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cToSBridge.sendMessageToServer(new Message(Message.TYPE.LOG_OUT,user.getUserId(),user.getUserName()));
                User.getInstance().clear();
                LeaderBoardModel.getInstance().getGlobalLeaderBoardData().clear();
                GameHistory.getInstance().getGameHistories().clear();
                FriendRequest.getInstance().getRequestData().clear();
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Login.fxml"));
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.setScene(scene);
                stage.show();
            }
        });
    }
    private static Label createLabel(String s){
        Label label=new Label(s);
        return label;
    }
    private static Label isOnline(String b,String friendName){
        Circle circle = new Circle();
        circle.setRadius(5);
       User user = User.getInstance();
        if(b.equals("1")){
           circle.setFill(Color.GREEN);
           user.getOnlineFriends().add(friendName);
        }
        else{
            circle.setFill(Color.RED);
            user.getOnlineFriends().remove(friendName);
        }
        Label label =new Label();
        label.setGraphic(circle);
        return label;
    }
}
