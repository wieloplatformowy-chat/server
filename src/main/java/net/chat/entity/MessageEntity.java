package net.chat.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Messages")
@NamedQueries({@NamedQuery(name = "Message.findAll", query = "SELECT m FROM MessageEntity m"),
        @NamedQuery(name = "Message.last20", query = "SELECT m FROM MessageEntity m WHERE m.conversation = :conversation ORDER BY m.date desc"),
        @NamedQuery(name = "Message.findByNameLike", query = "SELECT u FROM UserEntity u WHERE lower(u.name) like lower(:name)"),
        @NamedQuery(name = "Message.findByEmailLike", query = "SELECT u FROM UserEntity u WHERE lower(u.email) like lower(:email)")})
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Timestamp date;

    @NotNull
    @Column(columnDefinition = "mediumtext")
    private String messaage;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @NotNull
    @ManyToOne
    private ConversationEntity conversation;

    public static MessageEntity withCurrentDate() {
        return new MessageEntity().setDate(new Timestamp(System.currentTimeMillis()));
    }

    public Long getId() {
        return id;
    }

    public MessageEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Timestamp getDate() {
        return date;
    }

    public MessageEntity setDate(Timestamp data) {
        this.date = data;
        return this;
    }

    public String getMessaage() {
        return messaage;
    }

    public MessageEntity setMessaage(String messaage) {
        this.messaage = messaage;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public MessageEntity setUser(UserEntity userEntity) {
        this.user = userEntity;
        return this;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public MessageEntity setConversation(ConversationEntity conversationEntity) {
        this.conversation = conversationEntity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(messaage, that.messaage) &&
                Objects.equals(user, that.user) &&
                Objects.equals(conversation, that.conversation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, messaage, user, conversation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("data", date)
                .add("messaage", messaage)
                .add("userEntity", user)
                .add("conversationEntity", conversation)
                .toString();
    }
}
