package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class UserDto {
    @ApiModelProperty(required = true, position = 0)
    public String name;
    @ApiModelProperty(required = true, position = 1)
    public String email;
    @ApiModelProperty(required = true, position = 2)
    public String password;

    public UserEntity toUserWithNullId() {
        return new UserEntity()
                .setName(name)
                .setEmail(email)
                .setPassword(password);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
