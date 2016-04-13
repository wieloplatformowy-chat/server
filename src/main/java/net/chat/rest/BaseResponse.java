package net.chat.rest;

/**
 * @author Mariusz Gorzycki
 * @since 27.03.2016
 */
public class BaseResponse<T extends BaseResponse>{
    protected boolean success;

    public T setSuccess(boolean success) {
        this.success = success;
        return (T) this;
    }

    public boolean isSuccess() {
        return success;
    }
}
