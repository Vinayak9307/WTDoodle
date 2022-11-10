package com.nttd.wtdoodle.SharedObjects;

public class GameSharable {
    private String gameCode;
    private String ipAddress;
    private int portNo;

    public GameSharable(String gameCode, String ipAddress, int portNo) {
        this.gameCode = gameCode;
        this.ipAddress = ipAddress;
        this.portNo = portNo;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNo() {
        return portNo;
    }

    public void setPortNo(int portNo) {
        this.portNo = portNo;
    }

    @Override
    public String toString() {
        return gameCode + ";" +ipAddress + ";" +portNo;
    }
}
