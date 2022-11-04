package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerHandler implements Runnable {
    Socket player;
//    ObjectInputStream ois;
//    ObjectOutputStream oos;

    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    int playerID;
    boolean guessed;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void incrementScore(int score){
        this.score += score;
    }
    public boolean hasGuessed(){
        return guessed;
    }
    public void setGuessed(boolean guessed){
        this.guessed = guessed;
    }

    int score;

    GameServer game;

    public PlayerHandler(Socket socket , int playerID , GameServer game){
        this.player = socket;
        this.playerID = playerID;
        this.game = game;
        this.score = 0;
        this.guessed = false;
        try {
//            this.ois = new ObjectInputStream(socket.getInputStream());
//            this.oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(new Message(Message.TYPE.SET_ID,playerID,"Set Player ID."));
//            oos.flush();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Message m = new Message(Message.TYPE.SET_ID,playerID,"Set Player ID.");
            bufferedWriter.write(m.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(playerID + " connected to the server .");
    }
    @Override
    public void run() {
        receiveMessageFromClient(this);
    }

    public void receiveMessageFromClient(PlayerHandler me){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(player.isConnected()){
                    try {
//                            Object obj = ois.readObject();
//                            if(obj.getClass() == Message.class){
//                                Message m = (Message) obj;
//                                decodeMessage(m);
//                                if (m.getType() != Message.TYPE.GUESS && m.getType() != Message.TYPE.CLOSE_CONNECTION) {
//                                    for (PlayerHandler client : game.getPlayers()) {
//                                        if (client.equals(me)) continue;
//                                        client.sendMessageToClient(m);
//                                    }
//                                }
//                            }
                        String m = bufferedReader.readLine();
                        String []message = m.split(",");
                        decodeMessage(m);
                        if (Message.TYPE.valueOf(message[0]) != Message.TYPE.GUESS && Message.TYPE.valueOf(message[0]) != Message.TYPE.CLOSE_CONNECTION) {
                            for (PlayerHandler client : game.getPlayers()) {
                                if (client.equals(me)) continue;
                                client.sendMessageToClient(m);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("IOException in rMFC() . Reassigning Streams .");
                        logTime();
                        break;
                    }
                }
            }
        }).start();
    }

    private void decodeMessage(String m) {
        String []message = m.split(",");
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.SET_CURRENT_WORD){
            game.setCurrentWordSelected(true);
            game.setCurrentWord(message[2]);
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.GUESS){
            if(game.getCurrentWord().toLowerCase().equals(message[2].toLowerCase())){
                game.sendMessageToAll(new Message(Message.TYPE.SUCCESSFULLY_GUESSED, Integer.parseInt(message[1]) ,"Player " + Integer.parseInt(message[1]) + " has guessed the word correctly ."));
                setGuessed(true);
                score += game.getIncrementScoreFactorForGuesser();
                game.getDrawer().incrementScore(game.getIncrementScoreFactorForDrawer());
                System.out.println("Player #" + Integer.parseInt(message[1]) + " has guessed the word correctly .");
                System.out.println("Player #" + Integer.parseInt(message[1]) + " : " + score);
                System.out.println("Player #" + game.getDrawer().getPlayerID() + " : " + game.getDrawer().getScore());
            }
            else{
                game.sendMessageToAll(new Message(Message.TYPE.GUESS , Integer.parseInt(message[1]) , message[2]));
            }
        }
        if(Message.TYPE.valueOf(message[0]) == Message.TYPE.CLOSE_CONNECTION){
            logTime();
//            closeEverything(player , oos , ois);
            game.getPlayers().remove(Integer.parseInt(message[1])-1);
            game.sendMessageToAll(new Message(Message.TYPE.GENERAL,Integer.parseInt(message[1]),"Player " + Integer.parseInt(message[1]) + " has left."));
        }
    }
    public void sendMessageToClient(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
//            oos.writeObject(message);
//            oos.flush();
        }catch (IOException e){
            logTime();
            System.out.println("IOException in sMTC(). Reassigning Streams.");
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
    }
    public int getPlayerID() {
        return playerID;
    }

    public void logTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}
