package de.edu.game.controller.responses;

import de.edu.game.model.AbstractMeeple;
import de.edu.game.repositorys.MeepleRepository;

public class MeepleResponse {

    public int id;

    public String name;

    public String color;

    public String username;

    public MeepleResponse(AbstractMeeple meeple) {
        this.id = meeple.getId();
        this.name = meeple.getName();
        this.color = meeple.getColor();
        this.username = meeple.getUsername();
    }
}
