package net.chat.rest.dto;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 24.05.2016
 */
public class RenameGroupParams {
    @ApiModelProperty(position = 0)
    public Long groupId;
    @ApiModelProperty(position = 1)
    public String newName;

    public Long getGroupId() {
        return groupId;
    }

    public RenameGroupParams setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getNewName() {
        return newName;
    }

    public RenameGroupParams setNewName(String newName) {
        this.newName = newName;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("newName", newName)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenameGroupParams that = (RenameGroupParams) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(newName, that.newName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, newName);
    }
}
