package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.FriendRequest;
import com.nttd.wtdoodle.Client.Models.FriendRequestData;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FriendRequests  implements Initializable {

    public GridPane gridPane;
    public Button btn_close;

    User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();

        btn_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
        });

        /*
        loop through all friend request a person has , and call showRequest()
         */
        FriendRequest friendRequest = FriendRequest.getInstance();
        ArrayList<FriendRequestData> friendRequestData = friendRequest.getRequestData();
        int count = 1;
        for(FriendRequestData f : friendRequestData){
            showRequest(count++ , f.getSenderName());
        }
    }

    public void showRequest(int i ,String s){
        Label label = new Label(s);
        Button accept = new Button();
        accept.setText("Accept");
        CToSBridge cToSBridge = CToSBridge.getInstance();
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // send server query to add element in friend table and remove the entry from request
                cToSBridge.sendMessageToServer(new Message(Message.TYPE.ADD_FRIEND, user.getUserId(), user.getUserName() + ";" + s));
                gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == (i+1));
            }
        });

        Button reject =new Button("Reject");
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // only remove the data from request table
                cToSBridge.sendMessageToServer(new Message(Message.TYPE.DELETE_REQUEST, user.getUserId(), user.getUserName() + ";" + s));
                gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == (i+1));

            }
        });

        gridPane.addRow(i+1,label,accept,reject);
    }
}


