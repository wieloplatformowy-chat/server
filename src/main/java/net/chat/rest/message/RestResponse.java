package net.chat.rest.message;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
public class RestResponse {
    @ApiModelProperty(required = true)
    private String response;

    public static RestResponse with(String data) {
        return new RestResponse().setResponse(data);
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "response='" + response + '\'' +
                '}';
    }

    public RestResponse setResponse(String response) {
        this.response = response;
        return this;
    }
}
