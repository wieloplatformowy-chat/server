package net.chat.entity;

import com.google.common.base.Objects;
import org.fest.util.Collections;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Entity
@Table(name = "Groups")
@NamedQueries({@NamedQuery(name = "Groups.findAll", query = "SELECT g FROM GroupEntity g")})
public class GroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "UserGroups",
            joinColumns = @JoinColumn(name = "group"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    private List<UserEntity> users;

    public Long getId() {
        return id;
    }

    public GroupEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<UserEntity> getUsers() {
        if (users == null)
            users = new LinkedList<>();
        return users;
    }

    public GroupEntity setUsers(UserEntity... users) {
        this.users = Collections.list(users);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
