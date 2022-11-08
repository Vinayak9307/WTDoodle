package com.nttd.wtdoodle.Client.Dashboard;

import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard extends Application implements Initializable {
    public ScrollPane friendList;
    public GridPane gridPane;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        bring friend list and add in scroll pane  , make necessary changes in the dummy structure
         */
        gridPane.add(createLabel("Friends"),1,0);
        gridPane.addRow(1,isOnline(true),createLabel("sameer"));
        gridPane.addRow(2,isOnline(false),createLabel("seer"));
    }
    private Label createLabel(String s){
        Label label=new Label(s);
        return label;
    }
    private Label isOnline(boolean b){
        Circle circle = new Circle();
        circle.setRadius(5);
        if(b){
           circle.setFill(Color.GREEN);
        }
        else{
            circle.setFill(Color.RED);
        }
        Label label =new Label();
        label.setGraphic(circle);
        return label;
    }

}
