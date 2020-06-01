package de.edu.game.config.loader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // provide a Constructor with all args, witch is required by Gson
@Getter
public class MeepleConfig {

    private String name;

    private int hp;

    private String damage;

    private String defense;

    private int attackRange;

    private int mineSpeed;


}
