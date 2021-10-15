package app.pivo.common.filter;

import app.pivo.common.util.IPUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.runtime.configuration.ProfileManager;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
public class ResponseFilter implements ContainerResponseFilter {

    @Context
    HttpServerRequest request;

    private final ObjectMapper om;

    private final String profile = ProfileManager.getActiveProfile();

    public ResponseFilter() {
        this.om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void filter(ContainerRequestContext ctx, ContainerResponseContext response) throws IOException {
        final String uri = ctx.getUriInfo().getPath(true);
        final String method = ctx.getMethod();
        final String address = request.remoteAddress().toString();

        if (response.hasEntity() && this.profile.equalsIgnoreCase("dev")) {
            Object res = response.getEntity();

            log.debug("\n{}", om.writeValueAsString(res));
        }

        log.info("{} request {} {} -->", IPUtils.formatIP(address), method, uri);
    }

}
