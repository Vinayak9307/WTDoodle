package com.nttd.wtdoodle.Server;

import com.nttd.wtdoodle.SharedObjects.GameSharable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private ArrayList<GameSharable> runningGames = new ArrayList<>();
    private ArrayList<String> onlinePlayers = new ArrayList<>();

    public ArrayList<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public ArrayList<ClientHandler> getClients(){
        return clients;
    }

    public Server(){
        try {
            System.out.println("=====MAIN SERVER=====");
            serverSocket = new ServerSocket(9936);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void acceptConnections(){
        System.out.println("=====Waiting For Clients=====");
            try {
                while(true) {
                    Socket s = serverSocket.accept();
                    ClientHandler c = new ClientHandler(s,this);
                    clients.add(c);
                    Thread t = new Thread(c);
                    t.start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();
    }

    public ArrayList<GameSharable> getRunningGames() {
        return runningGames;
    }
}
