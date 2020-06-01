package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMineException;
import de.edu.game.exceptions.CannotMoveException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Asteroid extends AbstractMeeple {

    private int energy;

    public Asteroid(Field field) {
        super("admin", field, ConfigLoader.shared.getAsteroid().getName(), ConfigLoader.shared.getAsteroid().getColor());
        this.energy = ConfigLoader.shared.getAsteroid().getEnergy();

    }


    public int mine(int energyToMine) throws CannotMineException {
        if (this.energy > 0) {
            if (this.energy - energyToMine > 0) {
                // there is more energy to mine the Asteroid is still there
                this.energy -= energyToMine;
                return energyToMine;
            } else {
                int delta = this.energy;
                this.energy = -1;
                this.getField().setEmpty(); // disappear the asteroid
                return delta;
            }
        } else {
            // the Asteroid shoulder be there if he has no more energy
            this.getField().setEmpty(); // Make sure if the Asteroid will disappear on the Map
            throw new CannotMineException();
        }
    }

    @Override
    public boolean move(Map map, Field newPos) throws CannotMoveException {
        throw new CannotMoveException();
    }

    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();
    }
}
