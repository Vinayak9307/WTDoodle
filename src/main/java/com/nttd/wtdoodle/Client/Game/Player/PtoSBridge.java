package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.System.exit;

public class PtoSBridge {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean drawer;
    private boolean guesser;
    private int playerID;

    public PtoSBridge(Socket socket){
        try {
            this.socket = socket;
            this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.flush();
            this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            Message m = (Message) ois.readObject();
            if(m.getType() == Message.type.setID){
                playerID = m.getID();
                System.out.println("Player #"+playerID);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending Message to client");
            closeEverything(socket,oos,ois);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessageToServer(Message message) {
        try{
            oos.writeObject(message);
            oos.flush();
        }catch (IOException e){
            e.printStackTrace();
            logTime();
            System.out.println("Error sending Message to client");
            closeEverything(socket,oos,ois);
        }
    }
    public void receiveMessagesFromServer(GraphicsContext g,AnchorPane ap_main,VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        Object obj = ois.readObject();
                        if(obj.getClass() == Message.class) {
                            Message m = (Message) obj;
                            System.out.println(m.toString());
                            decodeMessage(m, g, ap_main, vBox);
                        }
                    } catch (IOException e) {
                        logTime();
                        e.printStackTrace();
                        System.out.println("Error sending Message to client");
                        closeEverything(socket,oos,ois);
                        break;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
    public void decodeMessage(Message m, GraphicsContext g , AnchorPane ap_main , VBox vBox) {
        if(m.getType() == Message.type.penPosition){
            Player.drawOnCanvas(m.getPen(),g);
        }
        if(m.getType() == Message.type.setDrawer){
            if(m.getID() == playerID){
                Player.addLabel("You are the new drawer",vBox);
                drawer = true;
                guesser = false;
            }
            else{
                Player.addLabel("Player " + m.getID() + " is the new drawer .",vBox);
                guesser = true;
                drawer = false;
            }
        }
        if(m.getType() == Message.type.setID){
            playerID = m.getID();
        }
        if(m.getType() == Message.type.wordSelection){
            if(m.getID() == playerID){
                Player.showWordSelectionButtons(m.getMessage(),ap_main);
            }
            else{
                Player.addLabel("Drawer is selecting a word .",vBox);
            }
        }
        if(m.getType() == Message.type.general){
            Player.addLabel(m.getMessage(),vBox);
        }
        if(m.getType() == Message.type.successfullyGuessed){
            if(m.getID()==playerID){
                Player.addLabel("Hurray! You guessed it.",vBox);
                guesser = false;
            }
            else{
                Player.addLabel(m.getMessage(),vBox);
            }
        }
        if(m.getType() == Message.type.guess){
            if(m.getID() != playerID){
                Player.addLabel("Player " + m.getID() + " has guessed " + m.getMessage(),vBox);
            }
        }
        if(m.getType() == Message.type.updateTimer){
            Label label = (Label) ap_main.lookup("#l_timer");
            Player.setTimer(m.getMessage(),label);
        }
        if(m.getType() == Message.type.setScore){
            Player.showScore(m.getMessage(),ap_main);
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
