package app.pivo.cloud.service.cloud.impl;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.cloud.service.cloud.CloudService;
import app.pivo.common.define.ApiErrorCode;
import app.pivo.common.define.UserLocation;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;
import app.pivo.common.exception.ApiException;
import app.pivo.common.repository.CognitoAccountRepository;
import app.pivo.common.service.geoip.GeoIPService;
import app.pivo.common.util.CommonPattern;
import app.pivo.common.util.IPUtils;
import app.pivo.common.util.PivoUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    public CognitoToken createCognitoURL(User user, String ip) {
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

        Optional<CognitoAccount> maybeCognitoAccount = cognitoAccountRepository.findByUserIdOptional(user.getId());
        CognitoAccount cognitoAccount;
        if (maybeCognitoAccount.isEmpty()) {
            cognitoAccount = amazonService.createCognitoAccount(user);
        } else {
            cognitoAccount = maybeCognitoAccount.get();
        }

        log.debug("checking {} is exists or not", cognitoAccount.getCognitoId());
        boolean isExists = amazonService.checkObject(cognitoAccount.getCognitoId() + "/", utils.locationToBucket(location));
        if (!isExists) {
            log.debug("initialize user resource");
            amazonService.initializeUserResource(cognitoAccount.getCognitoId(), utils.locationToBucket(location));
        }

        CognitoToken cognitoToken = amazonService.issueToken(user);

        return cognitoToken;
    }

    @Override
    public PreSignedURL makeShareableURL(User user, String path, Long ttl, UserLocation location) throws Exception {
        String bucket;
        boolean exist;
        if (null == location) {
            exist = amazonService.checkObjectInEveryBuckets(path);
            bucket = S3Bucket.US.getName();
        } else {
            bucket = utils.locationToBucket(location);
            exist = amazonService.checkObject(path, bucket);
        }

        if (!exist) {
            throw new ApiException(ApiErrorCode.OBJECT_NOT_FOUND);
        }

        Optional<CognitoAccount> maybeCognitoAccount = cognitoAccountRepository.findByUserIdOptional(user.getId());
        if (maybeCognitoAccount.isEmpty()) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }
        CognitoAccount cognitoAccount = maybeCognitoAccount.get();

        if (!path.startsWith(cognitoAccount.getCognitoId())) {
            throw new ApiException(ApiErrorCode.NOT_YOUR_RESOURCE);
        }

        if (CommonPattern.ARCHIVED_MEDIA_FOLDER.matcher(path).find()) {
            throw new ApiException(ApiErrorCode.CANNOT_SHARE_ARCHIVED_FILE);
        }

        PreSignedURL preSignedURL = amazonService.makePreSignedURL(path, bucket);

        return preSignedURL;
    }

}
