package com.nttd.wtdoodle.Server;


import java.sql.Connection;
import java.sql.DriverManager;

public class databaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName ="skribbl_db";
        String databaseUser ="root";
        String databasePassword ="";
        String url ="jdbc:mysql://localhost/"+ databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink=DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
