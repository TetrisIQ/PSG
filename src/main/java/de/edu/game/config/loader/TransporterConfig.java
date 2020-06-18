package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransporterConfig extends MeepleConfig {

    private int maxStorage;
    private int mineSpeed;

    public TransporterConfig(int hp, String damage, String defense, int attackRange, int mineSpeed, int maxStorage) {
        super("Transporter", hp, damage, defense, attackRange);
    }
}
