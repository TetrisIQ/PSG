package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.exceptions.NoEmptyFieldsException;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.*;

/**
 * Model class containing the most Map logic
 */
@Entity
public class Map {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Map.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Row> rows = new ArrayList<>();

    public Map(int rows, int columns) {
        for (int y = 0; y < rows; y++) {
            this.rows.add(new Row(y, columns));
        }
    }

    public Map() {
    }

    /**
     * Spawns Asteroids on empty random Fields on the Map
     *
     * @param amount the amount of Asteroids witch will Spawned
     */
    public void spawnAsteroids(int amount) {
        for (int i = 0; i < amount; i++) {
            Field f = null;
            try {
                f = getRandomEmptyField();
            } catch (NoEmptyFieldsException e) {
                log.warn("No Empty Fields");
                return;
            }
            f.setMeeple(new Asteroid(f));
        }
        log.info("Spawned {} new Asteroids", amount);

    }

    /**
     * find a coordinate, and handel overflows
     *
     * @param x X Coordinate
     * @param y Y Coordinate
     * @return {@link Field} valid Field of the map
     */
    public Field findCoordinate(int x, int y) {
        try {
            return rows.get(y).getFields().get(x);
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
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

    /**
     * Counts all Asteroids on Map
     *
     * @return the amount of Asteroids on Map
     */
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

    /**
     * Get a random Field on Map
     *
     * @return a random @{@link Field}
     */
    private Field getRandomField() {
        Random rand = new Random();
        Row row = this.rows.get(rand.nextInt(rows.size())); // get random row
        return row.getFields().get(rand.nextInt(row.getFields().size())); // get random field
    }

    /**
     * Get a random empty Field on Map
     *
     * @return A random empty {@link Field}
     * @throws NoEmptyFieldsException If there are no Empty Fields on Map
     */
    private Field getRandomEmptyField() throws NoEmptyFieldsException {
        final Set<Field> fields = new HashSet<>();
        Field field = getRandomField();
        final int maxFields = getFieldCount();
        while (!field.isEmpty()) {
            if (fields.size() == maxFields) {
                throw new NoEmptyFieldsException();
            }
            field = getRandomField();
            fields.add(field);
        }
        return field;
    }

    /**
     * Counts all fields on map
     *
     * @return The amount of Fields on Map
     */
    public int getFieldCount() {
        return rows.get(0).getFields().size() * rows.size();
    }

    public int getId() {
        return this.id;
    }

    public List<Row> getRows() {
        return this.rows;
    }

    public String toString() {
        return "Map(id=" + this.getId() + ", rows=" + this.getRows() + ")";
    }
}
