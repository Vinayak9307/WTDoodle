package com.nttd.wtdoodle.Client.Game.Server;

import com.nttd.wtdoodle.Client.Game.GameObjects.Message;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements Runnable {
    Socket player;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    int playerID;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void incrementScore(int score){
        this.score += score;
    }

    int score;

    GameServer game;

    public PlayerHandler(Socket socket , int playerID , GameServer game){
        this.player = socket;
        this.playerID = playerID;
        this.game = game;
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(new Message(Message.type.setID,playerID,"Server","",null));
            oos.flush();
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
                            Object obj = ois.readObject();
                            if(obj.getClass() == Message.class){
                                Message m = (Message) obj;
                                decodeMessage(m);
                                if (m.getType() != Message.type.guess && m.getType() != Message.type.closeConnection) {
                                    for (PlayerHandler client : game.getPlayers()) {
                                        if (client.equals(me)) continue;
                                        sendMessageToClient(m, client);
                                    }
                                }
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error sending Message to client");
                        closeEverything(player,oos,ois);
                        break;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private void decodeMessage(Message m) {
        if(m.getType() == Message.type.setCurrentWord){
            game.setCurrentWordSelected(true);
            game.setCurrentWord(m.getMessage());
        }
        if(m.getType() == Message.type.guess){
            if(game.getCurrentWord().equals(m.getMessage())){
                game.sendMessageToAll(new Message(Message.type.successfullyGuessed , m.getID() , "Server","Player " + m.getID() + " has guessed the word correctly .",null));
                score += game.getIncrementScoreFactorForGuesser();
                game.getDrawer().incrementScore(game.getIncrementScoreFactorForDrawer());
                System.out.println("Player #" + m.getID() + " has guessed the word correctly .");
                System.out.println("Player #" + m.getID() + " : " + score);
                System.out.println("Player #" + game.getDrawer().getPlayerID() + " : " + game.getDrawer().getScore());
            }
            else{
                game.sendMessageToAll(new Message(Message.type.guess , m.getID() , "" , m.getMessage() , null));
            }
        }
        if(m.getType() == Message.type.closeConnection){
            closeEverything(player , oos , ois);
            game.getPlayers().remove(m.getID()-1);
            game.sendMessageToAll(new Message(Message.type.general,m.getID(),"Server" , "Player " + m.getID() + " has left.",null));
        }
    }
    public static void sendMessageToClient(Message message , PlayerHandler client) {
        try {
            client.oos.writeObject(message);
            client.oos.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending Message to client");
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
}
