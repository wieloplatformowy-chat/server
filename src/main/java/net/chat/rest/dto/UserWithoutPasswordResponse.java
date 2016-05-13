package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class UserWithoutPasswordResponse {
    @ApiModelProperty(required = true, position = 0)
    public Long id;
    @ApiModelProperty(required = true, position = 1)
    public String name;
    @ApiModelProperty(required = true, position = 2)
    public String email;

    public static UserWithoutPasswordResponse fromEntity(UserEntity user) {
        return new UserWithoutPasswordResponse().setId(user.getId()).setName(user.getName()).setEmail(user.getEmail());
    }

    @Override
    public String toString() {
        return "UserWithoutPasswordResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public UserWithoutPasswordResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public UserWithoutPasswordResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserWithoutPasswordResponse setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWithoutPasswordResponse that = (UserWithoutPasswordResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
