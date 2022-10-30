package com.nttd.wtdoodle.Client.Game.GameObjects;

import java.io.Serializable;

public class PenColor implements Serializable {
    double red;
    double green;
    double blue;

    double[] array;

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
}
