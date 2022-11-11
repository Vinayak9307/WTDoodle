package com.nttd.wtdoodle.Server;

import com.nttd.wtdoodle.SharedObjects.GameSharable;
import com.nttd.wtdoodle.SharedObjects.Message;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class ClientHandler implements Runnable{

    Server server;
    Socket socket;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    DatabaseConnection databaseConnection;
    String userName;

    public String getUserName() {
        return userName;
    }

    @Override
    public void run() {
        while(socket.isConnected()){
            try {
                String message = bufferedReader.readLine();
                if(message != null)
                    decodeMessage(message);
            } catch (IOException e) {
                System.out.println("Error in ClientHandlerThread.");
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        System.out.println("Client left.");
    }
    public ClientHandler(Socket socket,Server server){
        this.socket = socket;
        this.server = server;
        this.databaseConnection = new DatabaseConnection();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void decodeMessage(String message){
        String[] data = message.split(",");
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LOGIN){
            Connection connection = databaseConnection.getConnection();
            String verifyLogin ="SELECT count(1) FROM user WHERE username = '" + data[2] + "' And password = '" + data[3] + "'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(verifyLogin);
                while(resultSet.next()){
                    if(resultSet.getInt(1)==1){
                        sendMessageToClient(new Message(Message.TYPE.LOGIN_SUCCESSFUL,0,""));
                        this.userName = data[2];
                        server.getOnlinePlayers().add(userName);
                    }
                    else{
                        sendMessageToClient(new Message(Message.TYPE.LOGIN_UNSUCCESSFUL,0,""));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.LOG_OUT){
            server.getOnlinePlayers().removeIf(userName -> userName.equals(data[2]));
            System.out.println(data[2] + " has logged out .");
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REGISTER){

            Connection connection = databaseConnection.getConnection();
            //check if username already exits or not
            String verifyUser = "SELECT count(1) FROM user WHERE username = '" + data[2] + "'";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(verifyUser);

                while(resultSet.next()){
                    if(resultSet.getInt(1) == 1){
                        sendMessageToClient(new Message(Message.TYPE.REGISTER_UNSUCCESSFUL,0,"User already exists with this Username."));
                    }
                    else{
                        String name = data[2];
                        String userName = data[3];
                        String email = data[4];
                        String password = data[5];
                        int count=0;
                        for(int i=0;i<email.length();i++)
                        {
                            if(email.charAt(i)=='@')
                                count++;
                        }
                        if(count == 1){
                            String insertFields = "INSERT INTO user(name , username , password , email) VALUES ('";
                            String insertValues = name + "','" + userName + "','" + password + "','" + email + "')";
                            String insertTORegister = insertFields + insertValues;

                            Statement statement1 = connection.createStatement();
                            statement1.executeUpdate(insertTORegister);
                            sendMessageToClient(new Message(Message.TYPE.REGISTER_SUCCESSFUL,0,"User Registered Successfully"));
                        }else{
                            sendMessageToClient(new Message(Message.TYPE.REGISTER_UNSUCCESSFUL,0,"Enter Valid Email."));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REQUEST_USER_INFO){
            Connection connection = databaseConnection.getConnection();
            String getUser = "SELECT * FROM user WHERE username = '" + data[2] + "'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getUser);

                while(resultSet.next()){
                    int id = resultSet.getInt("userId");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    int highScore = resultSet.getInt("totalScore");
                    int gamesPlayed = resultSet.getInt("totalGamesPlayed");

                    sendMessageToClient(new Message(Message.TYPE.USER_INFO,0,
                            id +","+ name +","+ username +","+ password +","+ email+","+highScore+","+gamesPlayed));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REQUEST_USER_GAME_HISTORY){
            Connection connection = databaseConnection.getConnection();
            String query = "SELECT game.gameId ,game.Date , game.winner , game.playerUsername , game.playerScore FROM game,gameplayed WHERE gameplayed.gameId=game.gameId AND gameplayed.username='"+data[2]+"'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                StringBuilder send = new StringBuilder();

                while(resultSet.next()){
                    int id = resultSet.getInt("gameId");
                    Date date = resultSet.getDate("Date");
                    String winner = resultSet.getString("winner");
                    String playerScore = "";
                    String[] playerUsernames = resultSet.getString("playerUsername").split(";");
                    String[] playerScores = resultSet.getString("playerScore").split(";");
                    for(int i = 0 ; i < playerUsernames.length ; i++){
                        if(playerUsernames[i].equals(data[2])){
                            playerScore = playerScores[i];
                        }
                    }

                    StringBuilder sc = new StringBuilder();
                    sc.append(id).append(" ").append(date).append(" ").append(winner).append(" ").append(playerScore);
                    send.append(sc.toString()).append(";");
                }
                sendMessageToClient(new Message(Message.TYPE.USER_GAME_HISTORY , 0 , send.toString()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REQUEST_LEADERBOARD) {
            Connection connection = databaseConnection.getConnection();
            String query = "SELECT * FROM globalleader ORDER BY totalScore DESC";

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                StringBuilder send = new StringBuilder();

                while(resultSet.next()){
                    String userName = resultSet.getString("username");
                    Date date = resultSet.getDate("date");
                    int totalScore = resultSet.getInt("totalScore");

                    StringBuilder sc = new StringBuilder();
                    sc.append(userName).append(" ").append(date).append(" ").append(totalScore);
                    send.append(sc.toString()).append(";");
                }
                sendMessageToClient(new Message(Message.TYPE.LEADERBOARD , 0 , send.toString()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.FIND_USER) {
            Connection connection = databaseConnection.getConnection();
            String findUser = "SELECT count(1),user.username,user.name FROM user WHERE username = '" + data[2] + "'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(findUser);
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        sendMessageToClient(new Message(Message.TYPE.USER_FOUND, 0, resultSet.getString("name") + ";" + resultSet.getString("username")));
                    } else {
                        sendMessageToClient(new Message(Message.TYPE.USER_NOT_FOUND, 0, " "));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.SEND_REQUEST){
            Connection connection = databaseConnection.getConnection();
            String []send = data[2].split(";");
            String updateQuery = "INSERT INTO `request`(`name`, `request`) VALUES ('"+send[1]+"','"+send[0]+"')";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REQUEST_FRIEND_REQUESTS){
            Connection connection = databaseConnection.getConnection();
            String query = "SELECT * FROM `request` WHERE name='" + data[2]+"'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                StringBuilder send = new StringBuilder();
                while(resultSet.next()){
                    String senderName = resultSet.getString("request");
                    send.append(senderName).append(";");
                }
                sendMessageToClient(new Message(Message.TYPE.FRIEND_REQUESTS,0,send.toString()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.ADD_FRIEND){
            Connection connection = databaseConnection.getConnection();
            String []send = data[2].split(";");
            String updateQuery1 = "INSERT INTO `friends`(`person`, `friend`) VALUES ('"+send[1]+"','"+send[0]+"')";
            String updateQuery2 = "INSERT INTO `friends`(`person`, `friend`) VALUES ('"+send[0]+"','"+send[1]+"')";
            String updateQuery3 = "DELETE FROM `request` WHERE name='"+send[0]+"' AND request='"+send[1]+"'";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateQuery1);
                statement.executeUpdate(updateQuery2);
                statement.executeUpdate(updateQuery3);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.DELETE_REQUEST){
            Connection connection = databaseConnection.getConnection();
            String []send = data[2].split(";");
            String updateQuery3 = "DELETE FROM `request` WHERE name='"+send[0]+"' AND request='"+send[1]+"'";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateQuery3);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.ADD_GAME){
            String[] gameInfo = data[2].split(";");
            server.getRunningGames().add(new GameSharable(gameInfo[0],gameInfo[1],Integer.parseInt(gameInfo[2])));
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.DELETE_GAME){
            server.getRunningGames().removeIf(g -> g.getGameCode().equals(data[2]));
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.SEARCH_GAME){
            boolean found = false;
            for (GameSharable g : server.getRunningGames()){
                if(g.getGameCode().equals(data[2])){
                    sendMessageToClient(new Message(Message.TYPE.GAME_FOUND,0,g.toString()));
                    found = true;
                    break;
                }
            }
            if(!found){
                sendMessageToClient(new Message(Message.TYPE.GAME_NOT_FOUND,0," "));
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.REQUEST_FRIEND_LIST){
            Connection connection = databaseConnection.getConnection();
            String query = "SELECT friend FROM friends WHERE person='"+data[2]+"'";

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                StringBuilder friends = new StringBuilder();
                while(resultSet.next()){
                    String friend = resultSet.getString("friend");
                    friends.append(friend).append(" ");
                    if(server.getOnlinePlayers().contains(friend)){
                        friends.append("1");
                    }else{
                        friends.append("0");
                    }
                    friends.append(";");
                }
                sendMessageToClient(new Message(Message.TYPE.FRIEND_LIST,0,friends.toString()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.SEND_GAME_INVITE){
            String[] friendData = data[2].split(";");
            if(server.getOnlinePlayers().contains(friendData[1])){
                for(ClientHandler clientHandler : server.getClients()){
                    if(clientHandler.getUserName().equals(friendData[1])){
                        clientHandler.sendMessageToClient(new Message(Message.TYPE.GAME_INVITE,0,friendData[0]+";"+friendData[2]));
                    }
                }
            }
        }
        if(Message.TYPE.valueOf(data[0]) == Message.TYPE.UPDATE_GAME_DETAIL) {
            String[] data1 = data[2].split("@");
            ArrayList<Pair<String , Integer>> scores = new ArrayList<>();
            for(String d : data1){
                String[] p = d.split(" : ");
                Pair<String , Integer> sc = new Pair<>(p[0] , Integer.parseInt(p[1]));
                scores.add(sc);
            }
            final Comparator<Pair<String, Integer>> c = reverseOrder(comparing(Pair::getValue));
            scores.sort(c);
            String winner = scores.get(0).getKey();
            String highScore = String.valueOf(scores.get(0).getValue());
            String numOfPlayers = String.valueOf(scores.size());
            String date = LocalDate.now().toString();
            String gameId = data[1];
            StringBuilder playerUsernames = new StringBuilder();
            StringBuilder playerScores = new StringBuilder();
            for(Pair<String ,Integer> s : scores){
                playerUsernames.append(s.getKey()).append(";");
                playerScores.append(s.getValue()).append(";");
            }

            Connection connection = databaseConnection.getConnection();
            String update = "INSERT INTO `game`( `gameId`,`winner`, `numOfPlayers`, `highScore`, `playerUsername`, `playerScore`, `Date`)" +
                    " VALUES ('"+gameId+"','"+winner+"','"+numOfPlayers+"','"+highScore+"','"+playerUsernames+"','"+playerScores+"','"+date+"')";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(update);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            for(Pair<String ,Integer> s : scores){
                String updateQuery2 = "INSERT INTO `gameplayed`(`username`, `gameId`) VALUES ('"+s.getKey()+"','"+gameId+"')";
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(updateQuery2);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            for(Pair<String , Integer> s : scores){
                int totalScoreOfUser = 0;
                int totalGamesPlayedByUser = 0;
                String query = "SELECT `totalScore`,`totalGamesPlayed` FROM `user` WHERE `username`='"+s.getKey()+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while(resultSet.next()){
                        totalScoreOfUser = resultSet.getInt("totalScore");
                        totalGamesPlayedByUser = resultSet.getInt("totalGamesPlayed");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                totalGamesPlayedByUser += 1;
                totalScoreOfUser += s.getValue();
                String deleteQuery = "DELETE FROM `globalleader` WHERE username='"+s.getKey()+"'";
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(deleteQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String insertQuery = "INSERT INTO `globalleader`(`username`, `totalScore`, `date`) VALUES ('"+s.getKey()+"','"+totalScoreOfUser+"','"+date+"')";
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(insertQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String updateQuery = "UPDATE `user` SET `totalGamesPlayed`="+totalGamesPlayedByUser+",`totalScore`="+totalScoreOfUser+" WHERE `username`='"+s.getKey()+"'";
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(updateQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
}
    public void sendMessageToClient(Message m){
        try {
            bufferedWriter.write(m.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
