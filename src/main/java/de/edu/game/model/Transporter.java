package de.edu.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Transporter extends AbstractMeeple {

    public Transporter(String username, Field field, String name, String color) {
        super(username, field, name, color);
    }

    @Override
    public boolean move(Field newPos) {
        if(newPos.isEmpty()) {
            this.getField().setEmpty();
            newPos.setMeeple(this);

            return true;
        }
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
