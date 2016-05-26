package net.chat.rest.dto;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @author Mariusz Gorzycki
 * @since 26.04.2016
 */
public class SendMessageParams {
    @ApiModelProperty(required = true, position = 0)
    private Long conversationId;
    @ApiModelProperty(required = true, position = 1)
    private String message;

    public Long getConversationId() {
        return conversationId;
    }

    public SendMessageParams setConversationId(Long conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SendMessageParams setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendMessageParams that = (SendMessageParams) o;
        return Objects.equals(conversationId, that.conversationId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, message);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("conversationId", conversationId)
                .add("message", message)
                .toString();
    }
}
