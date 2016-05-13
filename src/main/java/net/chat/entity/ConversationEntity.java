package net.chat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Entity
@Table(name = "Conversations", uniqueConstraints = @UniqueConstraint(columnNames = {"user1", "user2"}))
@NamedQueries({@NamedQuery(name = "Conversation.findAll", query = "SELECT c FROM ConversationEntity c")})
public class ConversationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user1", referencedColumnName = "id")
    private UserEntity user1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user2", referencedColumnName = "id")
    private UserEntity user2;

    public Long getId() {
        return id;
    }

    public ConversationEntity setId(Long id) {
        this.id = id;
        return this;
    }
}
