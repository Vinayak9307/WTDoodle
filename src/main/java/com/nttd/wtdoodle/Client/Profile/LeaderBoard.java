package com.nttd.wtdoodle.Client.Profile;

import com.nttd.wtdoodle.Client.Game.Player.Player;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoard implements Initializable {

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

        gridPane.addRow(0,createLabel("Rank"),createLabel("Username"),createLabel("Score"),createLabel("Date"));
      /*
      loop through the leaderboard and add rows
       */
        for(int i=1;i<=10;i++) {
            gridPane.addRow(i, createLabel(i+""), createLabel("ullu"), createLabel((100-i)+""), createLabel("Date"));
        }

    }
    private Label createLabel(String s){
        Label label=new Label(s);
        return label;
    }
}
