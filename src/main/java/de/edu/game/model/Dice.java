package de.edu.game.model;

import lombok.extern.java.Log;

import java.util.Random;

@Log
public class Dice {

    private int high;
    private Random random = new Random();
    private int low = 1;
    private int amount = 1;
    private int extra = 0;

    public Dice(String initString) {
        initString = initString.toLowerCase().replace("w", "d");
        try {
            this.high = Integer.parseInt(initString.split("d")[1].split("\\+")[0]);
            this.amount = Integer.parseInt(initString.split("d")[0]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Dice configuration incorrect");
        }
        try {
            this.extra = Integer.parseInt(initString.split("\\+")[1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            // The pattern has ne +
        }

    }

    public int throwDice() {
        return throwDices(this.amount) + extra;
    }

    private int throwOneDice() {
        return random.nextInt(high - low) + low;
    }

    private int throwDices(int amount) {
        int sum = 0;
        for (int i = 0; i < amount; i++) {
            sum += throwOneDice();
        }
        return sum;
    }
}
