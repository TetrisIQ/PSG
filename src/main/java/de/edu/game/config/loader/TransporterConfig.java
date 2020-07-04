package de.edu.game.config.loader;

/**
 * Configuration for Transporter
 */
public class TransporterConfig extends MeepleConfig {

    private int maxStorage;
    private int mineSpeed;

    public TransporterConfig(int hp, String damage, String defense, int attackRange, int mineSpeed, int maxStorage, int coasts) {
        super("Transporter", hp, damage, defense, attackRange, coasts);
        this.mineSpeed = mineSpeed;
        this.maxStorage = maxStorage;
    }

    public TransporterConfig(int maxStorage, int mineSpeed) {
        this.maxStorage = maxStorage;
        this.mineSpeed = mineSpeed;
    }

    public int getMaxStorage() {
        return this.maxStorage;
    }

    public int getMineSpeed() {
        return this.mineSpeed;
    }
}
