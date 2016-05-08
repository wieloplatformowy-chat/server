package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 08.05.2016
 */
public class OnlineDto {
    @ApiModelProperty(required = true)
    private boolean online;

    public static OnlineDto with(boolean data) {
        return new OnlineDto().setOnline(data);
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "PasswordDto{" +
                "online='" + online + '\'' +
                '}';
    }

    public OnlineDto setOnline(boolean online) {
        this.online = online;
        return this;
    }
}
