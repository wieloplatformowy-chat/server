package net.chat.rest.dto;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
public class CreateGroupResponse {
    @ApiModelProperty(required = true)
    private Long id;

    public static CreateGroupResponse with(Long id) {
        return new CreateGroupResponse().setId(id);
    }

    public Long getId() {
        return id;
    }

    public CreateGroupResponse setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
