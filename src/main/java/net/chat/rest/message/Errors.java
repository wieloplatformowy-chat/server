package net.chat.rest.message;

/**
 * @author Mariusz Gorzycki
 * @since 29.03.2016
 */
public enum Errors {
    UNKNOWN_ERROR(1, "Unknown error occured."),
    INVALID_JSON(2, "Invalid JSON in request body."),

    USER_NOT_EXISTS(101, "There is no registered user with given name."),
    INVALID_PASSWORD(102, "Password does not match."),
    USERNAME_IS_TAKEN(103, "UserEntity with given name already exists."),
    CREDENTIALS_NOT_PROVIDED(104, "Username or password is null."),
    LOGIN_REQUIRED(105, "You are not logged in. Authentication token is invalid, null or expired."),
    ALREADY_A_FRIEND(106, "This user has been added as your friend before."),
    NOT_A_FRIEND(107, "This user is not your friend."),
    GROUP_NOT_EXISTS(108, "Group with given id does not exists");

    Errors(int id, String message) {
        this.id = id;
        this.message = message;
    }

    private final int id;
    private final String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
