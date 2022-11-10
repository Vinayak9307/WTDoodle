package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.Serializable;

public class GameMessage implements Serializable {

    public enum TYPE{
        START_GAME,
        NEW_PLAYER_JOINED,
        SET_NAME,
        SET_ID,
        PEN_POSITION,
        GUESS,
        SET_DRAWER,
        WORD_SELECTION,
        SET_CURRENT_WORD,
        GENERAL,
        SUCCESSFULLY_GUESSED,
        UPDATE_TIMER,
        CLOSE_CONNECTION,
        SET_SCORE
    }
    private TYPE type;
    private int id;
    private String content;
    public GameMessage(){
        id = 0;
        type = TYPE.GENERAL;
        content = "";
    }

    public GameMessage(TYPE type, int id, String content) {
        this.type = type;
        this.id = id;
        this.content = content;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return type + "," + id + "," + content;
    }
}