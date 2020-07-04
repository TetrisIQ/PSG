package de.edu.game.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Model Class of a Field on the @{@link Map}
 */
@Entity
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Coordinate coordinate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AbstractMeeple meeple;

    public Field(int x, int y) {
        this.coordinate = new Coordinate(0, x, y);
    }

    public Field() {
    }

    public void setMeeple(AbstractMeeple meeple) {
        this.meeple = meeple;
    }

    public boolean isEmpty() {
        return meeple == null;
    }

    public void setEmpty() {
        this.meeple = null;
    }

    @Override
    public String toString() {
        return "[" + coordinate.getXCoordinate() + "/" + coordinate.getYCoordinate() + "-" + meeple + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate.getXCoordinate(), coordinate.getYCoordinate());
    }

    public int getId() {
        return this.id;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public AbstractMeeple getMeeple() {
        return this.meeple;
    }
}
