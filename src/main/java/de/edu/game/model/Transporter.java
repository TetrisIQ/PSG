package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMineException;
import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Transporter extends AbstractMeeple {

    private int storage;
    private int storageMax;

    public Transporter(String username, Field field, String name, String color) {
        super(username, field, name, color);
    }

    @Override
    public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, CannotMineException {
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
                if(newPos.getMeeple().getName().equals(ConfigLoader.shared.getAsteroid().getName())) { // checks if the meeple on the field is an asteroid
                    Asteroid asteroid = (Asteroid) newPos.getMeeple();
                    //TODO: improve mining
                    //Storage implementation in transporter, maxStorage, and slower mining if storage is full
                    //Update Response Types
                    int mine = asteroid.mine(ConfigLoader.shared.getTransporter().getMineSpeed());
                    this.storage = mine;
                    return true;
                }
                else {
                    this.setHasMoved(false); // The transporter can move to an other field
                    return false;
                }
            }
            this.setHasMoved(true);
            return true;
        }
        this.setHasMoved(false);
        return false;
    }

    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();

    }
}
