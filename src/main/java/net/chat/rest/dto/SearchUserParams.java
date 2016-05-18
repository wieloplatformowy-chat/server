package net.chat.rest.dto;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

/**
 * @author Mariusz Gorzycki
 * @since 04.05.2016
 */
public class SearchUserParams {
    @ApiModelProperty(position = 0)
    public String name;
    @ApiModelProperty(position = 1)
    public String email;

    public static SearchUserParams fromEntity(UserEntity user) {
        return new SearchUserParams().setName(user.getName()).setEmail(user.getEmail());
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public SearchUserParams setEmail(String email) {
        this.email = email;
        return this;
    }

    public SearchUserParams setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchUserParams that = (SearchUserParams) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, email);
    }
}
