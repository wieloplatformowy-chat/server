package net.chat.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@Entity
@Table(name = "UserConversations", uniqueConstraints = @UniqueConstraint(columnNames = {"user", "conversation"}))
@NamedQueries({@NamedQuery(name = "UserConversations.findAll", query = "SELECT u FROM UserConversationEntity u"),
        @NamedQuery(name = "UserConversations.findByUserAndConversation", query = "SELECT u FROM UserConversationEntity u WHERE u.user = :user AND u.conversation = :conversation")})
public class UserConversationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "conversation", referencedColumnName = "id")
    private ConversationEntity conversation;

    @ManyToOne
    private MessageEntity lastSeenPost;

    private Timestamp lastSeenPostDate;

    @NotNull
    @Column(columnDefinition = "Boolean default false")
    private boolean userLeft;

    public Long getId() {
        return id;
    }

    public UserConversationEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserConversationEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public UserConversationEntity setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
        return this;
    }

    public MessageEntity getLastSeenPost() {
        return lastSeenPost;
    }

    public UserConversationEntity setLastSeenPost(MessageEntity lastSeenPost) {
        this.lastSeenPost = lastSeenPost;
        return this;
    }

    public Timestamp getLastSeenPostDate() {
        return lastSeenPostDate;
    }

    public UserConversationEntity setLastSeenPostDate(Timestamp lastSeenPostDate) {
        this.lastSeenPostDate = lastSeenPostDate;
        return this;
    }

    public boolean isUserLeft() {
        return userLeft;
    }

    public UserConversationEntity setUserLeft(boolean userLeft) {
        this.userLeft = userLeft;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user)
                .add("conversation", conversation)
                .add("lastSeenPostId", lastSeenPost)
                .add("lastSeenPostDate", lastSeenPostDate)
                .add("userLeft", userLeft)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConversationEntity that = (UserConversationEntity) o;
        return userLeft == that.userLeft &&
                Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(conversation, that.conversation) &&
                Objects.equals(lastSeenPost, that.lastSeenPost) &&
                Objects.equals(lastSeenPostDate, that.lastSeenPostDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, conversation, lastSeenPost, lastSeenPostDate, userLeft);
    }
}
