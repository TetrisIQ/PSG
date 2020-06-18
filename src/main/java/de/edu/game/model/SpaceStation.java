package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.SpaceStationCannotMoveException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Log
@Getter
public class SpaceStation extends AbstractMeeple {

    private int storage = 0;


    public SpaceStation(Map map, String username, Field field, String color) {
        super(username, field, ConfigLoader.shared.getSpaceStation().getName(), color);
        this.setHp(ConfigLoader.shared.getSpaceStation().getHp());
        this.setDamage(ConfigLoader.shared.getSpaceStation().getDamage());
        this.setAttackRange(ConfigLoader.shared.getSpaceStation().getAttackRange());
    }

    public boolean spawnTransporter(Map map, User user) {
        try {
            List<Field> freeFields = findFreeFields(map);
            Collections.shuffle(freeFields);
            AbstractMeeple starfighter = new Transporter(this.getUsername(), freeFields.get(0), "Transporter", this.getColor());
            freeFields.get(0).setMeeple(starfighter);
            user.addMeeple(starfighter);
            user.addPoints(ConfigLoader.shared.getPointsConfig().getCreateTransporter());
            return true;

        } catch (IndexOutOfBoundsException ex) {
            // No empty fields to spawn Meeples
            log.warning("Cannot Spawn meeple, no space!");
            return false;
        }
    }

    public boolean spawnStarfighter(Map map, User user) {
        try {
            List<Field> freeFields = findFreeFields(map);
            Collections.shuffle(freeFields);
            AbstractMeeple starfighter = new Starfighter(this.getUsername(), freeFields.get(0), "Starfighter", this.getColor());
            freeFields.get(0).setMeeple(starfighter);
            user.addMeeple(starfighter);
            user.addPoints(ConfigLoader.shared.getPointsConfig().getCreateStarfighter());
            return true;
        } catch (IndexOutOfBoundsException ex) {
            // No empty fields to spawn Meeples
            return false;
        }
    }

    private List<Field> findFreeFields(Map map) {
        List<Field> returnList = new LinkedList<>();
        Coordinate spaceStationCoordinate = this.getField().getCoordinate();
        List<Field> ls = this.getFieldsAround(map, map.findCoordinate(spaceStationCoordinate.getXCoordinate(), spaceStationCoordinate.getYCoordinate()));
        for (Field field : ls) {
            if (field.isEmpty()) {
                returnList.add(field);
            }
        }
        return returnList;
    }

    public void addEnergy(int energie) {
        this.storage += energie;
    }

    @Override
    public boolean move(Map map, Field newPos) throws SpaceStationCannotMoveException {
        throw new SpaceStationCannotMoveException();
    }

    @Override
    public void attack(Field pos) {

    }
}
