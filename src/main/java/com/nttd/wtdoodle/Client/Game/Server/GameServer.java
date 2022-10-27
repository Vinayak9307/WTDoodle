package com.nttd.wtdoodle.Client.Game.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            boolean running = true;
            int count = 0;
            System.out.println("Server started successfully....");
            System.out.println("Waiting for Client....");
            while(running){
                Socket client = serverSocket.accept();
                Thread t = new Thread(new PlayerHandler(client));
                t.start();
                if(count==4){
                    running = false;
                }
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
