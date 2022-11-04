package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
import com.nttd.wtdoodle.Client.Game.GameObjects.WordGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer {

    /* ------ GAME RELATED -------*/
    int numPlayers;             //No of Players in lobby
    int maxPlayers;             //Maximum number of players who can join the game
    int numRounds;              //Current round number
    int maxRounds;              //Maximum number of round
    int currentDrawer = 0;      //Current Drawer index
    int incrementScoreFactorForGuesser;   //Score increasing factor for correct guess

    public int getIncrementScoreFactorForGuesser() {
        return incrementScoreFactorForGuesser;
    }

    public int getIncrementScoreFactorForDrawer() {
        return incrementScoreFactorForDrawer;
    }

    int incrementScoreFactorForDrawer;  //Score increasing factor for correct guess

    public PlayerHandler getDrawer() {
        return drawer;
    }

    PlayerHandler drawer = null;    //Current Drawer
    String currentWord;         //Current word selected by the drawer
    volatile boolean isCurrentWordSelected;         //Boolean to check if the current word is selected or not.
    WordGenerator wordGenerator;    //Helper object that return three magical words XD
    String randomThreeWords;        //three magical words :p
    int remainingTime;

    /*Method to select new drawer by the given index*/
    private void setDrawer(int drawerIndex) {
        drawer = players.get(drawerIndex);
        sendMessageToAll(new Message(Message.TYPE.SET_DRAWER,(drawerIndex+1),"Naya Drawer"));
    }

    /*Method to start new game*/
    private void startNewGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(numRounds < maxRounds) {
                    setRemainingTime(20);
                    setCurrentWordSelected(false);
                    setCurrentWord("");
                    setDrawer(currentDrawer++);
                    randomThreeWords = wordGenerator.getThreeRandomWords();
                    sendMessageToAll(new Message(Message.TYPE.WORD_SELECTION, drawer.getPlayerID(), randomThreeWords));
                    while (!isCurrentWordSelected) {
                        Thread.onSpinWait();
                    }
                    sendMessageToAll(new Message(Message.TYPE.GENERAL,0,"Timer Started"));
                    while (getRemainingTime() > 0) {
                        sendMessageToAll(new Message(Message.TYPE.UPDATE_TIMER, 0, getRemainingTime() + ""));
                        decrementRemainingTime(1);
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    sendClearScreenMessage();
                    sendScores();
                    numRounds++;
                    currentDrawer%=maxPlayers;
                    System.out.println("Current drawer index .");
                }
            }
        }).start();
    }
    public void sendClearScreenMessage(){
        PenInfo p = new PenInfo(0, 0, 500, true, new PenColor(0,0,0));
        sendMessageToAll(new Message(Message.TYPE.PEN_POSITION, 0, p.toString()));
    }
    public void sendScores(){
        StringBuilder sc = new StringBuilder();
        for(PlayerHandler player : players){
            sc.append(player.getPlayerID()).append(" : ").append(player.getScore()).append("@");
        }
        String score = sc.toString();
        sendMessageToAll(new Message(Message.TYPE.SET_SCORE,0,score));
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
    public void setRemainingTime(int n){
        remainingTime = n;
    }
    public void decrementRemainingTime(int n){ remainingTime -= n ;}

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
            System.out.println("Error occurred in GameServer constructor.");
        }
        numPlayers = 0;
        numRounds = 0;
        maxPlayers = 3;
        maxRounds = maxPlayers*2;
        isCurrentWordSelected = false;
        currentWord = "";
        wordGenerator = new WordGenerator();
        remainingTime = 10;
        incrementScoreFactorForGuesser = 200;
        incrementScoreFactorForDrawer = 50;
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
            player.sendMessageToClient(m.toString());
        }
    }


    /* -------------------------- */


    //Main method//
    public static void main(String[] args){
        GameServer game = new GameServer();
        game.acceptConnection();
    }
}
