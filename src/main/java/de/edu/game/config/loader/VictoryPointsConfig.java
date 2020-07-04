package de.edu.game.config.loader;

/**
 * Configuration for Victory Points
 */
public class VictoryPointsConfig {

    private int mining = 5;
    private int destroyTransporter = 10;
    private int destroyStarfighter = 30;
    private int destroySpaceStation = 1000;
    private int createTransporter = 25;
    private int createStarfighter = 25;

    public VictoryPointsConfig(int mining, int destroyTransporter, int destroyStarfighter, int destroySpaceStation, int createTransporter, int createStarfighter) {
        this.mining = mining;
        this.destroyTransporter = destroyTransporter;
        this.destroyStarfighter = destroyStarfighter;
        this.destroySpaceStation = destroySpaceStation;
        this.createTransporter = createTransporter;
        this.createStarfighter = createStarfighter;
    }

    public VictoryPointsConfig() {
    }

    public int getMining() {
        return this.mining;
    }

    public int getDestroyTransporter() {
        return this.destroyTransporter;
    }

    public int getDestroyStarfighter() {
        return this.destroyStarfighter;
    }

    public int getDestroySpaceStation() {
        return this.destroySpaceStation;
    }

    public int getCreateTransporter() {
        return this.createTransporter;
    }

    public int getCreateStarfighter() {
        return this.createStarfighter;
    }
}
