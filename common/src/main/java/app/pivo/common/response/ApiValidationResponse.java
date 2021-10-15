package app.pivo.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ToString
public class ApiValidationResponse implements Serializable {

    @JsonProperty("isOk")
    private final boolean isOk = false;

    private final String code;
    private final String msg;
    private Set<Map<String, String>> errors = new HashSet<>();

    public ApiValidationResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiValidationResponse(String code, String msg, Set<Map<String, String>> errors) {
        this.code = code;
        this.msg = msg;
        this.errors = errors;
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

    public Set<Map<String, String>> getErrors() {
        return errors;
    }
}
