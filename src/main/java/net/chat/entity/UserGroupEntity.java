package net.chat.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Entity
@Table(name = "UserGroups", uniqueConstraints = @UniqueConstraint(columnNames = {"user", "group"}))
@NamedQueries({@NamedQuery(name = "UserGroups.findAll", query = "SELECT u FROM UserGroupEntity u")})
public class UserGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group", referencedColumnName = "id")
    private GroupEntity group;

    public Long getId() {
        return id;
    }

    public UserGroupEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserGroupEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public UserGroupEntity setGroup(GroupEntity group) {
        this.group = group;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user)
                .add("conversation", group)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroupEntity that = (UserGroupEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, group);
    }
}
