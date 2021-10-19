package app.pivo.cloud.utils;

import app.pivo.cloud.define.S3Bucket;
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
        }
        return Region.US_EAST_1;
    }

    public Region getRegionFromBucket(S3Bucket bucket) {
        switch (bucket) {
            case EU:
                return Region.EU_CENTRAL_1;
            default:
            case US:
                return Region.US_EAST_1;
        }
    }

    public S3Bucket locationToBucket(UserLocation location) {
        switch (location) {
            case US:
                return S3Bucket.from(property.buckets().get("us"));
            case EU:
                return S3Bucket.from(property.buckets().get("eu"));
            default:
                return S3Bucket.from(property.fallbackBucket());
        }
    }

}
