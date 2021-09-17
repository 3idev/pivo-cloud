package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.quarkus.security.AuthenticationFailedException;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthenticationFailedExceptionHandler implements ExceptionMapper<AuthenticationFailedException> {

    private final ApiErrorCode code = ApiErrorCode.NOT_AUTHORIZED;

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        ApiErrorResponse res = new ApiErrorResponse(this.code.getCode(), this.code.getMsg());

        return Response.status(this.code.getStatus()).entity(res).build();
    }
}
