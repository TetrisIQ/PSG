package de.edu.game.controller.responses;

import de.edu.game.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    @ApiModelProperty(value = "TetrisIQ")
    private String username;
    @ApiModelProperty("doNotUsePassword")
    private String clearPassword;
    @ApiModelProperty("red")
    private String color;
    @ApiModelProperty("READY")
    private String state;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.color = user.getColor();
        this.state = user.getState().toString();
    }
}
