package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class FallbackExceptionHandler implements ExceptionMapper<Exception> {

    private final ApiErrorCode code = ApiErrorCode.UNKNOWN;

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        ApiErrorResponse res = new ApiErrorResponse(this.code.getCode(), this.code.getMsg());

        return Response.status(500).entity(res).build();
    }

}
