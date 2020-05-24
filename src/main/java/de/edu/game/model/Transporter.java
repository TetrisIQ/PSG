package de.edu.game.model;

import de.edu.game.exceptions.HasAlreadyMovedException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Transporter extends AbstractMeeple {

    public Transporter(String username, Field field, String name, String color) {
        super(username, field, name, color);
    }

    @Override
    public boolean move(Map map, Field newPos) throws HasAlreadyMovedException {
        if (isHasMoved()) {
            // can only move on field per Round
            throw new HasAlreadyMovedException();
        }
        //checks if the Coordinate of the new Position is next to the meeple
        if (this.canMove(map, newPos)) {
            //check if new Position is empty
            if (newPos.isEmpty()) {
                this.getField().setEmpty();
                newPos.setMeeple(this);
                this.setField(newPos);
            } else {
                //if Position is not empty, a Transporter will not attack
                this.setHasMoved(false); // The transporter can move to an other field
                return false;
            }
            this.setHasMoved(true);
            return true;
        }
        this.setHasMoved(false);
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
