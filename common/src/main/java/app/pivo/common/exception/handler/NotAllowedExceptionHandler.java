package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedExceptionHandler implements ExceptionMapper<NotAllowedException> {

    private final ApiErrorCode code = ApiErrorCode.METHOD_NOT_ALLOWED;

    @Override
    public Response toResponse(NotAllowedException exception) {
        ApiErrorResponse res = new ApiErrorResponse(this.code.getCode(), this.code.getMsg());

        return Response.status(this.code.getStatus()).entity(res).build();
    }

}
