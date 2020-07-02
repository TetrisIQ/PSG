package de.edu.game.model;

import de.edu.game.exceptions.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Log4j2
public abstract class AbstractMeeple {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Field field;

    private String name;

    @OneToOne
    private User user;

    private String color;

    private int hp;

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

    @Deprecated
    public Field getField(Map map) {
        return map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate());
    }

    public Field getField() {
        return this.field;
    }

    public boolean canMove(Map map, Field newField) {
        return getFieldsAround(map, this.getField()).contains(newField);
    }

    public int makeDamage(int damage) {
        if (damage < 0) {
            log.warn("Cannot make negative Damage!");
            return this.hp;
        }
        this.hp -= damage;
        return this.hp;
    }

    public List<Field> getFieldsAround(Map map, Field field) {
        List<Field> returnList = new LinkedList<>();
        //right
        int xCoordinate = field.getCoordinate().getXCoordinate();
        int yCoordinate = field.getCoordinate().getYCoordinate();
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

    abstract public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, SpaceStationCannotMoveException, CannotMoveException, CannotMoveButIAttackException, CannotMineException, CannotAttackOwnMeeplesException, StorageFullException;

    abstract public void attack(Field pos) throws CannotMoveException, CannotMoveButIAttackException, CannotAttackOwnMeeplesException;

    @Override
    public String toString() {
        return name + "-" + color;
    }
}
