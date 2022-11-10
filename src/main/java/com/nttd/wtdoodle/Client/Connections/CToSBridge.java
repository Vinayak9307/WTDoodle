package com.nttd.wtdoodle.Client.Connections;

import com.nttd.wtdoodle.Client.Dashboard.Dashboard;
import com.nttd.wtdoodle.Client.Dashboard.SearchFriend;
import com.nttd.wtdoodle.Client.Lobby.OtherLobby;
import com.nttd.wtdoodle.Client.Login.LoginController;
import com.nttd.wtdoodle.Client.Login.RegisterController;
import com.nttd.wtdoodle.Client.Models.*;
import com.nttd.wtdoodle.SharedObjects.GameSharable;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.scene.Node;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class CToSBridge implements Runnable{

    String ipAddress;
    int port;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    Node holder;
    User user;
    GameHistory gameHistory;
    LeaderBoardModel leaderBoard;

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
        while(socket.isConnected() && !Thread.currentThread().isInterrupted()){
            try {
                String message = bufferedReader.readLine();
                decodeMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Closing CtoSBridge.");
    }
    private void decodeMessage(String message){
        String[] data = message.split(",");
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LOGIN_SUCCESSFUL){
            sendMessageToServer(new Message(Message.TYPE.REQUEST_USER_INFO,99,user.getUserName()));
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
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.USER_INFO){
            user.setUserId(Integer.parseInt(data[2]));
            user.setName(data[3]);
            user.setUserName(data[4]);
            user.setPassword(data[5]);
            user.setEmail(data[6]);
            user.setTotalScore(Integer.parseInt(data[7]));
            user.setGamesPlayed(Integer.parseInt(data[8]));
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.USER_GAME_HISTORY){
            if(data.length>2) {
                String[] gameHistoryStr = data[2].split(";");
                gameHistory = GameHistory.getInstance();
                gameHistory.getGameHistories().clear();
                for (int i = 0; i < gameHistoryStr.length; i++) {
                    String[] gameHistoryData = gameHistoryStr[i].split(" ");
                    GameHistoryData g = new GameHistoryData(Integer.parseInt(gameHistoryData[0]), Date.valueOf(gameHistoryData[1]),
                            0, gameHistoryData[2]);
                    gameHistory.getGameHistories().add(g);
                }
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LEADERBOARD) {
            if (data.length>2) {
                String[] leaderboardStr = data[2].split(";");
                leaderBoard = LeaderBoardModel.getInstance();
                leaderBoard.getLeaderBoardData().clear();
                for (String s : leaderboardStr) {
                    String[] leaderboardData = s.split(" ");
                    LeaderBoardData l = new LeaderBoardData(0, leaderboardData[0], Date.valueOf(leaderboardData[1]), Integer.parseInt(leaderboardData[2]));
                    leaderBoard.getLeaderBoardData().add(l);
                }
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.USER_FOUND){
            SearchFriend.openFriendRequestDialog(holder , data[2]);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.USER_NOT_FOUND){
            SearchFriend.addLabel(holder , "User not Found !");
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.FRIEND_REQUESTS){
            if(data.length > 2){
                String[] senders = data[2].split(";");
                FriendRequest friendRequest = FriendRequest.getInstance();
                friendRequest.getRequestData().clear();
                for(String s : senders){
                    friendRequest.getRequestData().add(new FriendRequestData(s));
                }
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.GAME_FOUND){
            String[] gameData = data[2].split(";");
            GameSharable g = new GameSharable(gameData[0],gameData[1],Integer.parseInt(gameData[2]));
            System.out.println("Active game found with ipAddress : " + g.getIpAddress()+" and port No:"+g.getPortNo());
            OtherLobby.joinGame(g,holder);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.GAME_NOT_FOUND){
            OtherLobby.updateStatusLabel("Active game not found with given code.",holder);
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.FRIEND_LIST){
            if(data.length>2) {
                String[] friends = data[2].split(";");
                Dashboard.updateFriendList(friends,holder);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.GAME_INVITE){
            String[] inviteData = data[2].split(";");
            String friendName = inviteData[0];
            String gameCode = inviteData[1];
            Pair<String , String> invitePair = new Pair<>(friendName,gameCode);
            user.getInvites().add(invitePair);
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
