package app.pivo.cloud.service.cloud;

import app.pivo.common.domain.PreSignedURL;
import app.pivo.common.entity.User;

public interface CloudService {

    PreSignedURL createCognitoURL(User user, String ip) throws Exception;

    boolean softDeleteObject(User user, String path) throws Exception;

    boolean hardDeleteObject(User user, String path) throws Exception;

}
