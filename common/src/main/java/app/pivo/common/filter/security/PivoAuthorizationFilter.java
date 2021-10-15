package app.pivo.common.filter.security;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.define.RedisPrefix;
import app.pivo.common.entity.User;
import app.pivo.common.repository.RedisRepository;
import app.pivo.common.response.ApiErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Slf4j
@Provider
@Priority(Priorities.AUTHORIZATION)
public class PivoAuthorizationFilter implements ContainerRequestFilter {

    @Inject
    RedisRepository redis;

    @Inject
    ObjectMapper om;

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Context
    SecurityContext securityContext;

    private final ApiErrorResponse UNKNOWN = new ApiErrorResponse(ApiErrorCode.UNKNOWN.getCode(), ApiErrorCode.UNKNOWN.getMsg());
    private final ApiErrorResponse FORBIDDEN = new ApiErrorResponse(ApiErrorCode.FORBIDDEN.getCode(), ApiErrorCode.FORBIDDEN.getMsg());

    @Override
    public void filter(ContainerRequestContext ctx) {
        final String path = info.getPath();
        final String method = ctx.getMethod();
        final String current = method + path;

        if (!method.equalsIgnoreCase("OPTIONS")) {
            User user = (User) securityContext.getUserPrincipal();
            try {
                if (null != user) {
                    io.vertx.redis.client.Response res = redis.get(RedisPrefix.NONE.getName(user.getRoleId()));

                    List<String> permissions = om.readValue(res.toString(), new TypeReference<List<String>>() {
                    });

                    boolean pass = permissions.stream().anyMatch(current::equals);
                    if (!pass) {
                        ctx.abortWith(Response.status(403).entity(this.FORBIDDEN).build());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("error: {}", e.getMessage());
                ctx.abortWith(Response.status(500).entity(this.UNKNOWN).build());
            }
        }
    }
}
