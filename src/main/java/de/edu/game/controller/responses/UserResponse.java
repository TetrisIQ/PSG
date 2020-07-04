package de.edu.game.controller.responses;

import de.edu.game.model.User;

/**
 * Response Object for the @{@link User}
 */
public class UserResponse {
    private String username;
    private String clearPassword;
    private String color;
    private String state;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.color = user.getColor();
        this.state = user.getState().toString();
    }

    public UserResponse(String username, String clearPassword, String color, String state) {
        this.username = username;
        this.clearPassword = clearPassword;
        this.color = color;
        this.state = state;
    }

    public String getUsername() {
        return this.username;
    }

    public String getClearPassword() {
        return this.clearPassword;
    }

    public String getColor() {
        return this.color;
    }

    public String getState() {
        return this.state;
    }
}
