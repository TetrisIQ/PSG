package de.edu.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "rows") //row is an SQL key words
@NoArgsConstructor
@ToString
@Getter
public class Row {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Field> fields = new LinkedList<>();

    public Row(int y, int length) {
        for (int x = 0; x < length; x++) {
            fields.add(new Field(x, y));
        }
    }
}
