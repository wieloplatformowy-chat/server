package net.chat.rest.dto;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;
import io.swagger.annotations.ApiModelProperty;
import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;

import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 03.04.2016
 */
public class ConversationResponse {
    @ApiModelProperty(required = true, position = 0)
    private Long id;
    @ApiModelProperty(required = true, position = 1)
    private String name;
    @ApiModelProperty(required = true, position = 2)
    private boolean group;
    @ApiModelProperty(required = true, position = 3)
    private List<UserResponse> users;

    public static ConversationResponse fromEntity(ConversationEntity conversation) {
        return new ConversationResponse()
                .setId(conversation.getId())
                .setName(conversation.getName())
                .setGroup(conversation.isGroup())
                .setUsersFromEntity(conversation.getUsers());
    }

    public Long getId() {
        return id;
    }

    public ConversationResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConversationResponse setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isGroup() {
        return group;
    }

    public ConversationResponse setGroup(boolean group) {
        this.group = group;
        return this;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    private ConversationResponse setUsersFromEntity(List<UserEntity> users) {
        this.users = FluentIterable.from(users).transform(new Function<UserEntity, UserResponse>() {
            @Override
            public UserResponse apply(UserEntity input) {
                return UserResponse.fromEntity(input);
            }
        }).toList();
        return this;
    }

    public ConversationResponse setUsers(List<UserResponse> users) {
        this.users = users;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("group", group)
                .add("users", users)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationResponse that = (ConversationResponse) o;
        return group == that.group &&
                java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, group, users);
    }
}
