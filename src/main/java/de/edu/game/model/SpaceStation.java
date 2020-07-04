package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.SpaceStationCannotMoveException;
import org.apache.logging.log4j.Logger;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Model calls witch represents a SpaceStation
 */
@Entity
public class SpaceStation extends AbstractMeeple {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(SpaceStation.class);
    private int storage = 520; // when starting the game // 2x100 for Transporter 1x120 for Starfighter -> 200 rest


    public SpaceStation(User user, Field field, String color) {
        super(user, field, ConfigLoader.shared.getSpaceStation().getName(), color);
        this.setShieldEnergy(ConfigLoader.shared.getSpaceStation().getHp());
        this.setDamage(ConfigLoader.shared.getSpaceStation().getDamage());
        this.setAttackRange(ConfigLoader.shared.getSpaceStation().getAttackRange());
    }

    public SpaceStation() {
    }

    /**
     * Spawn a new {@link Transporter} on the {@link Map}
     *
     * @param map The {@link Map}
     * @return True if the new {@link Transporter} has been spawned
     */
    public boolean spawnTransporter(Map map) {
        if (this.storage >= ConfigLoader.shared.getTransporter().getCoasts()) {
            try {
                List<Field> freeFields = findFreeFields(map);
                Collections.shuffle(freeFields);
                AbstractMeeple starfighter = new Transporter(this.getUser(), freeFields.get(0), "Transporter", this.getColor());
                freeFields.get(0).setMeeple(starfighter);
                this.getUser().addMeeple(starfighter);
                this.getUser().addPoints(ConfigLoader.shared.getPointsConfig().getCreateTransporter());
                this.storage -= ConfigLoader.shared.getTransporter().getCoasts();

                return true;
            } catch (IndexOutOfBoundsException ex) {
                // No empty fields to spawn Meeples
                log.warn("Cannot Spawn meeple, no space!");
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Spawn a new {@link Starfighter} on the {@link Map}
     *
     * @param map The {@link Map}
     * @return True if the new {@link Starfighter} has been spawned
     */
    public boolean spawnStarfighter(Map map) {
        if (this.storage >= ConfigLoader.shared.getStarfighter().getCoasts()) {
            try {
                List<Field> freeFields = findFreeFields(map);
                Collections.shuffle(freeFields);
                AbstractMeeple starfighter = new Starfighter(this.getUser(), freeFields.get(0), "Starfighter", this.getColor());
                freeFields.get(0).setMeeple(starfighter);
                this.getUser().addMeeple(starfighter);
                this.getUser().addPoints(ConfigLoader.shared.getPointsConfig().getCreateStarfighter());
                this.storage -= ConfigLoader.shared.getStarfighter().getCoasts();
                return true;
            } catch (IndexOutOfBoundsException ex) {
                // No empty fields to spawn Meeples
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Find a free Field around the SpaceStation
     *
     * @param map The {@link Map}
     * @return A List of {@link Field}s around the {@link SpaceStation}
     */
    private List<Field> findFreeFields(Map map) {
        List<Field> returnList = new LinkedList<>();
        List<Field> ls = this.getFieldsAround(map);
        for (Field field : ls) {
            if (field.isEmpty()) {
                returnList.add(field);
            }
        }
        return returnList;
    }

    /**
     * Deploy energy to the {@link SpaceStation}
     *
     * @param energy the amount of Energy to deploy
     */
    public void addEnergy(int energy) {
        this.storage += energy;
    }


    /**
     * Space Stations cannot move, They always throw an @{@link SpaceStationCannotMoveException}
     *
     * @param map      The @{@link Map}
     * @param newField the new Field where the meeple should move
     * @return always @{@link CannotMoveException}
     * @throws SpaceStationCannotMoveException Returning allays a {@link SpaceStationCannotMoveException}
     */
    @Override
    public boolean move(Map map, Field newField) throws SpaceStationCannotMoveException {
        throw new SpaceStationCannotMoveException();
    }

    @Override
    public void attack(Field pos) {

    }

    public int getStorage() {
        return this.storage;
    }
}
