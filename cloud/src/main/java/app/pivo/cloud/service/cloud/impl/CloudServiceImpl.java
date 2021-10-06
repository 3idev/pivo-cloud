package app.pivo.cloud.service.cloud.impl;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.cloud.service.cloud.CloudService;
import app.pivo.common.define.UserLocation;
import app.pivo.common.domain.CognitoToken;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;
import app.pivo.common.repository.CognitoAccountRepository;
import app.pivo.common.service.geoip.GeoIPService;
import app.pivo.common.util.CommonPattern;
import app.pivo.common.util.IPUtils;
import app.pivo.common.util.PivoUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class CloudServiceImpl implements CloudService {

    @Inject
    GeoIPService geoIPService;

    @Inject
    AmazonService amazonService;

    @Inject
    CognitoAccountRepository cognitoAccountRepository;

    @Inject
    PivoUtils utils;

    @Override
    public CognitoToken createCognitoURL(User user, String ip) throws Exception {
        String formatIP = IPUtils.formatIP(ip);
        log.debug("ip is {}", formatIP);

        UserLocation location = UserLocation.US;
        try {
            boolean isEU = geoIPService.getLocation(formatIP).getIs_eu();
            log.debug("is eu?: {}", isEU);
            if (isEU) {
                location = UserLocation.EU;
            }
        } catch (ResteasyWebApplicationException e) {
            log.error("failed to get location from ip", e);
        }

        Optional<CognitoAccount> maybeCognitoAccount = cognitoAccountRepository.findByUserIdOptional(user.get_id());
        CognitoAccount cognitoAccount;
        if (maybeCognitoAccount.isEmpty()) {
            cognitoAccount = amazonService.createCognitoAccount(user);
        } else {
            cognitoAccount = maybeCognitoAccount.get();
        }

        log.info("Checking {} is exists", cognitoAccount.getCognitoId());
        boolean isExists = amazonService.checkObject(cognitoAccount.getCognitoId() + "/", utils.locationToBucket(location));
        if (!isExists) {
            log.debug("initialize user resource");
            amazonService.initializeUserResource(cognitoAccount.getCognitoId(), utils.locationToBucket(location));
        }

        CognitoToken cognitoToken = amazonService.issueToken(user);

        return cognitoToken;
    }

    @Override
    public void softDeleteObject(User user, String path) throws Exception {
        if (!path.startsWith(String.format("/%s", user.get_id()))) {
            throw new IllegalArgumentException("User can delete only own resource");
        } else if (!CommonPattern.MEDIA_FOLDER.matcher(path).find()) {
            throw new IllegalArgumentException("Only can delete object in archived folder");
        }

        List<S3Bucket> buckets = amazonService.checkObjectInEveryBuckets(path);

        if (buckets.size() > 0) {
            amazonService.softDeleteObject(user, path);
        }
    }

    @Override
    public void hardDeleteObject(User user, String path) throws Exception {
        if (!path.startsWith(String.format("/%s", user.get_id()))) {
            throw new IllegalArgumentException("User can delete only own resource");
        } else if (!CommonPattern.ARCHIVED_MEDIA_FOLDER.matcher(path).find()) {
            throw new IllegalArgumentException("Only can delete object in archived folder");
        }

        List<S3Bucket> buckets = amazonService.checkObjectInEveryBuckets(path);

        if (buckets.size() > 0) {
            amazonService.hardDeleteObject(user, path);
        }
    }

}
