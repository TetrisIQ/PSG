package de.edu.game.model;

import de.edu.game.StartupRunner;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMineException;
import de.edu.game.exceptions.CannotMoveException;

import javax.persistence.Entity;

/**
 * Model Class that represents an Asteroid
 */
@Entity
public class Asteroid extends AbstractMeeple {

    private int energyStorage;

    public Asteroid(Field field) {
        super(StartupRunner.ADMIN, field, ConfigLoader.shared.getAsteroid().getName(), ConfigLoader.shared.getAsteroid().getColor()); // we can make a new empty user, because the Asteroid has no owner
        this.energyStorage = ConfigLoader.shared.getAsteroid().getEnergy();
        this.setDefense("0d0");
        this.setShieldEnergy(100);

    }

    public Asteroid() {
    }

    /**
     * Mine energy on the {@link Asteroid}
     *
     * @param energyToMine How much energy should be mined
     * @return the amount of mined energy
     * @throws CannotMineException The meeple (@{@link Transporter}) cannot mine here
     */
    public int mine(int energyToMine) throws CannotMineException {
        if (this.energyStorage > 0) {
            if (this.energyStorage - energyToMine > 0) {
                // there is more energy to mine the Asteroid is still there
                this.energyStorage -= energyToMine;
                return energyToMine;
            } else {
                int delta = this.energyStorage;
                this.energyStorage = -1;
                this.getField().setEmpty(); // disappear the asteroid
                return delta;
            }
        } else {
            // the Asteroid should not be there if he has no more energy
            this.getField().setEmpty(); // Make sure if the Asteroid will disappear on the Map
            throw new CannotMineException();
        }
    }

    /**
     * Asteroids cannot move on the map, They always throw an {@link CannotMoveException}
     * @param map      The @{@link Map}
     * @param newField the new Field where the meeple should move
     * @return always {@link CannotMoveException}
     * @throws CannotMoveException Returning allays a {@link CannotMineException}
     */
    @Override
    public boolean move(Map map, Field newField) throws CannotMoveException {
        throw new CannotMoveException();
    }

    /**
     *
     * @param pos {@link Field} where the other meeple stands on
     * @throws CannotMoveException Returning allays a {@link CannotMineException}
     */
    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();
    }

    public int getEnergyStorage() {
        return this.energyStorage;
    }
}
