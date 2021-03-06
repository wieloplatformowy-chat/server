package net.chat.rest.message;

/**
 * @author Mariusz Gorzycki
 * @since 29.03.2016
 */
public class ResponseError {
    protected int id;
    protected String name;
    protected String message;

    public ResponseError() {
    }

    protected ResponseError(int id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public static ResponseError from(Errors error) {
        return new ResponseError(error.getId(), error.name(), "*DEBUG ONLY* " + error.getMessage());
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
