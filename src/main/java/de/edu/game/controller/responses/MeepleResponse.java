package de.edu.game.controller.responses;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.model.AbstractMeeple;
import de.edu.game.model.SpaceStation;
import de.edu.game.model.Transporter;

/**
 * Response Object for the @{@link AbstractMeeple} <br> and @{@link Transporter}, @{@link de.edu.game.model.Starfighter}, @{@link de.edu.game.model.Asteroid}, @{@link SpaceStation}
 */
public class MeepleResponse {

    public int id;

    public String name;

    public String color;

    public String username;

    public int energieStorage = -1;

    public MeepleResponse(String username, AbstractMeeple meeple) {
        this.id = meeple.getId();
        this.name = meeple.getName();
        this.color = meeple.getColor();
        this.username = meeple.getUser().getUsername();
        if (meeple.getName().equals(ConfigLoader.shared.getTransporter().getName())) { //checks if meeple is a transporter
            if (username.equals(meeple.getUser().getUsername())) { // Checks if the meeple is from the same player
                this.energieStorage = ((Transporter) meeple).getStorage();
            }
        }
        if(meeple.getName().equals(ConfigLoader.shared.getSpaceStation().getName())) {
            if (username.equals(meeple.getUser().getUsername())) { // Checks if the meeple is from the same player
                this.energieStorage = ((SpaceStation) meeple).getStorage();
            }
        }
    }
}
