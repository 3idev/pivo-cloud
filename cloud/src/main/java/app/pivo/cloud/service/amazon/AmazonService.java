package app.pivo.cloud.service.amazon;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;

import java.util.List;

public interface AmazonService {

    boolean checkObject(User user, String bucket);

    List<S3Bucket> checkObjectInEveryBuckets(User user);

    boolean checkObject(String key, String bucket);

    boolean checkObjectInEveryBuckets(String key);

    PreSignedURL makePreSignedURL(String path, String bucket);

    void initializeUserResource(String prefix, String bucket);

    CognitoAccount createCognitoAccount(User user);

    CognitoToken issueToken(User user);

}
