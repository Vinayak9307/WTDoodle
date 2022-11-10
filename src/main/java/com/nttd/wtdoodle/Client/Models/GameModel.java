package com.nttd.wtdoodle.Client.Models;

import com.nttd.wtdoodle.SharedObjects.GameSharable;

public class GameModel {
    private static GameModel instance = new GameModel();
    String hostIp;
    String gameCode;
    int portNo;
    boolean allowedToJoined = false;

    public boolean isAllowedToJoined() {
        return allowedToJoined;
    }

    public void setAllowedToJoined(boolean allowedToJoined) {
        this.allowedToJoined = allowedToJoined;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public int getPortNo() {
        return portNo;
    }

    public void setPortNo(int portNo) {
        this.portNo = portNo;
    }

    public GameModel(){}

    public static GameModel getInstance(){
        return instance;
    }
}
