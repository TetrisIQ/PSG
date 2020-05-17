package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sun.awt.image.ImageWatched;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@Getter
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Row> rows = new LinkedList<>();

    /*@ManyToMany
    private List<AbstractMeeple> meeples = new LinkedList<>();
     */

    public Map(int rows, int columns) {
        for (int y = 0; y < rows; y++) {
            this.rows.add(new Row(y, columns));
        }
    }

    public Field findCoordinate(int x, int y) {
        //TODO: index out of bounce, goto bottom of the map
        return rows.get(y).getFields().get(x);
    }

}
