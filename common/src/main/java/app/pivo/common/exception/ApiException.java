package app.pivo.common.exception;

import app.pivo.common.define.ApiErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends PivoException {

    private static final long serialVersionUID = -8809391504187862577L;

    public ApiException(ApiErrorCode code) {
        super(code);
    }

    public ApiException(ApiErrorCode code, String msg) {
        super(code, msg);
    }

    public ApiException(ApiErrorCode code, int status) {
        super(code, status);
    }

    public ApiException(ApiErrorCode code, int status, String msg) {
        super(code, status, msg);
    }

}
