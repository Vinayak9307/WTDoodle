package com.nttd.wtdoodle.Client.Models;

import java.util.ArrayList;

public class LeaderBoardModel {
    ArrayList<LeaderBoardData> leaderBoardData = new ArrayList<>();
    public static LeaderBoardModel instance = new LeaderBoardModel();
    public LeaderBoardModel(){}

    public static LeaderBoardModel getInstance() {
        return instance;
    }

    public ArrayList<LeaderBoardData> getLeaderBoardData() {
        return leaderBoardData;
    }

    public void setLeaderBoardData(ArrayList<LeaderBoardData> leaderBoardData) {
        this.leaderBoardData = leaderBoardData;
    }
}
