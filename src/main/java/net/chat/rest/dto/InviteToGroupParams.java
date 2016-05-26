package net.chat.rest.dto;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
public class InviteToGroupParams {
    @ApiModelProperty(position = 0)
    public Long groupId;
    @ApiModelProperty(position = 1)
    public Long[] userIds;

    public Long getGroupId() {
        return groupId;
    }

    public InviteToGroupParams setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public InviteToGroupParams setUserIds(Long... userIds) {
        this.userIds = userIds;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("userIds", userIds)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InviteToGroupParams that = (InviteToGroupParams) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(userIds, that.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userIds);
    }
}
