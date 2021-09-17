package app.pivo.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

import javax.ws.rs.core.Response;

@ToString
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = -2329793940858614781L;

    @JsonProperty("isOk")
    private final boolean isOk = true;
    private Object data;

    ApiResponse(Object data) {
        this.data = data;
    }

    public static Response from(Object data) {
        return Response.ok(new ApiResponse(data)).build();
    }

    public boolean getIsOk() {
        return isOk;
    }

    public Object getData() {
        return data;
    }
}
