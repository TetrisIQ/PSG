package de.edu.game.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model Class to represent a Coordinate
 */
@Entity
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int xCoordinate;
    private int yCoordinate;

    public Coordinate(int id, int xCoordinate, int yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Coordinate() {
    }

    @Override
    public String toString() {
        return this.xCoordinate + " " + this.yCoordinate;
    }

    public int getId() {
        return this.id;
    }

    public int getXCoordinate() {
        return this.xCoordinate;
    }

    public int getYCoordinate() {
        return this.yCoordinate;
    }
}
