package app.pivo.common.service.geoip;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RegisterRestClient(configKey="geo-ip")
public interface GeoIP {

    @GET
    @Path("/ipgeo")
    GeoIPResponse getLocation(@QueryParam("apiKey") String apikey, @QueryParam("ip") String ip);

}
