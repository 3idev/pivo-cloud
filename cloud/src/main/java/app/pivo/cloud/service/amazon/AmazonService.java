package app.pivo.cloud.service.amazon;

import app.pivo.common.define.S3Bucket;
import app.pivo.common.domain.PreSignedURL;
import app.pivo.common.entity.User;

import java.util.List;

public interface AmazonService {

    boolean checkObject(User user, String bucket);

    List<S3Bucket> checkObjectInEveryBuckets(User user);

    boolean checkObject(String key, String bucket);

    List<S3Bucket> checkObjectInEveryBuckets(String key);

    PreSignedURL makePreSignedURL(User user, String bucket) throws Exception;

    boolean softDeleteObject(User user, String path) throws Exception;

    boolean hardDeleteObject(User user, String path) throws Exception;

    void initializeUserResource(User user, String bucket);

    void issueToken(User user);

}
