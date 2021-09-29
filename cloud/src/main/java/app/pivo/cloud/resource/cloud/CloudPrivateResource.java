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

    @DELETE
    @Path("soft")
    public Response softDeleteObject(@BeanParam @Valid CloudPrivateDTO.SoftDeleteObject payload) throws Exception {
        User user = (User) ctx.getUserPrincipal();
        this.service.softDeleteObject(user, payload.getPath());

        return ApiResponse.from("OK");
    }

    @DELETE
    @Path("hard")
    public Response hardDeleteObject(@BeanParam @Valid CloudPrivateDTO.HardDeleteObject payload) throws Exception {
        User user = (User) ctx.getUserPrincipal();
        this.service.hardDeleteObject(user, payload.getPath());

        return ApiResponse.from("OK");
    }

    @POST
    @Path("restore")
    public Response restoreObject() throws Exception {
        return ApiResponse.from("NotImplementYet");
    }

}
