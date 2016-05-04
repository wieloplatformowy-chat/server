package net.chat.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Friends", uniqueConstraints = @UniqueConstraint(columnNames = {"user", "friend"}))
@NamedQueries({@NamedQuery(name = "Friend.findAll", query = "SELECT f FROM FriendEntity f"),
        @NamedQuery(name = "Friend.isFriend", query = "SELECT f FROM FriendEntity f WHERE f.user = :user and f.friend = :friend"),
        @NamedQuery(name = "Friend.findFriends", query = "SELECT u FROM FriendEntity f, UserEntity u WHERE f.user = :user and f.friend = u")})
public class FriendEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "friend", referencedColumnName = "id")
    private UserEntity friend;

    public Long getId() {
        return id;
    }

    public FriendEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public FriendEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public UserEntity getFriend() {
        return friend;
    }

    public FriendEntity setFriend(UserEntity friend) {
        this.friend = friend;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user)
                .add("friend", friend)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendEntity that = (FriendEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(friend, that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, friend);
    }
}
