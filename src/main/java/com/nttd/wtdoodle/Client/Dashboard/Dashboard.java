package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Dashboard extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 570);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public void hostButtonOnAction(ActionEvent event){
       // System.out.println("host button has been clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("HostLobby.fxml"));
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

    public void joinButtonOnAction(ActionEvent event){
        //System.out.println("join button has been clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("OtherLobby.fxml"));
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
    public void profileImageClicked(MouseEvent event){
        //System.out.println("Profile Image has been clicked");
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("ProfileView.fxml"));
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
    public void leaderboardImageClicked(MouseEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("LeaderBoard.fxml"));
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

    public static void main(String[] args) {
        launch();
    }
}
