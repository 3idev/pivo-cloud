package app.pivo.common.exception;

import app.pivo.common.define.ApiErrorCode;

public abstract class PivoException extends Exception {

    protected final ApiErrorCode code;
    protected final int status;
    protected final String msg;

    public PivoException(ApiErrorCode code) {
        super(code.getMsg());
        this.code = code;
        this.status = code.getStatus();
        this.msg = code.getMsg();
    }

    public PivoException(ApiErrorCode code, String msg) {
        super(msg);
        this.code = code;
        this.status = code.getStatus();
        this.msg = msg;
    }

    public PivoException(ApiErrorCode code, int status) {
        super(code.getMsg());
        this.code = code;
        this.status = status;
        this.msg = code.getMsg();
    }

    public PivoException(ApiErrorCode code, int status, String msg) {
        super(msg);
        this.code = code;
        this.status = status;
        this.msg = msg;
    }

    public String getCode() {
        return this.code.toString();
    }
    public int getStatus() { return this.status; }
    public String getMsg() { return this.msg; }

    public ApiErrorCode getCodeRaw() {
        return this.code;
    }

}
