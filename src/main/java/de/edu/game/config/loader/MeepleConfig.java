package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
/**
 * Configuration for basic Meeples
 */
public class MeepleConfig {


    private String name;

    private int hp;

    private String damage;

    private String defense;

    private int attackRange;

    private int coasts;
}
