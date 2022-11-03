package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
import com.nttd.wtdoodle.ResourceLocator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Player extends Application implements Initializable {

    public Canvas canvas;
    public ColorPicker colorPicker;
    public CheckBox eraser;
    public TextField brushSize;
    public AnchorPane ap_main;
    public Button bt_guess;
    public TextField tf_guess;
    public VBox vb_message;
    public ScrollPane sp_message;
    public Label l_timer;
    public Button bt_clear;
    GraphicsContext g;
    static PtoSBridge ptoSBridge;

    public static void showWordSelectionButtons(String message, AnchorPane ap_main) {
        String[] threeWords = message.split(",");
        Button bt1 = new Button();
        bt1.setId("chooseButton1");
        bt1.setLayoutX(211);
        bt1.setLayoutY(108);
        bt1.setText(threeWords[0]);
        bt1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ptoSBridge.sendMessageToServer(new Message(Message.type.setCurrentWord, ptoSBridge.getPlayerID(), "Something", threeWords[0],new PenInfo()));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton1"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton2"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton3"));
            }
        });
        Button bt2 = new Button();
        bt2.setId("chooseButton2");
        bt2.setLayoutX(211);
        bt2.setLayoutY(229);
        bt2.setText(threeWords[1]);
        bt2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ptoSBridge.sendMessageToServer(new Message(Message.type.setCurrentWord, ptoSBridge.getPlayerID(), "something", threeWords[1],new PenInfo()));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton1"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton2"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton3"));
            }
        });
        Button bt3 = new Button();
        bt3.setId("chooseButton3");
        bt3.setLayoutX(211);
        bt3.setLayoutY(351);
        bt3.setText(threeWords[2]);
        bt3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ptoSBridge.sendMessageToServer(new Message(Message.type.setCurrentWord, ptoSBridge.getPlayerID(), "something", threeWords[2],new PenInfo()));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton1"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton2"));
                ap_main.getChildren().remove(ap_main.lookup("#chooseButton3"));
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ap_main.getChildren().addAll(bt1, bt2, bt3);
            }
        });
    }
    public static void showScore(String Message , AnchorPane ap_main){
        Label lb_score = new Label(Message);
        lb_score.setId("lb_score");
        lb_score.setBackground(Background.fill(Color.PINK));
        lb_score.setTextFill(Color.BLACK);
        lb_score.setLayoutX(211);
        lb_score.setLayoutY(108);
        System.out.println(Message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ap_main.getChildren().add(lb_score);

            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ap_main.getChildren().remove(ap_main.lookup("#lb_score"));
            }
        });

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

    public static void drawOnCanvas(PenInfo p , GraphicsContext g){
        double x = p.getX();
        double y = p.getY();
        double size = p.getSize();
        boolean erase = p.isErase();
        PenColor color = p.getColor();
        double [] colorA = color.getColor();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!erase){
                    g.setFill(Color.color(colorA[0],colorA[1],colorA[2]));
                    g.fillOval(x , y , size , size);
                }
                else{
                    g.clearRect(x , y , size , size);
                }
            }
        });
    }

    public static void addLabel(String message , VBox vb_message){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233,233,255);" +
                "-fx-background-radius: 15px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vb_message.getChildren().add(hBox);
            }
        });
    }

    public static void setTimer(String remainingTime , Label label){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setText(remainingTime+" sec");
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ptoSBridge = new PtoSBridge(new Socket("localhost" , 1234));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g = canvas.getGraphicsContext2D();
        ptoSBridge.receiveMessagesFromServer(g,ap_main,vb_message);

        vb_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_message.setVvalue((Double)t1);
            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (ptoSBridge.isDrawer()) {
                    double size = Integer.parseInt(brushSize.getText());
                    double x = mouseEvent.getX() - size / 2;
                    double y = mouseEvent.getY() - size / 2;
                    Color c = colorPicker.getValue();
                    PenColor pc = new PenColor(c.getRed(), c.getGreen(), c.getBlue());
                    if (!eraser.isSelected()) {
                        g.setFill(c);
                        g.fillOval(x, y, size, size);
                        PenInfo p = new PenInfo(x, y, size, false, pc);
                        ptoSBridge.sendMessageToServer(new Message(Message.type.penPosition, ptoSBridge.getPlayerID(), "something","something", p));
                    } else {
                        g.clearRect(x, y, size, size);
                        PenInfo p = new PenInfo(x, y, size, true, pc);
                        ptoSBridge.sendMessageToServer(new Message(Message.type.penPosition, ptoSBridge.getPlayerID(), "something","something", p));
                    }
                }
            }
        });
        bt_guess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(ptoSBridge.isGuesser()){
                    String guess = tf_guess.getText();
                    tf_guess.setText("");
                    if(!guess.isEmpty()){
                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                        hBox.setPadding(new Insets(5,5,5,10));

                        Text text = new Text(guess);
                        TextFlow textFlow = new TextFlow(text);

                        textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                                          "-fx-background-color: rgb(15,125,242);" +
                                          "-fx-background-radius: 15px;");
                        textFlow.setPadding(new Insets(5,10,5,10));
                        text.setFill(Color.color(0.934,0.945,0.996));

                        hBox.getChildren().add(textFlow);
                        vb_message.getChildren().add(hBox);
                        ptoSBridge.sendMessageToServer(new Message(Message.type.guess,ptoSBridge.getPlayerID(),"Client" ,guess,new PenInfo()));
                    }
                }
            }
        });
        bt_clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(ptoSBridge.isDrawer()){
                    g.clearRect(0, 0, 500, 500);
                    PenInfo p = new PenInfo(0, 0, 500, true, new PenColor(0,0,0));
                    ptoSBridge.sendMessageToServer(new Message(Message.type.penPosition, ptoSBridge.getPlayerID(), "something","something", p));
                }
            }
        });
    }
    @Override
    public void stop(){
        ptoSBridge.sendMessageToServer(new Message(Message.type.closeConnection,ptoSBridge.getPlayerID(),"something","something",new PenInfo()));
    }
}
