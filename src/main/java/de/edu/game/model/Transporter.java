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

/**
 * Model Class witch represents a Transporter
 */
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

    /**
     * Move a {@link Transporter} to the new {@link Field} if possible
     *
     * @param map      The @{@link Map}
     * @param newField the new Field where the meeple should move
     * @return True if the {@link Transporter} has moved successful
     * @throws HasAlreadyMovedException If the meeple has already moved this round, it cannot move again
     * @throws CannotMineException      The meeple (@{@link Transporter}) cannot mine here
     * @throws StorageFullException     The meeple (@{@link Transporter}) cannot mine more energy, because the storage is full
     */
    @Override
    public boolean move(Map map, Field newField) throws HasAlreadyMovedException, CannotMineException, StorageFullException {
        if (isHasMoved()) {
            // can only move on field per Round
            throw new HasAlreadyMovedException();
        }
        setHasMoved(true);
        //checks if the Coordinate of the new Position is next to the meeple
        if (this.canMove(map, newField)) {
            //check if new Position is empty
            if (newField.isEmpty()) {
                this.getField().setEmpty();
                newField.setMeeple(this);
                this.setField(newField);
            } else {
                // if the position is not empty, a Transporter will not attack
                // if the position is not empty and the other meeple is an Asteroid the Transporter will start mining if possible
                if (newField.getMeeple().getName().equals(ConfigLoader.shared.getAsteroid().getName())) {
                    return checkAndMine(newField);
                }
                // if the position is not empty and the other meeple is a SpaceStation the Transporter will start deploying the energy to the SpaceStation
                if (newField.getMeeple().getName().equals(ConfigLoader.shared.getSpaceStation().getName())) {
                    return deployToSpaceStation(newField);
                } else {
                    this.setHasMoved(false); // The transporter can move to an other field
                    return false;
                }
            }
            return true;
        }
        this.setHasMoved(false);
        return false;
    }

    /**
     * Deploy energy to SpaceStation
     *
     * @param newPos Position of the SpaceStation
     * @return True if the deployment has been successful
     */
    private boolean deployToSpaceStation(Field newPos) {
        SpaceStation spaceStation = (SpaceStation) newPos.getMeeple();
        spaceStation.addEnergy(this.storage);
        this.storage = 0;
        this.setHasMoved(true);
        return true;
    }

    /**
     * Check and mien storage from an {@link Asteroid}
     *
     * @param field Field of the {@link Asteroid}
     * @return True if the mining is successful
     * @throws CannotMineException  The meeple (@{@link Transporter}) cannot mine here
     * @throws StorageFullException The meeple (@{@link Transporter}) cannot mine more energy, because the storage is full
     */
    private boolean checkAndMine(Field field) throws CannotMineException, StorageFullException {
        Asteroid asteroid = (Asteroid) field.getMeeple();
        if (this.storage < this.maxStorage) { // check if there is capacity
            int storageLeft = this.maxStorage - this.storage; // checks how much storage is available
            int nextMining = Math.min(storageLeft, this.mineSpeed); // if there are more storage left as we can mine in one round, we can mine 100% this round, otherwiese we mine only so much our storage can hold
            int mine = asteroid.mine(nextMining);
            this.storage += mine;
            addMiningPoints();
            // disapear the Asteroid if there is not storage left
            return true;
        } else {
            throw new StorageFullException();
        }
    }

    /**
     * add victoryPoints for mining
     */
    private void addMiningPoints() {
        this.getUser().addPoints(5);
    }


    /**
     * {@link Transporter} cannot Attack other meeple they always throw {@link CannotMoveException}
     *
     * @param pos {@link Field} where the other meeple stands on
     * @throws CannotMoveException The meeple cannot move to this point, but can move to another this round
     */
    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();
    }

    @Override
    public String toString() {
        return "Transporter-" + this.getColor() + "-" + this.isHasMoved();
    }
}
