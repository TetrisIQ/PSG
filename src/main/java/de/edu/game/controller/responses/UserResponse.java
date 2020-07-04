package de.edu.game.controller.responses;

import de.edu.game.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
