package com.nttd.wtdoodle.Client.Models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;

public class User {

    private String name;
    private String userName;
    private String password;
    private String email;
    private int userId;
    private int totalScore;
    private int gamesPlayed;
    private boolean joinedViaInvite = false;
    private String inviteCode;
    private ArrayList<String> friends = new ArrayList<>();
    public ArrayList<String> getFriends() {
        return friends;
    }
    public boolean isJoinedViaInvite() {
        return joinedViaInvite;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setJoinedViaInvite(boolean joinedViaInvite) {
        this.joinedViaInvite = joinedViaInvite;
    }

    private ArrayList<String> onlineFriends = new ArrayList<>();
    private ArrayList<Pair<String,String>> invites = new ArrayList<>();
    public ArrayList<String> getOnlineFriends() {
        return onlineFriends;
    }
    public static final User instance = new User();
    public User(){}
    public static User getInstance(){
        return instance;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public void clear(){
        this.userName = "";
        this.email = "";
        this.userId = 0;
        this.password ="";
        this.name ="";
        this.totalScore=0;
        this.gamesPlayed=0;
    }
    public ArrayList<Pair<String, String>> getInvites() {
        return invites;
    }
}
