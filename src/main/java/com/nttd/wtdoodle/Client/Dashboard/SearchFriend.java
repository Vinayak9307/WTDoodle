package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchFriend extends Application implements Initializable {
    public Button btn_search;

    @Override
    public void start(Stage stage) throws Exception {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                function to check if the username exists
                 */

                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("SendRequest.fxml"));
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
}
