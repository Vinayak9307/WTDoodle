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

    /* ------ GAME RELATED -------*/
    int numPlayers;             //No of Players in lobby
    int maxPlayers;             //Maximum number of players who can join the game
    int numRounds;              //Current round number
    int maxRounds;              //Maximum number of round
    int currentDrawer = 0;      //Current Drawer index
    PlayerHandler drawer = null;    //Current Drawer
    String currentWord;         //Current word selected by the drawer
    volatile boolean isCurrentWordSelected;         //Boolean to check if the current word is selected or not.
    WordGenerator wordGenerator;    //Helper object that return three magical words XD
    String randomThreeWords;        //three magical words :p


    int remainingTime;
    static class TimerDemo {
        Timer timer = new Timer();
        GameServer g ;

        TimerDemo(int seconds , GameServer game) {
            g = game;
            timer.schedule(new RemindTask(), seconds * 1000L);
        }

        class RemindTask extends TimerTask {
            public void run() {
                timer.cancel();
            }
        }
    }
    //Timer class for round timer



    /*Method to select new drawer by the given index*/
    private void setDrawer(int drawerIndex) {
        drawer = players.get(drawerIndex);
        sendMessageToAll(new Message(Message.type.setDrawer,drawerIndex+1));
    }



    /*Method to start new game*/
    private void startNewGame() {
        setDrawer(currentDrawer);
        randomThreeWords = wordGenerator.getThreeRandomWords();
        sendMessageToAll(new Message(Message.type.wordSelection,drawer.getPlayerID(),"Server",randomThreeWords));
        while (!isCurrentWordSelected) {
            Thread.onSpinWait();
        }
        sendMessageToAll(new Message(Message.type.general,"Timer Started ."));
        sendMessageToAll(new Message(Message.type.updateTimer,getRemainingTime()+""));
    }



    /*GETTER AND SETTER FOR isCurrentWordSelected*/
    public boolean isCurrentWordSelected() {
        return isCurrentWordSelected;
    }
    public void setCurrentWordSelected(boolean currentWordSelected) {
        isCurrentWordSelected = currentWordSelected;
    }



    /*GETTER AND SETTER FOR currentWord*/
    public String getCurrentWord() {
        return currentWord;
    }
    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }



    public int getRemainingTime() {
        return remainingTime;
    }

    /*----------------------------*/



    /* ------SERVER RELATED------ */
    ServerSocket serverSocket;      //ServerSocket for GameServer
    ArrayList<PlayerHandler> players = new ArrayList<>();   //List that contains the PlayerHandler
    public ArrayList<PlayerHandler> getPlayers() {
        return players;
    }


    /*GameServer Constructor to create new ServerSocket as well as initialize various game variables*/
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
        remainingTime = 120;
    }



    /*This method accepts socket connections and creates new playerHandlers*/
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



    /*This method sends a message to all playerHandlers*/
    public void sendMessageToAll(Message m){
        for(PlayerHandler player : players){
            PlayerHandler.sendMessageToClient(m,player);
        }
    }


    /* -------------------------- */


    //Main method//
    public static void main(String[] args){
        GameServer game = new GameServer();
        game.acceptConnection();
    }
}
