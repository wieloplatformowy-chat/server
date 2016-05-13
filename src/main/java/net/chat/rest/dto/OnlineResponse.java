package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 08.05.2016
 */
public class OnlineResponse {
    @ApiModelProperty(required = true)
    private boolean online;

    public static OnlineResponse with(boolean data) {
        return new OnlineResponse().setOnline(data);
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "PasswordParam{" +
                "online='" + online + '\'' +
                '}';
    }

    public OnlineResponse setOnline(boolean online) {
        this.online = online;
        return this;
    }
}
