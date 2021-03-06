package de.edu.game.model;

import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotAttackOwnMeeplesException;
import de.edu.game.exceptions.CannotMoveButIAttackException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;

/**
 * Model Class witch represents a Starfighter
 */
@Entity
@NoArgsConstructor
public class Starfighter extends AbstractMeeple {

    @Autowired
    private transient UserService userService;

    public Starfighter(User user, Field field, String name, String color) {
        super(user, field, name, color);
        this.setAttackRange(ConfigLoader.shared.getStarfighter().getAttackRange());
        this.setDamage(ConfigLoader.shared.getStarfighter().getDamage());
        this.setDefense(ConfigLoader.shared.getStarfighter().getDefense());
    }

    /**
     * Move the {@link Starfighter} on the Map
     *
     * @param map      The @{@link Map}
     * @param newField the new Field where the meeple should move
     * @return True if the Starfighter has moved
     * @throws HasAlreadyMovedException        If the meeple has already moved this round, it cannot move again
     * @throws CannotMoveButIAttackException   If the meeple ({@link Starfighter}) has attack an other meeple, but don't destroy it. It cannot move, and cannot move again this round
     * @throws CannotAttackOwnMeeplesException The meeple (@{@link Starfighter}) cannot attack own meeples, but can move to an other field this round
     */
    @Override
    public boolean move(Map map, Field newField) throws HasAlreadyMovedException, CannotMoveButIAttackException, CannotAttackOwnMeeplesException {
        if (isHasMoved()) {
            throw new HasAlreadyMovedException();
        }
        //checks if the Coordinate of the new Position is next to the meeple
        if (this.canMove(map, newField)) {
            //check if new Position is empty
            if (newField.isEmpty()) {
                this.getField().setEmpty();
                newField.setMeeple(this);
                this.setField(newField);
            } else {
                //if Position is not empty, a Starfighter will attack
                this.attack(newField);
            }
            this.setHasMoved(true);
            return true;
        }
        return false;
    }

    /**
     * Attack another meeple on the Field
     *
     * @param pos {@link Field} where the other meeple stands on
     * @throws CannotMoveButIAttackException   If the meeple ({@link Starfighter}) has attack an other meeple, but don't destroy it. It cannot move, and cannot move again this round
     * @throws CannotAttackOwnMeeplesException The meeple (@{@link Starfighter}) cannot attack own meeples, but can move to an other field this round
     */
    @Override
    public void attack(Field pos) throws CannotMoveButIAttackException, CannotAttackOwnMeeplesException {
        // Creating Dices for me and the enemy
        AbstractMeeple enemy = pos.getMeeple();
        if (enemy.getUser().getUsername().equals(this.getUser().getUsername())) {
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
            int spaceStationHp = enemy.makeDamage(attack);
            this.makeDamage(defence);
            if(spaceStationHp <= 0) {
                // SpaceStation is destroyed
                this.getField().setEmpty();
                pos.setMeeple(this);
                this.setField(pos);
                addPoints(enemy);
                // remove all other meeple
                enemy.getUser().removeAllMeeple();
            }
        } else { // this should be the default case
            if (attack < defence) {
                if (!(enemy.getName().equals(ConfigLoader.shared.getTransporter().getName()))) {
                    // defence is better so attacker get damage
                    this.makeDamage(defence - attack);
                }
            } else {
                int hpEnemy = enemy.makeDamage(attack - defence);
                if (hpEnemy <= 0) {
                    // enemy destroyed we can move to the field
                    this.getField().setEmpty();
                    pos.setMeeple(this);
                    this.setField(pos);
                    addPoints(enemy);
                } else {
                    // enemy not destroyed, we must attack again.
                    throw new CannotMoveButIAttackException();
                }
            }
        }
    }

    /**
     * Add VictoryPoints to the user how destroy the meeple
     *
     * @param enemy The Enemy witch is destroyed
     */
    private void addPoints(AbstractMeeple enemy) {
        // add Points for destroyed Enemy
        int points = 0;
        if (enemy.getName().equals(ConfigLoader.shared.getStarfighter().getName()))
            points = ConfigLoader.shared.getPointsConfig().getDestroyStarfighter();
        if (enemy.getName().equals(ConfigLoader.shared.getSpaceStation().getName()))
            points = ConfigLoader.shared.getPointsConfig().getDestroySpaceStation();
        if (enemy.getName().equals(ConfigLoader.shared.getTransporter().getName()))
            points = ConfigLoader.shared.getPointsConfig().getDestroyTransporter();
        this.getUser().addPoints(points);
    }
}
