package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.GameMessage;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenColor;
import com.nttd.wtdoodle.Client.Game.GameObjects.PenInfo;
import com.nttd.wtdoodle.Client.Game.GameObjects.WordGenerator;
import com.nttd.wtdoodle.Client.Lobby.HostLobby;
import com.nttd.wtdoodle.SharedObjects.GameSharable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class GameServer implements Runnable{

    /* ------ GAME RELATED -------*/
    int numPlayers;             //No of Players in lobby
    int maxPlayers;             //Maximum number of players who can join the game
    int numRounds;              //Current round number
    int maxRounds;              //Maximum number of round
    boolean isStarted;          //Check if the game is started or not
    int currentDrawer = 0;      //Current Drawer index
    int incrementScoreFactorForGuesser;   //Score increasing factor for correct guess
    String playerNames;
    AnchorPane ap_main;

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
    int guessTime;
    int remainingTime;
    Thread gameThread;

    /*Method to select new drawer by the given index*/
    private void setDrawer(int drawerIndex) {
        drawer = players.get(drawerIndex);
        sendMessageToAll(new GameMessage(GameMessage.TYPE.SET_DRAWER,(drawerIndex+1),drawer.getPlayerName()));
    }

    /*Method to start new game*/
    public void startNewGame() {
        isStarted = true;

        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!everyOneIsReady()){
                    Thread.onSpinWait();
                }
                while(numRounds < maxRounds && !Thread.currentThread().isInterrupted()) {
                    sendClearScreenMessage();
                    setRemainingTime(guessTime);
                    setCurrentWordSelected(false);
                    setCurrentWord("");
                    setDrawer(currentDrawer++);
                    setGuessers();
                    randomThreeWords = wordGenerator.getThreeRandomWords();
                    sendMessageToAll(new GameMessage(GameMessage.TYPE.WORD_SELECTION, drawer.getPlayerID(), randomThreeWords));
                    while (!isCurrentWordSelected) {
                        Thread.onSpinWait();
                    }
                    sendMessageToAll(new GameMessage(GameMessage.TYPE.GENERAL,0,"Timer Started"));
                    sendMessageToAll(new GameMessage(GameMessage.TYPE.SEND_HINT,drawer.playerID,getCurrentWord().length()+""));
                    while (getRemainingTime() > 0 && allHaveNotGuessed()) {
                        sendMessageToAll(new GameMessage(GameMessage.TYPE.UPDATE_TIMER, 0, getRemainingTime() + ""));
                        decrementRemainingTime(1);
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    sendScores();
                    numRounds++;
                    currentDrawer%=maxPlayers;
                }

            }
        });
        gameThread.start();
    }

    private boolean everyOneIsReady() {
        for(PlayerHandler p : players){
            if(!p.isReady()){
                return false;
            }
        }
        return true;
    }

    public void sendClearScreenMessage(){
        PenInfo p = new PenInfo(0, 0, 500, true, new PenColor(0,0,0));
        sendMessageToAll(new GameMessage(GameMessage.TYPE.PEN_POSITION, 0, p.toString()));
    }
    public boolean allHaveNotGuessed(){
        for(PlayerHandler player : players){
            if(player == drawer) continue;
            if(!player.hasGuessed()){
                return true;
            }
        }
        return false;
    }
    public void setGuessers(){
        for(PlayerHandler player : players){
            player.setGuessed(false);
        }
    }
    public void sendScores(){
        int max = 10000;
        StringBuilder sc = new StringBuilder();
        TreeMap<Integer,PlayerHandler> sorted = new TreeMap<Integer, PlayerHandler>();
        for(PlayerHandler player : players){
            sorted.put((max - player.getScore()),player);
        }
        for(Map.Entry<Integer,PlayerHandler> entry : sorted.entrySet()) {
            sc.append(entry.getValue().getPlayerName()).append(" : ").append((max-entry.getKey())).append("@");
        }
        String score = sc.toString();
        sendMessageToAll(new GameMessage(GameMessage.TYPE.SET_SCORE,0,score));
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

    public GameSharable getJoinCode(){
        return new GameSharable(getAlphaNumericString(8),Objects.requireNonNull(getLocalAddress()).toString(),serverSocket.getLocalPort());
    }

    /*GameServer Constructor to create new ServerSocket as well as initialize various game variables*/
    public GameServer(AnchorPane anchorPane , int max , int guessTime,int rounds){
        try{
            System.out.println("=====GAME SERVER=====");
            serverSocket = new ServerSocket(1234);
        }catch (IOException e){
            System.out.println("Error occurred in GameServer constructor.");
        }
        ap_main = anchorPane;
        numPlayers = 0;
        numRounds = 0;
        maxPlayers = max;
        playerNames = "";
        maxRounds = maxPlayers*rounds;
        isCurrentWordSelected = false;
        currentWord = "";
        wordGenerator = new WordGenerator();
        this.guessTime = guessTime;
        incrementScoreFactorForGuesser = 200;
        incrementScoreFactorForDrawer = 50;
        isStarted = false;
    }



    /*This method accepts socket connections and creates new playerHandlers*/
    public void acceptConnection(){
        try{
            System.out.println("Waiting for clients.....");
            while(numPlayers < maxPlayers && !isStarted) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                PlayerHandler p = new PlayerHandler(socket , numPlayers , this);
                Thread t = new Thread(p);
                t.start();
                players.add(p);
                playerNames += p.getPlayerName();
                HostLobby.updatePlayerLabel(playerNames,ap_main);
                sendMessageToAll(new GameMessage(GameMessage.TYPE.NEW_PLAYER_JOINED,0,playerNames));
            }
        }catch (IOException e){
            System.out.println("Error occurred in GameServer acceptConnection.");
        }
        System.out.println("Not Accepting any other connections ...");
    }

    private static InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*This method sends a message to all playerHandlers*/
    public void sendMessageToAll(GameMessage m){
        for(PlayerHandler player : players){
            player.sendMessageToClient(m.toString());
        }
    }
    private String getAlphaNumericString(int length)
    {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index= (int)(AlphaNumericString.length()* Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted())
            acceptConnection();
    }

    public void close() {
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(players != null){
            for(PlayerHandler playerHandler : players){
                playerHandler.closeEverything();
            }
            players.clear();
        }
        if(gameThread != null){
            if(gameThread.isAlive()){
                gameThread.interrupt();
            }
        }
        System.out.println("Game Server Closed.");
    }

    public int getNumberOfPlayers() {
        return players.size();
    }


    /* -------------------------- */


    //Main method//
//    public static void main(String[] args){
//        GameServer game = new GameServer();
//        game.acceptConnection();
//    }
}
