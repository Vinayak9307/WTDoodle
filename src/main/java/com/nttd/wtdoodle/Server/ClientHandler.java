package com.nttd.wtdoodle.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    Server server;
    Socket socket;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;

    @Override
    public void run() {
        while(true){
            try {
                String message = bufferedReader.readLine();
                decodeMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ClientHandler(Socket socket,Server server){
        this.socket = socket;
        this.server = server;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void decodeMessage(String message){
        String[] data = message.split(",");
    }
}
