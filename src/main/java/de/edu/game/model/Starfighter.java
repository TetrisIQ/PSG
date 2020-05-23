package de.edu.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Starfighter extends AbstractMeeple {

    public Starfighter(String username, Field field, String name, String color) {
        super(username, field, name, color);
        this.setAttackRange(1);
        this.setDamage("1w20"); //TODO: load via Config

    }


    @Override
    public boolean move(Field newPos) {
        if(newPos.isEmpty()) {
            this.getField().setEmpty();
            newPos.setMeeple(this);
        }else {
            this.attack(newPos);
        }
        return true;
    }

    @Override
    public int nextPossibleMoves() {
        return 0;
    }

    @Override
    public void attack(Field pos) {

    }
}
