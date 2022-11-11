package com.nttd.wtdoodle.Client.Lobby;

public class GameData {
    private static GameData instance = new GameData();

    public static GameData getInstance() {
        return instance;
    }

    private int maxPlayers;
    private int guessingTime;
    private int numberOfRounds;

    public GameData(){}

    public int getNumberOfRounds(){
        return numberOfRounds;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public int getGuessingTime() {
        return guessingTime;
    }

    public void setGuessingTime(int guessingTime) {
        this.guessingTime = guessingTime;
    }


    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }
}
