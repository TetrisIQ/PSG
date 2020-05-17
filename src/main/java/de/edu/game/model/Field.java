package de.edu.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinate coordinate;

    public Field(int x, int y) {
        this.coordinate = new Coordinate(0, x, y);
    }

    @OneToOne(cascade = CascadeType.ALL)
    private AbstractMeeple meeple;

    public void setMeeple(AbstractMeeple meeple) {
        this.meeple = meeple;
    }


    @Override
    public String toString() {
        return "[" + coordinate.getXCoordinate() + "/" + coordinate.getYCoordinate() + "-" + meeple + "]";
    }

    public boolean isEmpty() {
        return meeple == null;
    }
}
