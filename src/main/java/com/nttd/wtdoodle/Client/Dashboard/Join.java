package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.Client.Game.Player.PtoSBridge;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.nttd.wtdoodle.Client.Dashboard.Dashboard;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Join extends Application implements Initializable {
    public TextField tf_gameCode;
    public Button btn_join;
    public Button btn_cancel;

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
              String str_gameCode=tf_gameCode.getText();
              /*
              query Database for gamecode and get ip and port
              set ip and port
               */
                // go to other lobby if the gameCode is found in the Array


            }
        });

        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
    }


}
