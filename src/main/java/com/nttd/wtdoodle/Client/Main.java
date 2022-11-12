package com.nttd.wtdoodle.Client;

import com.nttd.wtdoodle.Client.Connections.CToSBridge;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        CToSBridge cToSBridge = CToSBridge.getInstance();
        cToSBridge.setIpAddress("localhost");
        cToSBridge.setPort(9936);
        cToSBridge.connectSocket();

        Thread t = new Thread(cToSBridge);
        t.start();

        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("What's That Doodle!");
        stage.setResizable(false);
        stage.show();
    }
}
