package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
public class SpaceStation extends AbstractMeeple {

    private static final Logger logger = LoggerFactory.getLogger(SpaceStation.class);

    public SpaceStation(Map map, String username, Coordinate coordinate, String name, String color) {
        super(map, username, coordinate, name, color);
        this.setHp(ConfigLoader.shared.getSpaceStationHq());
        this.setDamage(ConfigLoader.shared.getSpaceStationDamage());
        this.setAttackRange(ConfigLoader.shared.getSpaceStationAttackRange());
    }

    public boolean spawnTransporter(User user) {
        try {
            List<Field> freeFields = findFreeFields();
            Collections.shuffle(freeFields);
            AbstractMeeple starfighter = new Transporter(this.getMap(), this.getUsername(), freeFields.get(0).getCoordinate(), "Transporter", this.getColor());
            freeFields.get(0).setMeeple(starfighter);
            user.addMeeple(starfighter);
            return true;

        } catch (IndexOutOfBoundsException ex) {
            // No empty fields to spawn Meeples
            logger.warn("Cannot Spawn meeple, no space!");
            return false;
        }
    }

    public boolean spawnStarfighter(User user) {
        try {
            List<Field> freeFields = findFreeFields();
            Collections.shuffle(freeFields);
            AbstractMeeple starfighter = new Starfighter(this.getMap(), this.getUsername(), freeFields.get(0).getCoordinate(), "Starfighter", this.getColor());
            freeFields.get(0).setMeeple(starfighter);
            user.addMeeple(starfighter);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            // No empty fields to spawn Meeples
            return false;
        }
    }

    private List<Field> findFreeFields() {
        List<Field> returnList = new LinkedList<>();
        Coordinate spaceStationCoordinate = this.getCoordinate();
        List<Field> ls = this.getFieldsAround(this.getMap().findCoordinate(spaceStationCoordinate.getXCoordinate(), spaceStationCoordinate.getYCoordinate()));
        for (Field field : ls) {
            if (field.isEmpty()) {
                returnList.add(field);
            }
        }
        return returnList;
    }

    @Override
    public boolean move(Field newPos) {
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
