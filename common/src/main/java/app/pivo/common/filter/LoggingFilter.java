package app.pivo.common.filter;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class LoggingFilter implements ContainerRequestFilter {

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext ctx) {
        final String uri = ctx.getUriInfo().getPath(true);
        final String method = ctx.getMethod();
        final String address = request.remoteAddress().toString();

        log.info("<-- {} request {} {}", address, method, uri);
    }

}
