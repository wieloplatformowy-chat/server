package net.chat.entity;

import com.google.common.base.MoreObjects;
import org.fest.util.Collections;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Entity
@Table(name = "Conversations")
@NamedQueries({@NamedQuery(name = "Conversations.findAll", query = "SELECT c FROM ConversationEntity c"),
        @NamedQuery(name = "Conversations.myGroups", query = "SELECT c FROM ConversationEntity c JOIN c.users u WHERE c.group = true AND u = :me"),
        @NamedQuery(name = "Conversations.withMe", query = "SELECT c FROM ConversationEntity c JOIN c.users u  WHERE c.users.size = 1 AND u = :me"),
        @NamedQuery(name = "Conversations.with", query = "SELECT c FROM ConversationEntity c JOIN c.users u WHERE c.group = false AND u = :me AND c IN " +
                "(SELECT cc FROM ConversationEntity cc JOIN cc.users uu WHERE cc.group = false AND uu = :user)")})
public class ConversationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 1, max = 100)
    private String name;

    @Column(name = "isGroup")
    private boolean group;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "UserConversations",
            joinColumns = @JoinColumn(name = "conversation"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    private List<UserEntity> users;

    public Long getId() {
        return id;
    }

    public ConversationEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConversationEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<UserEntity> getUsers() {
        if (users == null)
            users = new LinkedList<>();
        return users;
    }

    public ConversationEntity setUsers(UserEntity... users) {
        this.users = Collections.list(users);
        return this;
    }

    public boolean isGroup() {
        return group;
    }

    public ConversationEntity setGroup(boolean group) {
        this.group = group;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("group", group)
                .toString();
    }
}
