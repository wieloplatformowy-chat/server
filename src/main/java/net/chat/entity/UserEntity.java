package net.chat.entity;

import org.fest.util.Collections;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries({@NamedQuery(name = "User.findAll", query = "SELECT u FROM UserEntity u"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM UserEntity u WHERE u.name = :name"),
        @NamedQuery(name = "User.findByNameLike", query = "SELECT u FROM UserEntity u WHERE lower(u.name) like lower(:name)"),
        @NamedQuery(name = "User.findByEmailLike", query = "SELECT u FROM UserEntity u WHERE lower(u.email) like lower(:email)")})
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    private String name;

    @NotNull
    @Size(min = 1, max = 25)
    private String email;

    @NotNull
    @Size(min = 1, max = 25)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "UserConversations",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "conversation"))
    private List<ConversationEntity> conversations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<ConversationEntity> getConversations() {
        if (conversations == null)
            conversations = new LinkedList<>();
        return conversations;
    }

    public UserEntity setConversations(ConversationEntity... conversations) {
        this.conversations = Collections.list(conversations);
        return this;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
