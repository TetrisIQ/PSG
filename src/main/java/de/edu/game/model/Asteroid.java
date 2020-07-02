package de.edu.game.model;

import de.edu.game.StartupRunner;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMineException;
import de.edu.game.exceptions.CannotMoveException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Asteroid extends AbstractMeeple {

    private int energyStorage;

    public Asteroid(Field field) {
        //TODO: Check if this can case Nullpointer erros
        super(StartupRunner.ADMIN, field, ConfigLoader.shared.getAsteroid().getName(), ConfigLoader.shared.getAsteroid().getColor()); // we can make a new empty user, because the Asteroid has no owner
        this.energyStorage = ConfigLoader.shared.getAsteroid().getEnergy();
        this.setDefense("0d0");
        this.setHp(100);

    }


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

    @Override
    public boolean move(Map map, Field newPos) throws CannotMoveException {
        throw new CannotMoveException();
    }

    @Override
    public void attack(Field pos) throws CannotMoveException {
        throw new CannotMoveException();
    }
}
