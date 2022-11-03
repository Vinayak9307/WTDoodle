package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.Serializable;
import java.util.Arrays;

public class PenColor implements Serializable {
    double red;
    double green;
    double blue;

    double[] array;

    public PenColor(){
        red = green = blue = 0;
        array = new double[]{0, 0, 0};
    }

    public PenColor(double a, double b, double c) {
        red = a;
        green = b;
        blue = c;
        array = new double[3];
        array[0] = red;
        array[1] = green;
        array[2] = blue;
    }

    public double[] getColor() {
        return array;
    }

    @Override
    public String toString() {
        return red +","+green +"," + blue;
    }
}
