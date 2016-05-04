package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.UserEntity;

import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 04.05.2016
 */
public class IdDto {
    @ApiModelProperty(required = true)
    public Long id;

    public static IdDto fromEntity(UserEntity user) {
        return new IdDto().setId(user.getId());
    }

    @Override
    public String toString() {
        return "UserWithoutPasswordDto{" +
                "id=" + id +
                '}';
    }

    public IdDto setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdDto that = (IdDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
