package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiErrorResponse;
import io.quarkus.security.UnauthorizedException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@Provider
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {

    @Inject
    Logger log;

    private final ApiErrorCode code = ApiErrorCode.NOT_AUTHORIZED;

    @Override
    public Response toResponse(UnauthorizedException exception) {
        exception.printStackTrace();
        ApiErrorResponse res = new ApiErrorResponse(this.code.getCode(), this.code.getMsg());

        return Response.status(this.code.getStatus()).entity(res).build();
    }
}
