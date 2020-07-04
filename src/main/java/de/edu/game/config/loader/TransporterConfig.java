package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
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
}
