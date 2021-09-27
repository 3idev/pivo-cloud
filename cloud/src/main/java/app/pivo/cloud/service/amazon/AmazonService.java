package app.pivo.cloud.service.amazon;

import app.pivo.common.domain.PreSignedURL;
import app.pivo.common.entity.User;

public interface AmazonService {

    boolean checkObject(User user, String bucket);

    boolean checkObjectInEveryBuckets(User user);

    boolean checkObject(String key, String bucket);

    boolean checkObjectInEveryBuckets(String key);

    PreSignedURL makePreSignedURL(User user, String bucket) throws Exception;

    boolean deleteObject(User user, String path) throws Exception;

    void initializeUserResource(User user, String bucket);

}
