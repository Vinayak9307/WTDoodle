package com.nttd.wtdoodle.SharedObjects;

public class Message {
    public enum TYPE {
        LOGIN,
        REGISTER,
        REGISTER_SUCCESSFUL,
        REGISTER_UNSUCCESSFUL,
        LOGIN_SUCCESSFUL,
        LOGIN_UNSUCCESSFUL
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
