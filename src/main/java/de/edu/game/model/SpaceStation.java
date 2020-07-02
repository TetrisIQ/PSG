package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.SpaceStationCannotMoveException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Log4j2
@Getter
public class SpaceStation extends AbstractMeeple {

    private int storage = 520; // when starting the game // 2x100 for Transporter 1x120 for Starfighter -> 200 rest


    public SpaceStation(User user, Field field, String color) {
        super(user, field, ConfigLoader.shared.getSpaceStation().getName(), color);
        this.setHp(ConfigLoader.shared.getSpaceStation().getHp());
        this.setDamage(ConfigLoader.shared.getSpaceStation().getDamage());
        this.setAttackRange(ConfigLoader.shared.getSpaceStation().getAttackRange());
    }

    public boolean spawnTransporter(Map map, User user) {
        if (this.storage >= ConfigLoader.shared.getTransporter().getCoasts()) {
            try {
                List<Field> freeFields = findFreeFields(map);
                Collections.shuffle(freeFields);
                AbstractMeeple starfighter = new Transporter(this.getUser(), freeFields.get(0), "Transporter", this.getColor());
                freeFields.get(0).setMeeple(starfighter);
                user.addMeeple(starfighter);
                user.addPoints(ConfigLoader.shared.getPointsConfig().getCreateTransporter());
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

    public boolean spawnStarfighter(Map map, User user) {
        if (this.storage >= ConfigLoader.shared.getStarfighter().getCoasts()) {
            try {
                List<Field> freeFields = findFreeFields(map);
                Collections.shuffle(freeFields);
                AbstractMeeple starfighter = new Starfighter(this.getUser(), freeFields.get(0), "Starfighter", this.getColor());
                freeFields.get(0).setMeeple(starfighter);
                user.addMeeple(starfighter);
                user.addPoints(ConfigLoader.shared.getPointsConfig().getCreateStarfighter());
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
