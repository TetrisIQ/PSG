package de.edu.game.model;

import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.CannotAttackOwnMeeplesException;
import de.edu.game.exceptions.CannotMoveButIAttackException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import de.edu.game.exceptions.UserNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;

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
            enemy.makeDamage(attack);
            this.makeDamage(defence);
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

    private void addPoints(AbstractMeeple enemy) {
        // add Points for destroyed Enemy
        try {
            int points = 0;
            if (enemy.getName().equals(ConfigLoader.shared.getStarfighter().getName()))
                points = ConfigLoader.shared.getPointsConfig().getDestroyStarfighter();
            if (enemy.getName().equals(ConfigLoader.shared.getSpaceStation().getName()))
                points = ConfigLoader.shared.getPointsConfig().getDestroySpaceStation();
            if (enemy.getName().equals(ConfigLoader.shared.getTransporter().getName()))
                points = ConfigLoader.shared.getPointsConfig().getDestroyTransporter();
            userService.getUserByUsername(this.getUser().getUsername()).addPoints(points);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }
}
