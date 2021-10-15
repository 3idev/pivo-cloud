package app.pivo.cloud.resource.cloud;

import app.pivo.cloud.resource.cloud.dto.CloudPrivateDTO;
import app.pivo.cloud.service.cloud.CloudService;
import app.pivo.common.entity.User;
import app.pivo.common.response.ApiResponse;
import app.pivo.common.util.IPUtils;
import io.vertx.core.http.HttpServerRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/cl/cloud")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CloudPrivateResource {

    @Inject
    CloudService service;

    @Context
    SecurityContext ctx;

    @GET
    @Path("me")
    public Response createURL(@Context HttpServerRequest request) throws Exception {
        User user = (User) ctx.getUserPrincipal();
        return ApiResponse.from(service.createCognitoURL(user, IPUtils.getRemoteIP(request)));
    }

    @POST
    @Path("share")
    public Response makeShareableURL(@Valid CloudPrivateDTO.MakeShareableURL payload) throws Exception {
        User user = (User) ctx.getUserPrincipal();
        return ApiResponse.from(
                service.makeShareableURL(
                        user,
                        payload.getPath(),
                        payload.getLocation()
                )
        );
    }

}
