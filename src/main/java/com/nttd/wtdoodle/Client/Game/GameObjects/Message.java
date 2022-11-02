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
    private final int ID;
    private final String name;
    private final String message;
    private final type t;
    private final PenInfo pen;

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
        return ID +"," + name + "," + message + "," + t +"," + pen;
    }
}
