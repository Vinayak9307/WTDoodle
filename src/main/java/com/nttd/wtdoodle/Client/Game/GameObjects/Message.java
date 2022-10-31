package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.Serializable;

public class Message implements Serializable {
    public enum type{
        start,
        setID,
        penPosition,
        guess,
        setDrawer,
        wordSelection,
        setCurrentWord,
        general,
        successfullyGuessed,
        updateTimer
    }
    private int ID;
    private String name;
    private String message;
    private type t;

    private PenInfo pen;

    public Message(type t,int ID, String name, String message) {
        this.ID = ID;
        this.name = name;
        this.message = message;
        this.t = t;
    }
    public Message(type t , int ID) {
        this.t = t;
        this.ID = ID;
    }
    public Message(type t){
        this.t = t;
    }

    public Message(type t , PenInfo p){
        this.t = t;
        this.pen = p;
    }

    public PenInfo getPen() {
        return pen;
    }

    public Message(int ID, String message, type t) {
        this.ID = ID;
        this.message = message;
        this.t = t;
    }
    public Message(type t , String message){
        this.t = t;
        this.message = message;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public type getType() {
        return t;
    }

    public void setT(type t) {
        this.t = t;
    }
}
