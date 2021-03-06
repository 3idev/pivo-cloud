package app.pivo.common.filter;

import app.pivo.common.util.IPUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.runtime.configuration.ProfileManager;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Provider
public class ResponseFilter implements ContainerResponseFilter {

    @Context
    HttpServerRequest request;

    @Context
    HttpServerResponse response;

    private final ObjectMapper om;

    private final String profile = ProfileManager.getActiveProfile();

    private final Map<String, String> headers = new HashMap<>() {
        {
            put("Content-Security-Policy", "script-src 'self'");
            put("X-Content-Type-Options", "nosniff");
            put("X-XSS-Protection", "1;mode=block");
            put("Cache-Control", "no-cache");
            put("Pragma", "no-cache");
        }
    };

    public ResponseFilter() {
        this.om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        final String uri = req.getUriInfo().getPath(true);
        final String method = req.getMethod();
        final String address = request.remoteAddress().toString();

        /* For prevent several MIME attack */
        headers.forEach((key, value) -> {
            response.putHeader(key, value);
        });

        if (res.hasEntity() && this.profile.equalsIgnoreCase("dev")) {
            final Object resObject = res.getEntity();

            log.debug("\n{}", om.writeValueAsString(resObject));
        }

        log.info("{} request {} {} -->", IPUtils.formatIP(address), method, uri);
    }

}
