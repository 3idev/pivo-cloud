package app.pivo.common.security;

import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.entity.Token;
import app.pivo.common.exception.InvalidJwtTokenException;
import app.pivo.common.repository.TokenRepository;
import app.pivo.common.response.ApiErrorResponse;
import app.pivo.common.service.token.TokenService;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class PivoAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    TokenRepository tokenRepository;

    @Inject
    TokenService tokenService;

    @Context
    Providers providers;

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    private final Pattern PUBLIC_REGEX = Pattern.compile("^/(.*)/p/(.*)$");
    //    private final Pattern ADMIN_REGEX = Pattern.compile("^/(.*)/a/(.*)$");

    private final ApiErrorResponse NOT_AUTHORIZED = new ApiErrorResponse(ApiErrorCode.NOT_AUTHORIZED.getCode(), ApiErrorCode.NOT_AUTHORIZED.getMsg());
    private final ApiErrorResponse INVALID_TOKEN = new ApiErrorResponse(ApiErrorCode.INVALID_TOKEN.getCode(), ApiErrorCode.INVALID_TOKEN.getMsg());

    @Override
    public void filter(ContainerRequestContext ctx) {
        final String path = info.getPath();
        final String method = ctx.getMethod();

        if (!method.equalsIgnoreCase("OPTIONS")) {
            if (!PUBLIC_REGEX.matcher(path).find()) {

                final String tokenRaw = request.getHeader("Authorization");
                try {
                    tokenService.validate(tokenRaw);
                } catch (InvalidJwtTokenException e) {
                    ctx.abortWith(Response.status(401).entity(INVALID_TOKEN).build());
                    return;
                }
                if (null != tokenRaw) {
                    Optional<Token> maybeToken = tokenRepository.findByAccessTokenOptional(tokenRaw);
                    if (maybeToken.isEmpty()) {
                        ctx.abortWith(Response.status(401).entity(this.NOT_AUTHORIZED).build());
                        return;
                    }

                    Token token = maybeToken.get();
                    ctx.setSecurityContext(UserContext.builder()
                            .user(token.getUser().convert())
                            .build()
                    );
                }
            }
        }
    }

}
