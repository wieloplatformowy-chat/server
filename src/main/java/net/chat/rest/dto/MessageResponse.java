package net.chat.rest.dto;

import com.google.common.base.MoreObjects;
import net.chat.entity.MessageEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Timestamp date;
    private String message;
    private Long userId;
    private Long conversationId;

    public static MessageResponse fromEntity(MessageEntity entity) {
        return new MessageResponse()
                .setId(entity.getId())
                .setDate(entity.getDate())
                .setMessage(entity.getMessage())
                .setUserId(entity.getUser().getId())
                .setConversationId(entity.getConversation().getId());
    }

    public Long getId() {
        return id;
    }

    public MessageResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Timestamp getDate() {
        return date;
    }

    public MessageResponse setDate(Timestamp date) {
        this.date = date;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MessageResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public MessageResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public MessageResponse setConversationId(Long conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageResponse that = (MessageResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(message, that.message) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(conversationId, that.conversationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, message, userId, conversationId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("date", date)
                .add("message", message)
                .add("userId", userId)
                .add("conversationId", conversationId)
                .toString();
    }
}
