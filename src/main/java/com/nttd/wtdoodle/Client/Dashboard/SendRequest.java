package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.RequestClass;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.Client.Utility.KeyPressHandler;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class SendRequest extends Application implements Initializable {
    public Button btn_Cancel;
    public Button btn_Confirm;
    public AnchorPane ap_main;
    public Label lb_name;
    CToSBridge cToSBridge;
    User user;
    RequestClass requestClass;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        cToSBridge = CToSBridge.getInstance();
        requestClass = RequestClass.getInstance();
        cToSBridge.setHolder(ap_main);
        lb_name.setText(requestClass.getReceiverName()+" "+requestClass.getReceiverUserName());
     btn_Cancel.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
             Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
             stage.close();
         }
     });
     btn_Confirm.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
             cToSBridge = CToSBridge.getInstance();
             cToSBridge.sendMessageToServer(new Message(Message.TYPE.SEND_REQUEST,user.getUserId(),requestClass.getSenderUserName()+";"+requestClass.getReceiverUserName()));

             Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
             stage.close();
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
