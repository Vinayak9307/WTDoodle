package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.GameObjects.WordGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameServer {

    ServerSocket serverSocket;
    int numPlayers;
    int maxPlayers;
    int numRounds;
    int maxRounds;
    volatile boolean isCurrentWordSelected;
    String currentWord;
    int clockTime = 0;
    int currentDrawer = 0;
    PlayerHandler drawer = null;
    WordGenerator wordGenerator;
    String randomThreeWords;
    static class TimerDemo {
        Timer timer = new Timer();
        GameServer g ;

        TimerDemo(int seconds , GameServer game) {
            g = game;
            timer.schedule(new RemindTask(), seconds * 1000L);
        }

        class RemindTask extends TimerTask {
            public void run() {
                g.startNewGame();
                timer.cancel();
            }
        }
    }

    ArrayList<PlayerHandler> players = new ArrayList<>();

    public ArrayList<PlayerHandler> getPlayers() {
        return players;
    }
    public GameServer(){
        try{
            System.out.println("=====GAME SERVER=====");
            serverSocket = new ServerSocket(1234);
        }catch (IOException e){
            System.out.println("Error occured in GameServer constructor.");
        }
        numPlayers = 0;
        numRounds = 0;
        maxPlayers = 3;
        maxRounds = maxPlayers;
        isCurrentWordSelected = false;
        currentWord = "";
        wordGenerator = new WordGenerator();
    }
    private void acceptConnection(){
        try{
            System.out.println("Waiting for clients.....");
            while(numPlayers < maxPlayers) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                PlayerHandler p = new PlayerHandler(socket , numPlayers , this);
                Thread t = new Thread(p);
                t.start();
                players.add(p);
            }
        }catch (IOException e){
            System.out.println("Error occurred in GameServer acceptConnection.");
        }
        System.out.println("Not Accepting any other connections ...");
        startNewGame();
    }
    private void startNewGame() {
        setDrawer(currentDrawer);
        randomThreeWords = wordGenerator.getThreeRandomWords();
        sendMessageToAll(new Message(Message.type.wordSelection,drawer.getPlayerID(),"Server",randomThreeWords));
        while (!isCurrentWordSelected) {
            Thread.onSpinWait();
        }
        sendMessageToAll(new Message(Message.type.general,"Timer Started ."));
        currentDrawer++;
    }
    public void sendMessageToAll(Message m){
        for(PlayerHandler player : players){
            PlayerHandler.sendMessageToClient(m,player);
        }
    }
    private void setDrawer(int drawerIndex) {
        drawer = players.get(drawerIndex);
        sendMessageToAll(new Message(Message.type.setDrawer,drawerIndex+1));
    }
    public static void main(String[] args){
        GameServer game = new GameServer();
        game.acceptConnection();
    }
    public boolean isCurrentWordSelected() {
        return isCurrentWordSelected;
    }
    public void setCurrentWordSelected(boolean currentWordSelected) {
        isCurrentWordSelected = currentWordSelected;
    }
    public String getCurrentWord() {
        return currentWord;
    }
    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }
}
