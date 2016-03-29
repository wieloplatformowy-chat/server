package net.chat.rest;

/**
 * @author Mariusz Gorzycki
 * @since 27.03.2016
 */
public class LoginResponse extends BaseResponse<LoginResponse>{
    private boolean userNotExists;
    private boolean wrongPassword;
    private boolean credentialsNotProvided;

    private LoginResponse() {
    }

    public static LoginResponse success(){
        return new LoginResponse().setSuccess(true);
    }

    public static LoginResponse userNotExists(){
        return new LoginResponse().setUserNotExists(true);
    }

    public static LoginResponse wrongPassword(){
        return new LoginResponse().setWrongPassword(true);
    }

    public static LoginResponse credentialsNotProvided(){
        return new LoginResponse().setCredentialsNotProvided(true);
    }

    public LoginResponse setUserNotExists(boolean userNotExists) {
        this.userNotExists = userNotExists;
        return this;
    }

    public LoginResponse setWrongPassword(boolean wrongPassword) {
        this.wrongPassword = wrongPassword;
        return this;
    }

    public LoginResponse setCredentialsNotProvided(boolean credentialsNotProvided) {
        this.credentialsNotProvided = credentialsNotProvided;
        return this;
    }

    public boolean isUserNotExists() {
        return userNotExists;
    }

    public boolean isWrongPassword() {
        return wrongPassword;
    }

    public boolean isCredentialsNotProvided() {
        return credentialsNotProvided;
    }
}
