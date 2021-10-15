package app.pivo.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class ApiErrorResponse implements Serializable {

    private static final long serialVersionUID = 5981505808349632177L;

    @JsonProperty("isOk")
    private final boolean isOk = false;

    private final String code;
    private final String msg;

    public ApiErrorResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean getIsOk() {
        return isOk;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
