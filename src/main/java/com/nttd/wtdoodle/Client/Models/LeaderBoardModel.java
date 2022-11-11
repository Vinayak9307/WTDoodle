package com.nttd.wtdoodle.Client.Models;

import java.util.ArrayList;

public class LeaderBoardModel {
    ArrayList<LeaderBoardData> globalLeaderBoardData = new ArrayList<>();
    ArrayList<LeaderBoardData> friendsLeaderBoardData = new ArrayList<>();

    public ArrayList<LeaderBoardData> getFriendsLeaderBoardData() {
        return friendsLeaderBoardData;
    }

    public static LeaderBoardModel instance = new LeaderBoardModel();
    public LeaderBoardModel(){}

    public static LeaderBoardModel getInstance() {
        return instance;
    }

    public ArrayList<LeaderBoardData> getGlobalLeaderBoardData() {
        return globalLeaderBoardData;
    }

    public void setGlobalLeaderBoardData(ArrayList<LeaderBoardData> globalLeaderBoardData) {
        this.globalLeaderBoardData = globalLeaderBoardData;
    }
}
