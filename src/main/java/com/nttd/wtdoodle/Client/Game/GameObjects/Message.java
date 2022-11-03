package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
        updateTimer,
        closeConnection,
        setScore
    }
    private int ID;
    private String name;
    private String message;
    private type t;
    private PenInfo pen;

    public Message(){
        ID = 0;
        name = "";
        message = "";
        t = type.general;
        pen = new PenInfo();
    }

    public Message(type t,int ID, String name, String message,PenInfo p) {
        this.ID = ID;
        this.name = name;
        this.message = message;
        this.t = t;
        this.pen = p;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
    public type getType() {
        return t;
    }
    public PenInfo getPen() {
        return pen;
    }

    @Override
    public String toString() {
        return t +","+ID+"," + name + "," + message +"," + pen;
    }
}
