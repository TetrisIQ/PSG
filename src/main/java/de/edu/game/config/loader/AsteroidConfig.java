package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
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

}
