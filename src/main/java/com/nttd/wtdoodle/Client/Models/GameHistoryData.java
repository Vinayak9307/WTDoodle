package com.nttd.wtdoodle.Client.Models;

import java.sql.Date;

public class GameHistoryData {
    private int id;
    private Date date;
    private int yourScore;
    private String winner;

    public GameHistoryData(int id, Date date, int yourScore, String winner) {
        this.id = id;
        this.date = date;
        this.yourScore = yourScore;
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYourScore() {
        return yourScore;
    }

    public void setYourScore(int yourScore) {
        this.yourScore = yourScore;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
