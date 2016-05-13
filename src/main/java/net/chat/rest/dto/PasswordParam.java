package net.chat.rest.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class PasswordParam {
    @ApiModelProperty(required = true)
    private String password;

    public static PasswordParam with(String data) {
        return new PasswordParam().setPassword(data);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PasswordParam{" +
                "password='" + password + '\'' +
                '}';
    }

    public PasswordParam setPassword(String password) {
        this.password = password;
        return this;
    }
}
