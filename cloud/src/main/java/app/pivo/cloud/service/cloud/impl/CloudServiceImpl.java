package app.pivo.cloud.service.cloud.impl;

import app.pivo.common.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.cloud.service.cloud.CloudService;
import app.pivo.common.service.geoip.GeoIPService;
import app.pivo.common.entity.User;
import app.pivo.common.util.PivoUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CloudServiceImpl implements CloudService {

    @Inject
    Logger log;

    @Inject
    GeoIPService geoIPService;

    @Inject
    AmazonService amazonService;

    @Inject
    PivoUtils utils;

    @Override
    public PreSignedURL createCognitoURL(User user, String ip) throws Exception {
        log.infof("ip is %s", formatIP(ip));

        String location = "US";
        try {
            boolean isEU = geoIPService.getLocation(formatIP(ip)).getIs_eu();
            log.infof("is eu?: %b", isEU);
            if (isEU) {
                location = "EU";
            }
        } catch (ResteasyWebApplicationException e) {
            log.error("failed to get location from ip");
        }

        boolean isExists = amazonService.checkObject(user, utils.locationToBucket(location));
        if (!isExists) {
            log.infof("initialize user resource");
            amazonService.initializeUserResource(user, utils.locationToBucket(location));
        }

        return amazonService.makePreSignedURL(user, utils.locationToBucket(location));
    }

    @Override
    public boolean deleteObject(User user, String path) {
        return true;
    }

    private String formatIP(String ip) {
        if (ip.contains(":")) {
            return ip.split(":")[0];
        }

        return ip;
    }

}
