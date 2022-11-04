package com.nttd.wtdoodle.Client.Game.Player;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
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

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private boolean drawer;
    private boolean guesser;
    private int playerID;

    public PtoSBridge(Socket socket){
        try {
            this.socket = socket;
//            this.oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.flush();
//            this.ois = new ObjectInputStream(socket.getInputStream());
//            Message m = (Message) ois.readObject();
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
    public void receiveMessagesFromServer(GraphicsContext g,AnchorPane ap_main,VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        String message = bufferedReader.readLine();
//                        Object obj = ois.readObject();
//                        if(obj.getClass() == Message.class) {
//                            Message m = (Message) obj;
//                            decodeMessage(m, g, ap_main, vBox);
//                        }
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
                Player.addLabel("Player " + Integer.parseInt(message[1]) + " is the new drawer .",vBox);
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
                Player.addLabel("Player " + Integer.parseInt(message[1]) + " has guessed " + message[2],vBox);
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
