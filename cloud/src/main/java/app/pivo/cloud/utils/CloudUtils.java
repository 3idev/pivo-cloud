package app.pivo.cloud.utils;

import app.pivo.cloud.define.UserLocation;
import app.pivo.cloud.service.amazon.sdk.configuration.S3Configuration;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class CloudUtils {

    @Inject
    S3Configuration property;

    private CloudUtils() {
    }

    public Region getRegionFromBucket(String bucket) {
        if (bucket.contains("eu")) {
            return Region.EU_CENTRAL_1;
        } else {
            return Region.US_EAST_1;
        }
    }

    public String locationToBucket(UserLocation location) {
        switch (location) {
            case US:
                return property.buckets().get("us");
            case EU:
                return property.buckets().get("eu");
            default:
                return property.fallbackBucket();
        }
    }

}
