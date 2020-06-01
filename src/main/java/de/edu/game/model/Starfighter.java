package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotAttackOwnMeeplesException;
import de.edu.game.exceptions.CannotMoveButIAttackException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Starfighter extends AbstractMeeple {

    public Starfighter(String username, Field field, String name, String color) {
        super(username, field, name, color);
        this.setAttackRange(1);
        this.setDamage("1w20"); //TODO: load via Config

    }


    @Override
    public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, CannotMoveButIAttackException, CannotAttackOwnMeeplesException {
        if (isHasMoved()) {
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
                //if Position is not empty, a Starfighter will attack
                this.attack(newPos);
            }
            this.setHasMoved(true);
            return true;
        }
        return false;
    }

    @Override
    public void attack(Field pos) throws CannotMoveButIAttackException, CannotAttackOwnMeeplesException {
        // Creating Dices for me and the enemy
        AbstractMeeple enemy = pos.getMeeple();
        if (enemy.getUsername().equals(this.getUsername())) {
            // cannot attack my meeples
            throw new CannotAttackOwnMeeplesException();
        }
        Dice myDice = new Dice(this.getDamage());
        Dice enemyDice = new Dice(enemy.getDefense());
        int attack = myDice.throwDice();
        int defence = enemyDice.throwDice();

        // Exception for SpaceStations
        if (enemy.getName().equals(ConfigLoader.shared.getSpaceStation().getName())) {
            //A SpaceStations has no defence value, they cannot avoid damage
            enemy.makeDamage(attack);
            this.makeDamage(defence);
        } else { // this should be the default case
            int hpEnemy = enemy.makeDamage(attack - defence);
            if (hpEnemy <= 0) {
                // enemy destroyed we can move to the field
                this.getField().setEmpty();
                pos.setMeeple(this);
                this.setField(pos);
            } else {
                // enemy not destroyed, we must attack again.
                throw new CannotMoveButIAttackException();

            }
        }

    }
}
