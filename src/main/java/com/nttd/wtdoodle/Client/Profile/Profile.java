package com.nttd.wtdoodle.Client.Profile;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.GameHistory;
import com.nttd.wtdoodle.Client.Models.GameHistoryData;
import com.nttd.wtdoodle.Client.Models.User;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    public GridPane gridPane;
    public Label l_nGamePlayed;
    public Label l_name;
    public Label l_totalScore;
    public Label l_userName;
    public Label l_email;
    User user;
    GameHistory gameHistory;
    CToSBridge cToSBridge;

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
        cToSBridge = CToSBridge.getInstance();
        user = User.getInstance();
        gameHistory = GameHistory.getInstance();
        l_name.setText(user.getName());
        l_userName.setText(user.getUserName());
        l_email.setText(user.getEmail());
        l_nGamePlayed.setText(String.valueOf(user.getGamesPlayed()));
        l_totalScore.setText(String.valueOf(user.getTotalScore()));
        /*
        Get leaderboard data here;
         */

        gridPane.addRow(0,createLabel("S.no"),createLabel("Game Id"),createLabel("Date"),createLabel("Score"),createLabel("Winner"));
      /*
      loop through the leaderboard and add rows
       */
        ArrayList<GameHistoryData> gData = gameHistory.getGameHistories();
        int count = 1;
        for(GameHistoryData g : gData) {
            gridPane.addRow(count,createLabel(count+""),createLabel(g.getId()+""),createLabel(g.getDate().toString()),createLabel(100+""),createLabel(g.getWinner()));
            count++;
        }
    }
    private Label createLabel(String s){
        return new Label(s);
    }

}
