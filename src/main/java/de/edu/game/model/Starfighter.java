package de.edu.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Starfighter extends AbstractMeeple {

    public Starfighter(Map map, String username, Coordinate coordinate, String name, String color) {
        super(map, username, coordinate, name, color);
        this.setAttackRange(1);
        this.setDamage("1w20"); //TODO: load via Config

    }


    @Override
    public boolean move(Field newPos) {
        return false;
    }

    @Override
    public int nextPossibleMoves() {
        return 0;
    }

    @Override
    public void attack(Field pos) {

    }
}
