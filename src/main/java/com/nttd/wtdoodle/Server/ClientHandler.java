package com.nttd.wtdoodle.Server;

import com.nttd.wtdoodle.SharedObjects.Message;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientHandler implements Runnable{

    Server server;
    Socket socket;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    DatabaseConnection databaseConnection;

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
                    }
                    else{
                        sendMessageToClient(new Message(Message.TYPE.LOGIN_UNSUCCESSFUL,0,""));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
