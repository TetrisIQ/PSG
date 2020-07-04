package de.edu.game.config.loader;

/**
 * Configuration for Asteroids
 */
public class AsteroidConfig {

    private String name = "Asteroid";

    private int hp = 30;

    private int mineRange = 1;

    private int energy = 100;

    private String color = "#DCDF62";

    private int maxAsteroids = 20;

    public AsteroidConfig(String name, int hp, int mineRange, int energy, String color, int maxAsteroids) {
        this.name = name;
        this.hp = hp;
        this.mineRange = mineRange;
        this.energy = energy;
        this.color = color;
        this.maxAsteroids = maxAsteroids;
    }

    public AsteroidConfig() {
    }

    public String getName() {
        return this.name;
    }

    public int getHp() {
        return this.hp;
    }

    public int getMineRange() {
        return this.mineRange;
    }

    public int getEnergy() {
        return this.energy;
    }

    public String getColor() {
        return this.color;
    }

    public int getMaxAsteroids() {
        return this.maxAsteroids;
    }
}
