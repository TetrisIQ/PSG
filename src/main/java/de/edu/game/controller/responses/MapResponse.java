package de.edu.game.controller.responses;

import de.edu.game.model.Map;

import java.util.LinkedList;
import java.util.List;

public class MapResponse {

    public List<RowResponse> rows = new LinkedList<>();

    public String updateMessage;

    public MapResponse(Map map) {
        map.getRows().forEach(r -> this.rows.add(new RowResponse("admin", r)));
    }

    public MapResponse(Map map, String updateMessage) {
        map.getRows().forEach(r -> this.rows.add(new RowResponse("admin", r)));
        this.updateMessage = updateMessage;
    }




}
