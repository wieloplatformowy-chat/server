package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class TokenDto {
    @ApiModelProperty(required = true)
    private String token;

    public static TokenDto with(String data) {
        return new TokenDto().setToken(data);
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "PasswordDto{" +
                "token='" + token + '\'' +
                '}';
    }

    public TokenDto setToken(String token) {
        this.token = token;
        return this;
    }
}
