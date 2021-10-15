package app.pivo.cloud.service.cloud;

import app.pivo.cloud.define.UserLocation;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.common.entity.User;

public interface CloudService {

    CognitoToken createCognitoURL(User user, String ip);

    PreSignedURL makeShareableURL(User user, String path, UserLocation location) throws Exception;

}
