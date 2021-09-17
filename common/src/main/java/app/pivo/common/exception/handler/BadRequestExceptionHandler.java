package app.pivo.common.exception.handler;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.response.ApiValidationResponse;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.*;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    private final ApiErrorCode code = ApiErrorCode.VALIDATION_ERROR;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<Map<String, String>> errors = new HashSet<>();
        exception.getConstraintViolations().forEach(item -> {
            String path = item.getPropertyPath().toString();
            String param;
            if (path.contains(".")) {
                String[] splits = path.split("\\.");
                param = splits[splits.length - 1];
            } else {
                param = path;
            }
            errors.add(new HashMap<>() {
                {
                    put(param, item.getMessage());
                }
            });
        });
        ApiValidationResponse res = new ApiValidationResponse(code.getCode(), code.getMsg(), errors);
        return Response.status(400).entity(res).build();
    }

}
