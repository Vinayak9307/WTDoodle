package com.nttd.wtdoodle.Client.Game.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerHandler implements Runnable {
    Socket player;
    final int id;

    static int count = 0;

    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    static ArrayList<PlayerHandler> players = new ArrayList<>();

    public PlayerHandler(Socket socket){
        this.player = socket;
        this.id = count++;
        players.add(this);
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println((id+1)+"Client is connected to Handler .");
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
                        String messageFromClient = bufferedReader.readLine();
                        for(PlayerHandler client : players){
                            if(client.equals(me)) continue;
                            sendMessageToClient(messageFromClient,client);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error sending Message to client");
                        closeEverything(player,bufferedWriter,bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    public void sendMessageToClient(String message , PlayerHandler client) {
        try {
            client.bufferedWriter.write(message);
            client.bufferedWriter.newLine();
            client.bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending Message to client");
            closeEverything(this.player,bufferedWriter,bufferedReader);
        }
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter , BufferedReader bufferedReader){
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
