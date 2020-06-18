package de.edu.game.controller.responses;

import de.edu.game.model.Coordinate;
import de.edu.game.model.Field;
import lombok.AllArgsConstructor;

public class FieldResponse {

    public int id;

    public Coordinate coordinate;

    public MeepleResponse meeple;

    public boolean empty;

    public FieldResponse(String username, Field field) {
        this.id = field.getId();
        this.coordinate = field.getCoordinate();
        this.empty = field.isEmpty();
        if(!empty) {
            this.meeple = new MeepleResponse(username, field.getMeeple());
        }
    }

}
