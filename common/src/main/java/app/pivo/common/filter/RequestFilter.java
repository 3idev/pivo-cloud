package app.pivo.common.filter;

import app.pivo.common.util.IPUtils;
import io.quarkus.runtime.configuration.ProfileManager;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Provider
@Priority(0)
public class RequestFilter implements ContainerRequestFilter {

    @Context
    HttpServerRequest request;

    private final String profile = ProfileManager.getActiveProfile();

    @Override
    public void filter(ContainerRequestContext ctx) {
        final String uri = ctx.getUriInfo().getPath(true);
        final String method = ctx.getMethod();
        final String address = request.remoteAddress().toString();

        log.info("<-- {} request {} {}", IPUtils.formatIP(address), method, uri);

        if (ctx.hasEntity() && profile.equals("dev")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 InputStream is = ctx.getEntityStream();) {
                is.transferTo(os);
                InputStream original = new ByteArrayInputStream(os.toByteArray());
                InputStream copy = new ByteArrayInputStream(os.toByteArray());
                String result = new String(copy.readAllBytes(), StandardCharsets.UTF_8);
                ctx.setEntityStream(original);
                original.close();
                copy.close();
                log.debug("\n{}", result);
            } catch (IOException e) {
                log.error("Failed to parse request body");
            }
        }
    }

}
