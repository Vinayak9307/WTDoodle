package com.nttd.wtdoodle.Client.Login;

import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.Server.databaseConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class loginController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

        @FXML
        private Label welcomeText;
        @FXML
        private Label loginMessageLabel;
        @FXML
        private TextField username;
        @FXML
        private PasswordField password;

        //setting what login button will do when clicked
        public void loginButtonOnAction(ActionEvent event) {
            //validating if username and password provided
            if(username.getText().isBlank()==false && password.getText().isBlank()==false){
                validateLogin();
            }else{
                loginMessageLabel.setText("please enter username and password");
            }
        }

        //setting what register button will do
        public void registerButtonOnAction(ActionEvent event) {
            registrationForm();
        }
        //method to open registration form
        public void registrationForm(){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("register.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 550);
                Stage registerStage = new Stage();
                registerStage.initStyle(StageStyle.DECORATED);
                registerStage.setResizable(false);
                registerStage.setScene(scene);
                registerStage.show();
            }
            catch(Exception e){
                e.getCause();
                e.printStackTrace();
            }

        }
        //for opening dashboard
        public void dashboard(){
            try {

                    FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Dashboard.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 950, 570);
                    Stage dashboardstage = (Stage)loginMessageLabel.getScene().getWindow();
                    dashboardstage.setScene(scene);
                    dashboardstage.show();

            }
            catch (Exception e){
                e.getCause();
                e.printStackTrace();
            }

        }
        //validating login credential
        public void validateLogin(){
            databaseConnection connectNow =new databaseConnection();
            Connection connectDB=connectNow.getConnection();

            //querry for login verification
            String verifyLogin ="SELECT count(1) FROM user_account WHERE username = '" + username.getText() + "' And password = '" + password.getText() + "'";

            try{
                Statement statement =connectDB.createStatement();
                ResultSet querryResult =statement.executeQuery(verifyLogin);

                //System.out.println(querryResult.);
                while(querryResult.next())
                {
                    if(querryResult.getInt(1) == 1){
                        dashboard();
                    }
                    else{
                        loginMessageLabel.setText("invalid credentials");

                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
        @FXML
        private ImageView loginImgImageView;

        @FXML
        protected void onHelloButtonClick() {
            welcomeText.setText("Welcome to JavaFX Application!");
        }

    public static void main(String[] args) {launch();}
}
