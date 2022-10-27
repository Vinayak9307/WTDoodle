package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Player extends Application implements Initializable {

    public Canvas canvas;
    public boolean drawer = false;
    public ColorPicker colorPicker;
    public CheckBox eraser;
    public TextField brushSize;
    GraphicsContext g;
    PtoSBridge ptoSBridge;

    public static void decodeMessage(String messageFromServer, GraphicsContext g) {
        String []GameData = messageFromServer.split(" ");
        if(GameData[0].equals("0")){
            drawOnCanvas(GameData,g);
        }
        else{
            System.out.println("Add Label");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("Player.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("GameScreen");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void drawOnCanvas(String []Gamedata , GraphicsContext g){
        double x = Double.parseDouble(Gamedata[1]);
        double y = Double.parseDouble(Gamedata[2]);
        double size = Double.parseDouble(Gamedata[4]);
        int erase = Integer.parseInt(Gamedata[3]);
        if(erase == 0){
            double red = Double.parseDouble(Gamedata[5]);
            double green = Double.parseDouble(Gamedata[6]);
            double blue = Double.parseDouble(Gamedata[7]);

            g.setFill(Color.color(red , green , blue));
            g.fillOval(x , y , size , size);

        }
        else{
            g.clearRect(x , y , size , size);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ptoSBridge = new PtoSBridge(new Socket("localhost" , 1234));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g = canvas.getGraphicsContext2D();

        if(drawer){
            canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double size = Integer.parseInt(brushSize.getText());
                    double x = mouseEvent.getX() - size / 2;
                    double y = mouseEvent.getY() - size / 2;
                    if(!eraser.isSelected()) {
                        g.setFill(colorPicker.getValue());
                        g.fillOval(x, y, size, size);
                        Color c = colorPicker.getValue();
                        String color = c.getRed()+" "+c.getGreen()+" "+c.getBlue();
                        ptoSBridge.sendMessageToServer("0 " + x + " " + y + " 0" + " " + size + " " + color);
                    }else{
                        g.clearRect(x,y,size,size);
                        ptoSBridge.sendMessageToServer("0 " + x + " " + y + " 1" + " " + size);
                    }
                }
            });
        }
        else{
            ptoSBridge.receiveMessagesFromServer(g);
        }
    }
}
