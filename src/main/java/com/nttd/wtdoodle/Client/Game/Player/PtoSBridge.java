package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
import com.nttd.wtdoodle.Client.Lobby.OtherLobby;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.System.exit;

public class PtoSBridge {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private boolean drawer;
    private boolean isHost;
    private boolean guesser;
    private int playerID;
    private String playerName;

    public void setAp_main(AnchorPane ap_main) {
        this.ap_main = ap_main;
    }

    public void setG(GraphicsContext g) {
        this.g = g;
    }

    public void setVBox(VBox vBox) {
        this.vBox = vBox;
    }

    private AnchorPane ap_main;
    private GraphicsContext g;
    private VBox vBox;


    public PtoSBridge(Socket socket,boolean host,AnchorPane anchorPane){
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] message = bufferedReader.readLine().split(",");
            if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SET_ID){
                playerID = Integer.parseInt(message[1]);
                System.out.println("Player #"+playerID);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending Message to client");
            closeEverything(socket,oos,ois);
        }
        Scanner sc = new Scanner(System.in);
        playerName = sc.nextLine();
        this.isHost = host;
        sendMessageToServer(new Message(Message.TYPE.SET_NAME,playerID,playerName));
        ap_main = anchorPane;
    }
    public void sendMessageToServer(Message message) {
        try {
//            oos.writeObject(message);
//            oos.flush();
            bufferedWriter.write(message.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            logTime();
            System.out.println("IOException in sMTC(). Reassigning Streams.");
        }
    }
    public void receiveMessagesFromServer( ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        String message = bufferedReader.readLine();
                        decodeMessage(message,g,ap_main,vBox);
                    } catch (IOException e) {
                        System.out.println("IOException in rMFS() .");
                        e.printStackTrace();
                        logTime();
                        break;
                    }
                }
            }
        }).start();
    }
    public void decodeMessage(String m, GraphicsContext g , AnchorPane ap_main , VBox vBox) {
        String[] message = m.split(",");

        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.NEW_PLAYER_JOINED){
            if(!isHost){
                OtherLobby.updatePlayerLabel(message[2],ap_main);
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.START_GAME){
            if(!isHost){
                OtherLobby.startGame(ap_main);
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.PEN_POSITION){
            PenColor pc = new PenColor(Double.parseDouble(message[6]),Double.parseDouble(message[7]),Double.parseDouble(message[8]));
            PenInfo p = new PenInfo(Double.parseDouble(message[2]),Double.parseDouble(message[3]),Double.parseDouble(message[4]),Boolean.parseBoolean(message[5]),pc);
            Player.drawOnCanvas(p,g);
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SET_DRAWER){
            if(Integer.parseInt(message[1]) == playerID){
                Player.addLabel("You are the new drawer",vBox);
                drawer = true;
                guesser = false;
            }
            else{
                Player.addLabel(message[2] + " is the new drawer .",vBox);
                guesser = true;
                drawer = false;
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SET_ID){
            playerID = Integer.parseInt(message[1]);
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.WORD_SELECTION){
            if(Integer.parseInt(message[1]) == playerID){
                Player.showWordSelectionButtons(message[2],ap_main);
            }
            else{
                Player.addLabel("Drawer is selecting a word .",vBox);
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.GENERAL){
            Player.addLabel(message[2],vBox);
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SUCCESSFULLY_GUESSED){
            if(Integer.parseInt(message[1])==playerID){
                Player.addLabel("Hurray! You guessed it.",vBox);
                guesser = false;
            }
            else{
                Player.addLabel(message[2],vBox);
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.GUESS){
            if(Integer.parseInt(message[1]) != playerID){
                Player.addLabel(message[2],vBox);
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.UPDATE_TIMER){
            Label label = (Label) ap_main.lookup("#l_timer");
            Player.setTimer(message[2],label);
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SET_SCORE){
            Player.showScore(message[2],ap_main);
        }
    }
    public void closeEverything(Socket socket, ObjectOutputStream oos , ObjectInputStream ois){
        try {
            if (socket != null) {
                socket.close();
            }
            if (ois != null) {
                ois.close();
            }
            if (oos != null) {
                oos.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        exit(0);
    }
    public boolean isDrawer() {
        return drawer;
    }
    public int getPlayerID(){
        return playerID;
    }
    public boolean isGuesser(){ return guesser; }

    public void logTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}
