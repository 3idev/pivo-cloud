/**
 *
 */

package app.pivo.common.exception.handler;

import app.pivo.common.exception.ApiException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        Map<String, Object> returnMap = new HashMap<>();
        {
            returnMap.put("isOk", false);
            returnMap.put("code", exception.getCode());
            returnMap.put("msg", exception.getMsg());
        }
        return Response.status(exception.getStatus()).entity(returnMap).build();
    }

}
