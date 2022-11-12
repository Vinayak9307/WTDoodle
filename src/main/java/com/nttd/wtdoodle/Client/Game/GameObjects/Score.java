package com.nttd.wtdoodle.Client.Game.GameObjects;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class Score {
    private static Score instance = new Score();
    ArrayList<Pair<String , Integer>> scores = new ArrayList<>();
    public static Score getInstance() {
        return instance;
    }

    public ArrayList<Pair<String, Integer>> getScores() {
        return scores;
    }

    public void sortScores() {
        final Comparator<Pair<String, Integer>> c = reverseOrder(comparing(Pair::getValue));
        scores.sort(c);
    }
}
