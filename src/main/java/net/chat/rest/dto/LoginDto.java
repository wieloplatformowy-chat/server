package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class LoginDto {
    @ApiModelProperty(required = true, position = 0)
    public String name;
    @ApiModelProperty(required = true, position = 1)
    public String password;

    public static LoginDto fromEntity(UserEntity user) {
        return new LoginDto().setName(user.getName()).setPassword(user.getPassword());
    }

    public static LoginDto fromUserDto(UserDto user) {
        return new LoginDto().setName(user.name).setPassword(user.password);
    }

    public UserEntity toUserWithNullId() {
        return new UserEntity()
                .setName(name)
                .setPassword(password);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LoginDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginDto setName(String name) {
        this.name = name;
        return this;
    }
}
