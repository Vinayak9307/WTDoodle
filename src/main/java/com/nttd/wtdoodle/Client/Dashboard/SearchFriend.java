package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.RequestClass;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.Client.Utility.KeyPressHandler;
import com.nttd.wtdoodle.ResourceLocator;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchFriend extends Application implements Initializable {
    public Button btn_search;
    public Label l_search;
    public TextField tf_friend;
    public AnchorPane ap_main;
    CToSBridge cToSBridge;
    User user;
    RequestClass requestClass;

    public static void openFriendRequestDialog(Node node,String data){
        String []data1 = data.split(";");
        RequestClass requestClass = RequestClass.getInstance();
        requestClass.setReceiverName(data1[0]);
        requestClass.setReceiverUserName(data1[1]);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("SendRequest.fxml"));
                Stage stage = (Stage) node.getScene().getWindow();

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

    public static void addLabel(Node holder, String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label l = (Label)((AnchorPane)holder).lookup("#l_search");
                l.setText(s);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cToSBridge = CToSBridge.getInstance();
        cToSBridge.setHolder(ap_main);
        user = User.getInstance();
        requestClass = RequestClass.getInstance();
        btn_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                function to check if the username exists
                 */
                if(!tf_friend.getText().isEmpty()) {
                    if (!tf_friend.getText().equals(user.getUserName())) {
                        cToSBridge.sendMessageToServer(new Message(Message.TYPE.FIND_USER, user.getUserId(), tf_friend.getText()));
                    }
                    else{
                        l_search.setText("You are searching yourSelf.");
                    }
                }else {
                    l_search.setText("Please enter a username.");
                }
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void stop(){
        KeyPressHandler.getInstance().setFriendRequestButtonClicked(false);
    }
}
