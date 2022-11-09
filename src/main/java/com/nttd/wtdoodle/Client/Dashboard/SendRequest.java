package com.nttd.wtdoodle.Client.Dashboard;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class SendRequest extends Application implements Initializable {
    public Button btn_Cancel;

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     btn_Cancel.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
             Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
             stage.close();
         }
     });
    }


}
