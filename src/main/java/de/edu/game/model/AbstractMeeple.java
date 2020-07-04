package de.edu.game.model;

import de.edu.game.exceptions.*;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract meeple Class
 */
@Entity
public abstract class AbstractMeeple {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(AbstractMeeple.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Field field;

    private String name;

    @OneToOne
    private User user;

    private String color;

    private int shieldEnergy;

    private String damage;

    private String defense;

    private int attackRange;

    private boolean hasMoved;

    public AbstractMeeple(User user, Field field, String name, String color) {
        this.field = field;
        this.name = name;
        this.color = color;
        this.user = user;
    }

    public AbstractMeeple() {
    }

    /**
     * Get the Field of the meeple over the @{@link Map}
     *
     * @param map {@link Map}
     * @return {@link Field} where the meeple stands on
     */
    @Deprecated
    public Field getField(Map map) {
        return map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate());
    }

    /**
     * Get the Field where the meeple stands on
     *
     * @return {@link Field} where the meeple stands on
     */
    public Field getField() {
        return this.field;
    }

    /**
     * Checks if the meeple con move to a field
     *
     * @param map      The @{@link Map}
     * @param newField The @{@link Field} where the meeple wants to move to
     * @return True if the meeple can move to the {@link Field} <br>
     * False if the meeple cannot move to the @{@link Field}
     */
    public boolean canMove(Map map, Field newField) {
        return getFieldsAround(map).contains(newField);
    }

    /**
     * Make damage on the meeple
     *
     * @param damage amount of shieldEnergy, the meeple should lose
     * @return the rest of the shieldEnergy, the meeple has
     */
    public int makeDamage(int damage) {
        if (damage < 0) {
            log.warn("Cannot make negative Damage!");
            return this.shieldEnergy;
        }
        this.shieldEnergy -= damage;
        return this.shieldEnergy;
    }

    /**
     * Get the fields around the meeple
     *
     * @param map The @{@link Map}
     * @return A List of @{@link Field}s around the meeple
     */
    public List<Field> getFieldsAround(Map map) {
        List<Field> returnList = new LinkedList<>();
        //right
        int xCoordinate = this.field.getCoordinate().getXCoordinate();
        int yCoordinate = this.field.getCoordinate().getYCoordinate();
        returnList.add(map.findCoordinate(xCoordinate + 1, yCoordinate));
        //left
        returnList.add(map.findCoordinate(xCoordinate - 1, yCoordinate));
        // up
        returnList.add(map.findCoordinate(xCoordinate, yCoordinate - 1));
        //down
        returnList.add(map.findCoordinate(xCoordinate, yCoordinate + 1));
        //Diagonal
        //right up
        returnList.add(map.findCoordinate(xCoordinate + 1, yCoordinate - 1));
        //right down
        returnList.add(map.findCoordinate(xCoordinate + 1, yCoordinate + 1));
        //left Up
        returnList.add(map.findCoordinate(xCoordinate - 1, yCoordinate - 1));
        //left down
        returnList.add(map.findCoordinate(xCoordinate - 1, yCoordinate + 1));
        return returnList;
    }

    /**
     * Move a meeple to the new {@link Field} if possible
     * @param map      The @{@link Map}
     * @param newField the new Field where the meeple should move
     * @return True if the meeple has moved <br>
     * False if the meeple has not moved
     * @throws SpaceStationCannotMoveException If the meeple is an SpaceStation it cannot move
     * @throws HasAlreadyMovedException        If the meeple has already moved this round, it cannot move again
     * @throws CannotMoveButIAttackException   If the meeple ({@link Starfighter}) has attack an other meeple, but don't destroy it. It cannot move, and cannot move again this round
     * @throws CannotMoveException             The meeple cannot move to this point, but can move to another this round
     * @throws StorageFullException            The meeple (@{@link Transporter}) cannot mine more energy, because the storage is full
     * @throws CannotAttackOwnMeeplesException The meeple (@{@link Starfighter}) cannot attack own meeples, but can move to an other field this round
     * @throws CannotMineException             The meeple (@{@link Transporter}) cannot mine here
     */
    abstract public boolean move(Map map, Field newField) throws HasAlreadyMovedException, SpaceStationCannotMoveException, CannotMoveException, CannotMoveButIAttackException, CannotMineException, CannotAttackOwnMeeplesException, StorageFullException;

    /**
     * Attack an other meeple on the Field
     * @param pos {@link Field} where the other meeple stands on
     * @throws CannotMoveException             The meeple cannot move to this point, but can move to another this round
     * @throws CannotMoveButIAttackException   If the meeple ({@link Starfighter}) has attack an other meeple, but don't destroy it. It cannot move, and cannot move again this round
     * @throws CannotAttackOwnMeeplesException The meeple (@{@link Starfighter}) cannot attack own meeples, but can move to an other field this round
     */
    abstract public void attack(Field pos) throws CannotMoveException, CannotMoveButIAttackException, CannotAttackOwnMeeplesException;

    @Override
    public String toString() {
        return name + "-" + color;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public User getUser() {
        return this.user;
    }

    public String getColor() {
        return this.color;
    }

    public int getShieldEnergy() {
        return this.shieldEnergy;
    }

    public String getDamage() {
        return this.damage;
    }

    public String getDefense() {
        return this.defense;
    }

    public int getAttackRange() {
        return this.attackRange;
    }

    public boolean isHasMoved() {
        return this.hasMoved;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setShieldEnergy(int shieldEnergy) {
        this.shieldEnergy = shieldEnergy;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
