package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.Score;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScoreBoard extends Application implements Initializable {
    public Button btn_goToDashboard;
    public GridPane gridPane;

    @Override
    public void start(Stage stage) throws Exception {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     btn_goToDashboard.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
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
     });

     /*populate the grid with score of player in the game*/
        int i=0;
        gridPane.addRow(i,createLabel("Rank"),createLabel("Username"),createLabel("Score"));
        Score score = Score.getInstance();
        score.sortScores();
        for(Pair<String , Integer> sc : score.getScores()){
            i++;
            gridPane.addRow(i,createLabel(i+""),createLabel(sc.getKey()) , createLabel(String.valueOf(sc.getValue())));
        }
    }
    private Label createLabel(String s){
        Label label=new Label(s);
        label.setFont(Font.font("Verdana",15));
        return label;
    }
}
