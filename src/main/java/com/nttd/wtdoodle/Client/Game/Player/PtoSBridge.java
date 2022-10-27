package com.nttd.wtdoodle.Client.Game.Player;

import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.net.Socket;

public class PtoSBridge {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public PtoSBridge(Socket socket){
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending Message to client");
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void sendMessageToServer(String message) {
        try{
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending Message to client");
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    public void receiveMessagesFromServer(GraphicsContext g) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        String messageFromServer = bufferedReader.readLine();
                        Player.decodeMessage(messageFromServer,g);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error sending Message to client");
                        closeEverything(socket,bufferedWriter,bufferedReader);
                        break;
                    }
                }
            }
        }).start();
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
