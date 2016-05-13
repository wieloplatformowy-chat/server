package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class LoginParams {
    @ApiModelProperty(required = true, position = 0)
    public String name;
    @ApiModelProperty(required = true, position = 1)
    public String password;

    public static LoginParams fromEntity(UserEntity user) {
        return new LoginParams().setName(user.getName()).setPassword(user.getPassword());
    }

    public static LoginParams fromUserDto(RegisterParams user) {
        return new LoginParams().setName(user.name).setPassword(user.password);
    }

    public UserEntity toUserWithNullId() {
        return new UserEntity()
                .setName(name)
                .setPassword(password);
    }

    @Override
    public String toString() {
        return "RegisterParams{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LoginParams setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginParams setName(String name) {
        this.name = name;
        return this;
    }
}
