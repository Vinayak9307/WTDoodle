package com.nttd.wtdoodle.SharedObjects;

public class Message {
    public enum TYPE {
        LOGIN,
        REGISTER,
        REGISTER_SUCCESSFUL,
        REGISTER_UNSUCCESSFUL,
        LOGIN_SUCCESSFUL,
        LOGIN_UNSUCCESSFUL,
        USER_INFO,
        REQUEST_USER_INFO,
        REQUEST_USER_GAME_HISTORY,
        USER_GAME_HISTORY,
        REQUEST_LEADERBOARD,
        LEADERBOARD,
        FIND_USER,
        USER_FOUND,
        USER_NOT_FOUND,
        SEND_REQUEST,
        REQUEST_FRIEND_REQUESTS,
        FRIEND_REQUESTS,
        ADD_FRIEND,
        DELETE_REQUEST,
        ADD_GAME,
        DELETE_GAME,
        SEARCH_GAME,
        GAME_FOUND,
        GAME_NOT_FOUND,
        REQUEST_FRIEND_LIST,
        FRIEND_LIST,
        SEND_GAME_INVITE,
        GAME_INVITE,
        LOG_OUT
    }

    private TYPE type;
    private int id;
    private String content;

    public Message(TYPE type, int id, String content) {
        this.type = type;
        this.id = id;
        this.content = content;
    }

    public TYPE getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getContent(){
        return content;
    }

    @Override
    public String toString() {
        return type +"," + id + "," + content;
    }
}
