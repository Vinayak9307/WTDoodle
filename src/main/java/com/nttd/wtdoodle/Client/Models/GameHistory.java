package com.nttd.wtdoodle.Client.Models;

import java.util.ArrayList;

public class GameHistory {

    ArrayList<GameHistoryData> gameHistories = new ArrayList<>();
    public static final GameHistory instance = new GameHistory();
    public GameHistory(){}
    public static GameHistory getInstance(){
        return instance;
    }
    public ArrayList<GameHistoryData> getGameHistories() {
        return gameHistories;
    }

    public void setGameHistories(ArrayList<GameHistoryData> gameHistories) {
        this.gameHistories = gameHistories;
    }


}
