package app.pivo.common.service.geoip;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GeoIPService {

    @ConfigProperty(name = "geoip.api-key")
    String API_KEY;

    @Inject
    @RestClient
    GeoIP geoIP;

    public GeoIPResponse getLocation(String ip) throws ResteasyWebApplicationException {
        if (ip.equalsIgnoreCase("127.0.0.1")) {
            return GeoIPResponse.builder()
                    .continent_code("US")
                    .is_eu(false)
                    .build();
        }
        return geoIP.getLocation(API_KEY, ip);
    }

}
