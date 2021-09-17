package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAcceptableExceptionHandler implements ExceptionMapper<NotAcceptableException> {

    private final ApiErrorCode code = ApiErrorCode.NOT_ACCEPTABLE;

    @Override
    public Response toResponse(NotAcceptableException exception) {
        ApiErrorResponse res = new ApiErrorResponse(this.code.getCode(), this.code.getMsg());

        return Response.status(this.code.getStatus()).entity(res).build();
    }

}
