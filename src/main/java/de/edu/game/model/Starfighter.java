package de.edu.game.model;

import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Starfighter extends AbstractMeeple {

    public Starfighter(String username, Field field, String name, String color) {
        super(username, field, name, color);
        this.setAttackRange(1);
        this.setDamage("1w20"); //TODO: load via Config

    }


    @Override
    public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, CannotMoveException {
        if (isHasMoved()) {
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
                //if Position is not empty, a Starfighter will attack
                this.attack(newPos);
            }
            this.setHasMoved(true);
            return true;
        }
        return false;
    }

    @Override
    public int nextPossibleMoves() {
        return 0;
    }

    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();
        //TODO: implement attack

    }
}
