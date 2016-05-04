package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class PasswordDto {
    @ApiModelProperty(required = true)
    private String password;

    public static PasswordDto with(String data) {
        return new PasswordDto().setPassword(data);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PasswordDto{" +
                "password='" + password + '\'' +
                '}';
    }

    public PasswordDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
