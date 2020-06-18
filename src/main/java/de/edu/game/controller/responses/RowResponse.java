package de.edu.game.controller.responses;

import de.edu.game.model.Row;

import java.util.LinkedList;
import java.util.List;

public class RowResponse {

    public List<FieldResponse> fields = new LinkedList<>();

    public RowResponse(String username, Row row) {
        row.getFields().forEach(f -> this.fields.add(new FieldResponse(username, f)));
    }
}
