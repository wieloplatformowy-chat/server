package net.chat.rest.message;

/**
 * @author Mariusz Gorzycki
 * @since 27.03.2016
 */
public class BaseResponse<ERROR extends ResponseError> {
    protected boolean success;
    protected ERROR error;

    public static BaseResponse success() {
        return new BaseResponse().setSuccess();
    }

    public static BaseResponse error(Errors error) {
        return error(ResponseError.from(error));
    }

    public static <T extends ResponseError> BaseResponse<T> error(T error) {
        return new BaseResponse<T>().setError(error);
    }

    protected BaseResponse setError(ERROR error) {
        this.error = error;
        return this;
    }

    protected BaseResponse setSuccess() {
        this.success = true;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ERROR getError() {
        return error;
    }
}
