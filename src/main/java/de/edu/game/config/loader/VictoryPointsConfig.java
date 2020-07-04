package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
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

}
