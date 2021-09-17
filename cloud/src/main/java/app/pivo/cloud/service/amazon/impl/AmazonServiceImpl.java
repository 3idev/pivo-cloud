package app.pivo.cloud.service.amazon.impl;

import app.pivo.common.util.aws.AWSProperty;
import app.pivo.common.util.aws.S3;
import app.pivo.common.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.common.entity.User;
import app.pivo.common.util.aws.S3PreSignerClient;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AmazonServiceImpl implements AmazonService {

    @Inject
    S3 s3;

    @Inject
    S3PreSignerClient presignedClient;

    @Inject
    Logger log;

    @Override
    public boolean checkObject(User user, String bucket) {
        try {
            return s3.checkObjectIsExists(user, bucket);
        } catch(NoSuchKeyException e) {
            log.warnf("We can't find the key: %s", user.get_id());
            return false;
        } catch(S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public PreSignedURL makePreSignedURL(User user, String bucket) {
        return presignedClient.generatePreSignedURL(user, bucket);
    }

    @Override
    public boolean deleteObject(User user, String path) {
        return true;
    }

    @Override
    public void initializeUserResource(User user, String bucket) {
        try {
            // Create root folder
            s3.createFolder(user.transformToObjectKey(), bucket);
            // Create images folder
            s3.createFolder(user.transformToObjectKey("images"), bucket);
            // Create videos folder
            s3.createFolder(user.transformToObjectKey("videos"), bucket);
        } catch (Exception ignore) {
            log.error("Failed to make default folders");
        }
    }

}
