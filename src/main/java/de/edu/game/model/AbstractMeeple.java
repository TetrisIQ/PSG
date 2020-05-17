package de.edu.game.model;

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

    @OneToOne
    private Coordinate coordinate;

    @OneToOne
    private transient Map map;

    private String name;

    private String username;

    private String color;

    private int hp;

    private String damage;

    private int attackRange;

    public AbstractMeeple(Map map, String username, Coordinate coordinate, String name, String color) {
        this.map = map;
        this.coordinate = coordinate;
        this.name = name;
        this.color = color;
    }

    public List<Field> getFieldsAround(Field field) {
        List<Field> returnList = new LinkedList<>();
        //right
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate()));
        //left
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate()));
        // up
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate() - 1));
        //down
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate(), field.getCoordinate().getYCoordinate() + 1));
        //Diagonal field.getCoordinate()s
        //right up
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate() - 1));
        //right down
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() + 1, field.getCoordinate().getYCoordinate() + 1));
        //left Up
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate() - 1));
        //left down
        returnList.add(this.map.findCoordinate(field.getCoordinate().getXCoordinate() - 1, field.getCoordinate().getYCoordinate() + 1));
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
