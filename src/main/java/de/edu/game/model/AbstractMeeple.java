package de.edu.game.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

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

    private int attackRange;

    public AbstractMeeple(String username, Field field, String name, String color) {
        this.field = field;
        this.name = name;
        this.color = color;
        this.username = username;
    }

    public Field getField(Map map) {
        return map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate());
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
        //Diagonal field.getCoordinate()s
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

    abstract public boolean move(Field newPos);

    abstract public int nextPossibleMoves();

    abstract public void attack(Field pos);

    @Override
    public String toString() {
        return name + "-" + color;
    }
}
