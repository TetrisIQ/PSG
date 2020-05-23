package de.edu.game.model;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int xCoordinate;
    private int yCoordinate;

    @Override
    public String toString() {
        return this.xCoordinate + " " + this.yCoordinate;
    }
}
