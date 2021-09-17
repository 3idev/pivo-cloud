package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    private final ApiErrorCode code = ApiErrorCode.PAGE_NOT_FOUND;

    @Override
    public Response toResponse(NotFoundException exception) {
        ApiErrorResponse res = new ApiErrorResponse(
                this.code.getCode(),
                this.code.getMsg()
        );

        return Response.status(this.code.getStatus()).entity(res).build();
    }

}
