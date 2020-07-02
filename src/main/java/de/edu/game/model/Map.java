package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Log4j2
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Row> rows = new ArrayList<>();

    /*@ManyToMany
   Benötigt private List<AbstractMeeple> meeples = new LinkedList<>();
     */

    public Map(int rows, int columns) {
        for (int y = 0; y < rows; y++) {
            this.rows.add(new Row(y, columns));
        }
    }

    public void spawnAsteroids(int amount) {
        for (int i = 0; i < amount; i++) {
            Field f = getRandomEmptyField();
            f.setMeeple(new Asteroid(f));
        }
        log.info("Spawned {} new Asteroids", amount);

    }


    public Field findCoordinate(int x, int y) {
        try {
            return rows.get(y).getFields().get(x);
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder(); // TODO: evaluate if this overhead is necessary
            sb.append(ex.toString()).append("\toverflow, fixing indexing \t[ ");
            int xOld = x;
            int yOld = y;
            // Fixing indexing
            if (x < 0) {
                sb.append("x").append(x).append("->");
                x = ConfigLoader.shared.getColumns() - 1;
                sb.append(x).append(" ");
            }
            if (x >= ConfigLoader.shared.getColumns()) {
                sb.append("x").append(x).append("->");
                x = 0;
                sb.append(x).append(" ");
            }
            if (y < 0) {
                sb.append("y").append(x).append("->");
                y = ConfigLoader.shared.getRows() - 1;
                sb.append(y).append(" ");
            }
            if (y >= ConfigLoader.shared.getRows()) {
                sb.append("y").append(x).append("->");
                y = 0;
                sb.append(y).append(" ");
            }
            sb.append("]");
            log.debug(sb.toString());
            //recursive call with fixed parameters
            if (xOld != x || yOld != y) {
                return findCoordinate(x, y);
            } else {
                // this could not be possible!
                log.warn("Fatal Error in Map.findCoordinate()! {}/{}", x, y);
                ex.printStackTrace();
                System.exit(-1);
                return null;
            }
        }
    }

    public int countAllAsteroids() {
        int counter = 0;
        for (Row r : rows) {
            for (Field f : r.getFields()) {
                if (f.getMeeple() != null && f.getMeeple().getName().equals(ConfigLoader.shared.getAsteroid().getName())) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private Field getRandomField() {
        Random rand = new Random();
        Row row = this.rows.get(rand.nextInt(rows.size())); // get random row
        return row.getFields().get(rand.nextInt(row.getFields().size())); // get random field
    }

    private Field getRandomEmptyField() throws StackOverflowError {
        Field f = getRandomField();
        return f.isEmpty() ? f : getRandomEmptyField(); // recursion call, can be a stackoverflow whey all fields in the map are not empty
    }

}
