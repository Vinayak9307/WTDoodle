package com.nttd.wtdoodle.Client.Profile;

import com.nttd.wtdoodle.ResourceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    public GridPane gridPane;

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        Get leaderboard data here;
         */

        gridPane.addRow(0,createLabel("S.no"),createLabel("Game Id"),createLabel("Date"),createLabel("Score"),createLabel("Winner"));
      /*
      loop through the leaderboard and add rows
       */
        for(int i=1;i<=10;i++) {
            gridPane.addRow(i,createLabel(i+""),createLabel("5"+i+"3"),createLabel("Date"),createLabel(100-i+""),createLabel("Saket"));

        }
    }
    private Label createLabel(String s){
        Label label=new Label(s);
        return label;
    }


}
