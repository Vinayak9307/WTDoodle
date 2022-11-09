package com.nttd.wtdoodle.Client.Models;

import java.sql.Date;

public class LeaderBoardData {
    private int id;
    private String userName;
    private Date date;
    private int totalScore;

    public LeaderBoardData(int id, String userName, Date date, int totalScore) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.totalScore = totalScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
