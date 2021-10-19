package app.pivo.cloud.service.amazon;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;

import java.util.List;

public interface AmazonService {

    boolean checkObject(User user, S3Bucket bucket);

    List<S3Bucket> checkObjectInEveryBuckets(User user);

    boolean checkObject(String key, S3Bucket bucket);

    List<S3Bucket> checkObjectInEveryBuckets(String key);

    PreSignedURL makePreSignedURL(String path, S3Bucket bucket);

    void initializeUserResource(String prefix, S3Bucket bucket);

    CognitoAccount createCognitoAccount(User user);

    CognitoToken issueToken(User user);

}
