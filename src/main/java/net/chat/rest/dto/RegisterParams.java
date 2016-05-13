package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class RegisterParams {
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
        return "RegisterParams{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public RegisterParams setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterParams setName(String name) {
        this.name = name;
        return this;
    }

    public RegisterParams setEmail(String email) {
        this.email = email;
        return this;
    }
}
