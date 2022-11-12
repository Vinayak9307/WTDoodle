package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.GameMessage;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
import com.nttd.wtdoodle.ResourceLocator;
import com.nttd.wtdoodle.SharedObjects.Message;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

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

    public static void setPtoSBridge(PtoSBridge ptoSBridge) {
        Player.ptoSBridge = ptoSBridge;
    }

    public static void showWordSelectionButtons(String message, AnchorPane ap_main) {
        PtoSBridge ptoSBridge = PtoSBridge.getInstance();
        if(ptoSBridge.isDrawer()) {
            String[] threeWords = message.split(" ");
            int i=3;
            Button btn[] = new Button[i];
            int x=211, y = 0;
            for(int j=0;j<i;j++){
                btn[j] = new Button();
                btn[j].setId("chooseButton"+(j+1));
                btn[j].setLayoutX(x);
                btn[j].setLayoutY(y+=121);
                btn[j].setText(threeWords[j]);

                int finalJ = j;
                btn[j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        PtoSBridge ptosBridge = PtoSBridge.getInstance();
                        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.SET_CURRENT_WORD, ptoSBridge.getPlayerID(),threeWords[finalJ]));
                        for(int k=0;k<i;k++){
                            ap_main.getChildren().remove(ap_main.lookup("#chooseButton"+(k+1)));
                        }
                    }
                });

            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ap_main.getChildren().addAll(btn[0],btn[1],btn[2]);
                }
            });


        }
    }
    public static void showScore(String message , AnchorPane ap_main){
        String[] scores = message.split("@");
        StringBuilder sc = new StringBuilder();
        for(int i = 0 ; i < scores.length; i++){
            sc.append(scores[i]).append("\n");
        }
        Label lb_score = new Label(sc.toString());
        lb_score.setId("lb_score");
        lb_score.setTextFill(Color.BLACK);
        lb_score.setLayoutX(211);
        lb_score.setLayoutY(108);
        System.out.println(sc.toString());
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

    public static void showDrawingButtons(AnchorPane ap_main){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ap_main.getChildren().remove(ap_main.lookup("#rect_hide"));
            }
        });
    }

    public static void hideDrawingButtons(AnchorPane ap_main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Rectangle rect = new Rectangle();
                rect.setId("rect_hide");
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.WHITE);
                rect.setLayoutX(35);
                rect.setLayoutY(440);
                rect.setWidth(420);
                rect.setHeight(30);
                ap_main.getChildren().add(rect);
            }
        });
    }

    public static void removeHint(AnchorPane ap_main){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ap_main.getChildren().remove(ap_main.lookup("#lb_hint"));
            }
        });
    }
    public static void showHint(String s,AnchorPane ap_main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label l = new Label("Length of Word : "+s);
                l.setId("lb_hint");
                l.setLayoutX(70);
                l.setLayoutY(447);
                ap_main.getChildren().add(l);
            }
        });
    }

    public static void goToEndScreen(AnchorPane ap_main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage)ap_main.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceLocator.class.getResource("ScoreBoard.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 800, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.setScene(scene);
                stage.show();
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

//    public static void main(String[] args) {
//        launch();
//    }

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
        g = canvas.getGraphicsContext2D();
        PtoSBridge ptoSBridge = PtoSBridge.getInstance();
        ptoSBridge.setG(g);
        ptoSBridge.setVBox(vb_message);
        ptoSBridge.setAp_main(ap_main);

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
                        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.PEN_POSITION,ptoSBridge.getPlayerID(),p.toString()));
                    } else {
                        g.clearRect(x, y, size, size);
                        PenInfo p = new PenInfo(x, y, size, true, pc);
                        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.PEN_POSITION,ptoSBridge.getPlayerID(),p.toString()));
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
                        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.GUESS,ptoSBridge.getPlayerID(),guess));
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
                    ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.PEN_POSITION, ptoSBridge.getPlayerID(), p.toString()));
                }
            }
        });
        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.READY,ptoSBridge.getPlayerID(),"I am ready."));
    }
    @Override
    public void stop(){
        ptoSBridge.sendMessageToServer(new GameMessage(GameMessage.TYPE.CLOSE_CONNECTION,ptoSBridge.getPlayerID(),"Close Connection."));
        exit(0);
    }
}
