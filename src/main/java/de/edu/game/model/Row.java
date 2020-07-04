package de.edu.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model Class for Rows on the {@link Map}
 */
@Entity(name = "rows") //row is an SQL key words
@NoArgsConstructor
@ToString
@Getter
public class Row {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Field> fields = new ArrayList<>();

    public Row(int y, int length) {
        for (int x = 0; x < length; x++) {
            fields.add(new Field(x, y));
        }
    }
}
