package com.nttd.wtdoodle.Client.Connections;

import com.nttd.wtdoodle.Client.Login.LoginController;
import com.nttd.wtdoodle.Client.Login.RegisterController;
import com.nttd.wtdoodle.Client.Models.User;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.scene.Node;
import java.io.*;
import java.net.Socket;

public class CToSBridge implements Runnable{

    String ipAddress;
    int port;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    Node holder;
    User user;

    public static final CToSBridge instance = new CToSBridge();
    public CToSBridge(String ipAddress , int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }
    public CToSBridge(){
        user = User.getInstance();
    }

    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }
    public void setPort(int port){
        this.port = port;
    }
    public void setHolder(Node n){
        this.holder = n;
    }
    public void connectSocket(){
        try {
            this.socket = new Socket(ipAddress , port);
            System.out.println("Connected To Server....");
            System.out.println("Loading Login Page....");
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static CToSBridge getInstance(){
        return instance;
    }

    @Override
    public void run() {
            receiveMessageFromServer();
    }

    private void receiveMessageFromServer() {
        while(socket.isConnected()){
            try {
                String message = bufferedReader.readLine();
                decodeMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void decodeMessage(String message){
        String[] data = message.split(",");
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LOGIN_SUCCESSFUL){
            sendMessageToServer(new Message(Message.TYPE.REQUEST_USER_INFO,user.getUserId(),user.getUserName()));
            LoginController.goToDashboard(holder);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LOGIN_UNSUCCESSFUL){
            LoginController.addLabel(holder);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REGISTER_SUCCESSFUL){
            RegisterController.addLabel(holder,data[2]);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REGISTER_UNSUCCESSFUL){
            RegisterController.addLabel(holder,data[2]);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.SEND_USER_INFO){
            user.setUserId(Integer.parseInt(data[2]));
            user.setName(data[3]);
            user.setUserName(data[4]);
            user.setPassword(data[5]);
            user.setEmail(data[6]);
            user.setTotalScore(Integer.parseInt(data[7]));
            user.setGamesPlayed(Integer.parseInt(data[8]));
        }
    }
    public void sendMessageToServer(Message message){
        try {
            bufferedWriter.write(message.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
