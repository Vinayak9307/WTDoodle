package com.nttd.wtdoodle.Client.Login;


import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.Server.DatabaseConnection;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public AnchorPane ap_main;
    @FXML
    private Button closeButton;
    @FXML
    private TextField nameTextfield;
    @FXML
    private TextField usernameTextfield;
    @FXML
    private TextField emailTextfield;
    @FXML
    private TextField passwordTextfield;
    @FXML
    private TextField  confirmPasswordTextfield;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private Label confirmPasswordLabel;

    private CToSBridge cToSBridge;

    public static void addLabel(Node holder , String message) {
        Label label = (Label) holder.lookup("#registrationMessageLabel");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setText(message);
            }
        });
    }

    //setting up what close button will do
    public void closeButtonOnAction(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) confirmPasswordLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void clearLabels(){
        registrationMessageLabel.setText("");
        confirmPasswordLabel.setText("");
    }

    //setting up what register button will do
    public void registerButtonOnAction(ActionEvent event) {
        clearLabels();
        if(!nameTextfield.getText().isBlank() && !usernameTextfield.getText().isBlank() && !emailTextfield.getText().isBlank() && !passwordTextfield.getText().isBlank() && !confirmPasswordTextfield.getText().isBlank() )
            registerUser();
        else{
            registrationMessageLabel.setText("Please enter the required field ...");
        }
    }

    //method to register user
    public void registerUser() {
        if (passwordTextfield.getText().length() >= 4) {
            if (passwordTextfield.getText().equals(confirmPasswordTextfield.getText())) {
                String name = nameTextfield.getText();
                String username = usernameTextfield.getText();
                String email = emailTextfield.getText();
                String password = passwordTextfield.getText();

                cToSBridge.sendMessageToServer(new Message(Message.TYPE.REGISTER,0,name+","+username+","+email+","+password));
            } else {
                confirmPasswordLabel.setText("Password did not match");
            }
        } else {
            registrationMessageLabel.setText("Password length should be greater than 4");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cToSBridge = CToSBridge.getInstance();
        cToSBridge.setHolder(ap_main);
    }
}
