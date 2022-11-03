package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.Serializable;

public class PenInfo implements Serializable {
    double x, y, size;
    boolean erase;
    PenColor color;

    public PenInfo(){
        x = 0;
        y = 0;
        size = 0;
        erase = false;
        color = new PenColor();
    }

    public PenInfo(double x, double y, double size, boolean erase, PenColor color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.erase = erase;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public boolean isErase() {
        return erase;
    }

    public PenColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return x+ "," + y +"," + size +"," + erase +"," + color;
    }
}

