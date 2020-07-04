package de.edu.game.config.loader;

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

    public MeepleConfig(String name, int hp, String damage, String defense, int attackRange, int coasts) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.attackRange = attackRange;
        this.coasts = coasts;
    }

    public MeepleConfig() {
    }

    public String getName() {
        return this.name;
    }

    public int getHp() {
        return this.hp;
    }

    public String getDamage() {
        return this.damage;
    }

    public String getDefense() {
        return this.defense;
    }

    public int getAttackRange() {
        return this.attackRange;
    }

    public int getCoasts() {
        return this.coasts;
    }
}
