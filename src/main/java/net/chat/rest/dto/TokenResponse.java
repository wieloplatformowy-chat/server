package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class TokenResponse {
    @ApiModelProperty(required = true)
    private String token;

    public static TokenResponse with(String data) {
        return new TokenResponse().setToken(data);
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "PasswordParam{" +
                "token='" + token + '\'' +
                '}';
    }

    public TokenResponse setToken(String token) {
        this.token = token;
        return this;
    }
}
