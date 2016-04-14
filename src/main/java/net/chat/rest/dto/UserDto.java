package net.chat.rest.dto;

import io.swagger.annotations.ApiParam;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class UserDto {
    @ApiParam(required = true)
    public String name;
    @ApiParam(required = true)
    public String password;

    public UserEntity toUserWithNullId() {
        return new UserEntity()
                .setName(name)
                .setPassword(password);
    }

    @Override public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
