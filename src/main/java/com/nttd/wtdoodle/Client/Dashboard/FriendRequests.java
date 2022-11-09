package com.nttd.wtdoodle.Client.Dashboard;

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
import java.util.ResourceBundle;

public class FriendRequests extends Application implements Initializable {

    public GridPane gridPane;
    public Button btn_close;


    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        String str[]={"saket","prashant","vinayak"};
        for(int i=0;i< str.length;i++){
            showRequest(i,str[0]);
        }
    }

    public void showRequest(int i ,String s){
        Label label = new Label(s);
        Button accept = new Button();
        accept.setText("Accept");
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // send server query to add element in friend table and remove the entry from request


            }
        });

        Button reject =new Button("Reject");
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // only remove the data from request table
            }
        });

        gridPane.addRow(i+1,label,accept,reject);
    }
}


