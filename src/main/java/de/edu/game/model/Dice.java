package de.edu.game.model;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Model Class to represent dices
 */
public class Dice {

    private static final Logger log = Logger.getLogger(Dice.class.getName());
    private int high;
    private static final Random RANDOM = new Random();
    private int low = 1;
    private int amount = 1;
    private int extra = 0;

    /**
     * Create a dice with an initialisation string <br>
     * The initialisation string points out the range for random numbers, from 1 to X
     *
     * @param initString <b>Examples:</b> "1d20+10", "2d40" od "100d100+1"
     */
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
            // The pattern has no +
        }

    }

    /**
     * Throw the digital dice, and sum up the dots of it.
     *
     * @return a random integer based on the initialisation string
     */
    public int throwDice() {
        return throwDices(this.amount) + extra;
    }

    private int throwOneDice() {
        return RANDOM.nextInt(high - low) + low;
    }

    private int throwDices(int amount) {
        int sum = 0;
        for (int i = 0; i < amount; i++) {
            sum += throwOneDice();
        }
        return sum;
    }
}
