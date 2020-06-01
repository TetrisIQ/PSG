package de.edu.game.model;

import de.edu.game.exceptions.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractMeeple {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Field field;

    private String name;

    private String username;

    private String color;

    private int hp;

    private String damage;

    private String defense;

    private int attackRange;

    private boolean hasMoved;

    public AbstractMeeple(String username, Field field, String name, String color) {
        this.field = field;
        this.name = name;
        this.color = color;
        this.username = username;
    }

    @Deprecated
    public Field getField(Map map) {
        return map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate());
    }

    public Field getField() {
        return this.field;
    }

    public boolean canMove(Map map, Field newField) {
        Set<Field> set = new HashSet<>(getFieldsAround(map, this.getField()));
        return set.contains(newField);
    }

    public int makeDamage(int damage) {
        this.hp -= damage;
        return this.hp;
    }

    public List<Field> getFieldsAround(Map map, Field field) {
        List<Field> returnList = new LinkedList<>();
        //right
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate()));
        //left
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate()));
        // up
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate() - 1));
        //down
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate() + 1));
        //Diagonal
        //right up
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate() - 1));
        //right down
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate() + 1));
        //left Up
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate() - 1));
        //left down
        returnList.add(map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate() + 1));
        return returnList;
    }

    abstract public boolean move(Map map, Field newPos) throws HasAlreadyMovedException, SpaceStationCannotMoveException, CannotMoveException, CannotMoveButIAttackException, CannotMineException, CannotAttackOwnMeeplesException;

    abstract public void attack(Field pos) throws CannotMoveException, CannotMoveButIAttackException, CannotAttackOwnMeeplesException;

    @Override
    public String toString() {
        return name + "-" + color;
    }
}
