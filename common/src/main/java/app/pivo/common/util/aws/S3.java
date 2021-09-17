package app.pivo.common.util.aws;

import app.pivo.common.entity.User;
import app.pivo.common.util.PivoUtils;
import org.jboss.logging.Logger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

@ApplicationScoped
public class S3 {

    @Inject
    Logger log;

    @Inject
    PivoUtils utils;

    public boolean checkObjectIsExists(User user, String bucket) {
        return this.checkObjectIsExists(user.transformToObjectKey(), bucket);
    }

    public boolean checkObjectIsExists(String key, String bucket) {
        log.infof("Finding %s key from %s bucket", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            HeadObjectResponse res = client.headObject(headObjectRequest);

            log.debugf("HeadObjectResponse: %s", res);

            return res.hasMetadata();
        }
    }

    public void createFolder(String key, String bucket) {
        log.infof("Creating %s folder into %s", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            PutObjectResponse res = client.putObject(putObjectRequest, RequestBody.fromByteBuffer(makeEmptyBuffer()));

            log.debugf("PutObjectResponse: %s", res);
        }
    }

    private ByteBuffer makeEmptyBuffer() {
        byte[] b = new byte[0];
        return ByteBuffer.wrap(b);
    }

    private S3Client generateClient(String bucket) {
        return S3Client.builder()
                .region(utils.getRegionFromBucket(bucket))
                .credentialsProvider(ProfileCredentialsProvider.create("dev"))
                .build();
    }

}
