package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.GameMessage;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerHandler implements Runnable {
    int playerID;
    String playerName;
    boolean guessed;
    int score;
    boolean ready = false;

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    Socket player;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

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
    public String getPlayerName() {
        return playerName;
    }


    GameServer game;

    public PlayerHandler(Socket socket , int playerID , GameServer game){
        this.player = socket;
        this.playerID = playerID;
        this.game = game;
        this.score = 0;
        this.guessed = false;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            GameMessage m = new GameMessage(GameMessage.TYPE.SET_ID,playerID,"Set Player ID.");
            bufferedWriter.write(m.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String[] message = bufferedReader.readLine().split(",");
            if(GameMessage.TYPE.valueOf(message[0]) == GameMessage.TYPE.SET_NAME) {
                this.playerName = message[2];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(playerName + " connected to the server .");
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
                        String m = bufferedReader.readLine();
                        String []message = m.split(",");
                        decodeMessage(m);
                        if (GameMessage.TYPE.valueOf(message[0]) != GameMessage.TYPE.GUESS && GameMessage.TYPE.valueOf(message[0]) != GameMessage.TYPE.CLOSE_CONNECTION) {
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
        if(GameMessage.TYPE.valueOf(message[0]) == GameMessage.TYPE.SET_CURRENT_WORD){
            game.setCurrentWordSelected(true);
            game.setCurrentWord(message[2]);
        }
        if(GameMessage.TYPE.valueOf(message[0]) == GameMessage.TYPE.GUESS){
            if(game.getCurrentWord().toLowerCase().equals(message[2].toLowerCase())){
                game.sendMessageToAll(new GameMessage(GameMessage.TYPE.SUCCESSFULLY_GUESSED, Integer.parseInt(message[1]) ,playerName + " has guessed the word correctly ."));
                setGuessed(true);
                float totalTime = (float)game.getGuessTime();
                float totalPoint =(float) game.getIncrementScoreFactorForGuesser();
                float remainingTime = (float) game.getRemainingTime();
                if(remainingTime > totalTime/4.0){
                    score+=(remainingTime/totalTime)*totalPoint;
                }
                else if(remainingTime == 0){
                    score += 0;
                }
                else{
                    score += (totalPoint/5) + (totalPoint/20) * (remainingTime/totalTime) * 4;
                }
                game.getDrawer().incrementScore((int)(score*0.5));
                System.out.println("Player #" + Integer.parseInt(message[1]) + " " +playerName + " has guessed the word correctly .");
                System.out.println("Player #" + Integer.parseInt(message[1]) + " " +playerName + " : " + score);
                System.out.println("Player #" + game.getDrawer().getPlayerID() + " " + game.getDrawer().getPlayerName() + " : " + game.getDrawer().getScore());
            }
            else{
                game.sendMessageToAll(new GameMessage(GameMessage.TYPE.GUESS , Integer.parseInt(message[1]) , playerName + " has guessed " + message[2]));
            }
        }
        if(GameMessage.TYPE.valueOf(message[0]) == GameMessage.TYPE.CLOSE_CONNECTION){
            logTime();
            game.getPlayers().remove(Integer.parseInt(message[1])-1);
            game.sendMessageToAll(new GameMessage(GameMessage.TYPE.GENERAL,Integer.parseInt(message[1]),playerName + " has left."));
        }
        if(GameMessage.TYPE.valueOf(message[0]) == GameMessage.TYPE.READY){
            setReady(true);
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
    public void closeEverything(){
        try {
            if (player != null) {
                player.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
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
