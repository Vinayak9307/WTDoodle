package com.nttd.wtdoodle.Client.Lobby;

import com.nttd.wtdoodle.Client.Dashboard.Dashboard;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.naming.InitialContext;
import java.net.URL;
import java.util.ResourceBundle;

public class GameCreator implements Initializable {
    public TextField tf_maxPlayers;
    public TextField tf_guessingTime;
    public Button btn_createGame;
    public TextField tf_numRounds;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameData gameData = GameData.getInstance();
        tf_maxPlayers.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_maxPlayers.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        tf_guessingTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_guessingTime.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        tf_numRounds.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_numRounds.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        btn_createGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_maxPlayers.getText().isEmpty() && !tf_guessingTime.getText().isEmpty() && !tf_numRounds.getText().isEmpty()) {
                    int maxPlayers = Integer.parseInt(tf_maxPlayers.getText());
                    int guessingTime = Integer.parseInt(tf_guessingTime.getText());
                    int numRound = Integer.parseInt(tf_numRounds.getText());
                    if (maxPlayers > 1){
                        gameData.setMaxPlayers(maxPlayers);
                        if(guessingTime > 20) {
                            gameData.setGuessingTime(guessingTime);
                            if(numRound > 0){
                                gameData.setNumberOfRounds(numRound);
                                Dashboard.goToHostLobby();
                                Stage stage = (Stage) btn_createGame.getScene().getWindow();
                                stage.close();
                            }
                            else {
                                System.out.println("numROunds badhao");
                            }
                        }
                        else{
                            System.out.println("kuch");
                        }

                    }
                }
            }
        });
    }
}
