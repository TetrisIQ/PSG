package de.edu.game.model;

import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMineException;
import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import de.edu.game.exceptions.StorageFullException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
public class Transporter extends AbstractMeeple {

    private int storage = 0;
    private int maxStorage = ConfigLoader.shared.getTransporter().getMaxStorage();
    private int mineSpeed = ConfigLoader.shared.getTransporter().getMineSpeed();

    @Autowired
    transient UserService userService;

    public Transporter(User user, Field field, String name, String color) {
        super(user, field, name, color);
        this.setAttackRange(ConfigLoader.shared.getTransporter().getAttackRange());
        this.setDamage(ConfigLoader.shared.getTransporter().getDamage());
        this.setDefense(ConfigLoader.shared.getTransporter().getDefense());
        this.maxStorage = ConfigLoader.shared.getTransporter().getMaxStorage();
        this.mineSpeed = ConfigLoader.shared.getTransporter().getMineSpeed();
    }

    @Override
    public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, CannotMineException, StorageFullException {
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
                // if the position is not empty, a Transporter will not attack
                // if the position is not empty and the other meeple is an Asteroid the Transporter will start mining if possible
                if (newPos.getMeeple().getName().equals(ConfigLoader.shared.getAsteroid().getName())) {
                    return checkAndMine(newPos);
                }
                // if the position is not empty and the other meeple is a SpaceStation the Transporter will start deploying the energy to the SpaceStation
                if (newPos.getMeeple().getName().equals(ConfigLoader.shared.getSpaceStation().getName())) {
                    return deployToSpaceStation(newPos);
                } else {
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

    private boolean deployToSpaceStation(Field newPos) {
        SpaceStation spaceStation = (SpaceStation) newPos.getMeeple();
        spaceStation.addEnergy(this.storage);
        this.storage = 0;
        this.setHasMoved(true);
        return true;
    }

    private boolean checkAndMine(Field newPos) throws CannotMineException, StorageFullException {
        Asteroid asteroid = (Asteroid) newPos.getMeeple();
        if (this.storage < this.maxStorage) { // check if there is capacity
            int storageLeft = this.maxStorage - this.storage; // checks how much storage is available
            int nextMining = storageLeft < this.mineSpeed ? storageLeft : this.mineSpeed; // if there are more storage left as we can mine in one round, we can mine 100% this round, otherwiese we mine only so much our storage can hold
            int mine = asteroid.mine(nextMining);
            this.storage += mine;
            this.setHasMoved(true);
            addMiningPoints();
            return true;
        } else {
            throw new StorageFullException();
        }
    }

    private void addMiningPoints() {
        this.getUser().addPoints(5);
    }

    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();

    }
}
