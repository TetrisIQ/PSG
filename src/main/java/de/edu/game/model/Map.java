package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Log
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
        try {
            return rows.get(y).getFields().get(x);
        } catch (NullPointerException nullPointerException) {
            log.info("NullPointer Overflow");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            log.info("ArrayIndexOutOfBounds overflow");
        }
        if (x < 0) {
            x = ConfigLoader.shared.getColumns() - 1;
        }
        if (x >= ConfigLoader.shared.getColumns()) {
            x = 0;
        }
        if (y < 0) {
            y = ConfigLoader.shared.getRows() - 1;
        }
        if (y > ConfigLoader.shared.getRows()) {
            y = 0;
        }
        return findCoordinate(x, y);
    }

}
