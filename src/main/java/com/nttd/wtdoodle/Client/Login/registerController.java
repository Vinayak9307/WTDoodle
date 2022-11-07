package com.nttd.wtdoodle.Client.Login;


import com.nttd.wtdoodle.Server.databaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class registerController {

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

    //setting up what close button will do
    public void closeButtonOnAction(ActionEvent event){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
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
                //confirmPasswordLabel.setText("Password matched--");
                //establishing connection
                databaseConnection connectNow = new databaseConnection();
                Connection connectDB = connectNow.getConnection();
                //to check whether a user already exist with this username and password
                String verifyUser = "SELECT count(1) FROM user_account WHERE username = '" + usernameTextfield.getText() + "'";
                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet querryResult = statement.executeQuery(verifyUser);

                    while (querryResult.next()) {
                        if (querryResult.getInt(1) == 1) {
                            registrationMessageLabel.setText("User already exists with this username");
                        } else {
                            String name = nameTextfield.getText();
                            String username = usernameTextfield.getText();
                            String email = emailTextfield.getText();
                            String password = passwordTextfield.getText();
                            int count=0;
                                for(int i=0;i<email.length();i++)
                                {
                                    if(email.charAt(i)=='@')
                                        count++;
                                }
                                if(count==1) {
                                    //inserting values
                                    String insertFields = "INSERT INTO user_account(name , username , password , email) VALUES ('";
                                    String insertValues = name + "','" + username + "','" + password + "','" + email + "')";
                                    String insertTORegister = insertFields + insertValues;

                                    Statement statement1 = connectDB.createStatement();
                                    statement1.executeUpdate(insertTORegister);
                                    registrationMessageLabel.setText("User registered successfully!!");
                                    confirmPasswordLabel.setText("");
                                }
                                else{
                                    registrationMessageLabel.setText("enter valid e-mail");

                                }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                }
            } else {
                confirmPasswordLabel.setText("Password did not match");
            }
        } else {
            registrationMessageLabel.setText("Password length should be greator than 4");

        }

    }
}