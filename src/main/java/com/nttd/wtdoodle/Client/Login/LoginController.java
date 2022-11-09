package com.nttd.wtdoodle.Client.Login;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private BorderPane bp_main;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private CToSBridge cToSBridge;
    User user;
    public static void goToDashboard(Node node) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Dashboard.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 950, 570);
                    Stage dashboardStage = (Stage)node.getScene().getWindow();
                    dashboardStage.setScene(scene);
                    dashboardStage.show();
                }
                catch (Exception e){
                    e.getCause();
                    e.printStackTrace();
                }
            }
        });
    }

    public static void addLabel(Node holder) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label l = (Label)holder;
                l.setText("UserName or Password doesn't match.");
            }
        });
    }
        //setting what login button will do when clicked
    public void loginButtonOnAction(ActionEvent event) {
        //validating if username and password provided
        if(!username.getText().isBlank() && !password.getText().isBlank()){
            validateLogin();
        }else{
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    //setting what register button will do
    public void registerButtonOnAction(ActionEvent event) {
        registrationForm();
    }
    //method to open registration form
    public void registrationForm(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 550);
            Stage registerStage = (Stage)loginMessageLabel.getScene().getWindow();
            registerStage.setScene(scene);
            registerStage.show();
        }
        catch(Exception e){
            e.getCause();
            e.printStackTrace();
        }
    }
    //validating login credential
    public void validateLogin(){
        sendMessageToServer(new Message(Message.TYPE.LOGIN,0,username.getText()+","+password.getText()));
        user.setUserName(username.getText());
    }

    private void sendMessageToServer(Message message) {
        cToSBridge.sendMessageToServer(message);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cToSBridge = CToSBridge.getInstance();
        cToSBridge.setHolder(loginMessageLabel);
        user = User.getInstance();
    }
}
