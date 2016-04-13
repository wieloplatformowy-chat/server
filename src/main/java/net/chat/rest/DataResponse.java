package net.chat.rest;

/**
 * @author Mariusz Gorzycki
 * @since 29.03.2016
 */
public class DataResponse<DATA> extends BaseResponse{
    protected DATA data;

    public static BaseResponse success() {
        throw new UnsupportedOperationException("succes without a data is not allowed.");
    }

    public static <D>DataResponse<D> success(D data) {
        return new DataResponse<D>().setSuccess().setData(data);
    }

    protected DataResponse setData(DATA data) {
        this.data = data;
        return this;
    }

    @Override
    protected DataResponse setSuccess() {
        this.success = true;
        return this;
    }

    public DATA getData() {
        return data;
    }
}
