package app.pivo.common.util;

import app.pivo.common.define.UserLocation;
import app.pivo.common.util.aws.AWSProperty;
import software.amazon.awssdk.regions.Region;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PivoUtils {

    @Inject
    AWSProperty property;

    private PivoUtils() {}

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
