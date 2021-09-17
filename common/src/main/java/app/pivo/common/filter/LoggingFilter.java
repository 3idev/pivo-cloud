package app.pivo.common.filter;

import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

    @Inject
    Logger logger;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext ctx) {
        final String uri = ctx.getUriInfo().getPath(true);
        final String method = ctx.getMethod();
        final String address = request.remoteAddress().toString();

        logger.infof("<-- %s request %s %s", address, method, uri);
    }

}
