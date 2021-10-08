package app.pivo.cloud.service.amazon.sdk;

import app.pivo.common.entity.User;
import app.pivo.common.util.CommonPattern;
import app.pivo.common.util.PivoUtils;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

@Slf4j
@ApplicationScoped
public class S3SDK {

    @Inject
    PivoUtils utils;

    /**
     * @param user   user object
     * @param bucket bucket name
     * @return is it exists or not
     */
    public boolean checkObjectIsExists(User user, String bucket) {
        return this.checkObjectIsExists(user.transformToObjectKey(), bucket);
    }

    /**
     * @param key    object path
     * @param bucket bucket name
     * @return is it exists or not
     */
    public boolean checkObjectIsExists(String key, String bucket) {
        log.debug("Finding {} key from {} bucket", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucket).key(key).build();

            HeadObjectResponse res = client.headObject(headObjectRequest);

            log.debug("HeadObjectResponse: {}", res.metadata());
            return res.hasMetadata();
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    /**
     * Create folder
     *
     * @param key    folder path
     * @param bucket bucket name
     */
    public void createFolder(String key, String bucket) {
        log.debug("Creating {} folder into {}", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();

            PutObjectResponse res = client.putObject(putObjectRequest, RequestBody.fromByteBuffer(makeEmptyBuffer()));

            log.debug("PutObjectResponse: {}", res);
        }
    }

    /**
     * Move object into archived folder
     *
     * @param key    target path
     * @param bucket bucket name
     */
    public void softDelete(String key, String bucket) {
        try (S3Client client = generateClient(bucket)) {
            String from = key;
            String to = convertPathToArchivedFolder(key);
            this.copyObject(from, to, bucket, client);
            this.deleteObject(from, bucket, client);
        }
    }

    /**
     * Hard delete object from archive folder
     *
     * @param key    target object path
     * @param bucket bucket name
     */
    public void hardDelete(String key, String bucket) {
        if (!CommonPattern.ARCHIVED_MEDIA_FOLDER.matcher(key).find()) {
            throw new IllegalArgumentException("Only can delete object in archived folder");
        }

        try (S3Client client = generateClient(bucket)) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .key(key).bucket(bucket).build();

            DeleteObjectResponse res = client.deleteObject(deleteObjectRequest);

            log.debug("DeleteObjectResponse: {}", res);
        }
    }

    /**
     * Move object
     *
     * @param from   source target path
     * @param to     destination path
     * @param bucket bucket name
     */
    public void moveObject(String from, String to, String bucket) {
        try (S3Client client = generateClient(bucket)) {
            this.copyObject(from, to, bucket, client);
            this.deleteObject(from, bucket, client);
        }
    }

    /**
     * Copy object
     *
     * @param from   source target path
     * @param to     destination path
     * @param bucket bucket name
     */
    public void copyObject(String from, String to, String bucket) {
        try (S3Client client = generateClient(bucket)) {
            this.copyObject(from, to, bucket, client);
        }
    }

    /**
     * Copy object (internal use)
     *
     * @param from   source target path
     * @param to     destination path
     * @param bucket bucket name
     * @param client client
     */
    private void copyObject(String from, String to, String bucket, S3Client client) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder().sourceBucket(bucket).sourceKey(from).destinationBucket(bucket).destinationKey(to).build();
        CopyObjectResponse res = client.copyObject(copyObjectRequest);

        log.debug("CopyObjectResponse: {}", res);
    }

    /**
     * !!!DANGEROUS!!!
     * <p>
     * use it very carefully
     *
     * @param key    object path
     * @param bucket bucket name
     */
    public void deleteObject(String key, String bucket) {
        log.debug("Delete {} object", key);
        try (S3Client client = generateClient(bucket)) {
            this.deleteObject(key, bucket, client);
        }
    }

    /**
     * !!!DANGEROUS!!!
     * <p>
     * use it vert carefully
     *
     * @param key    target path
     * @param bucket bucket name
     * @param client client
     */
    private void deleteObject(String key, String bucket, S3Client client) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        DeleteObjectResponse res = client.deleteObject(deleteObjectRequest);

        log.debug("DeleteObjectResponse: {}", res);
    }

    /**
     * Create empty buffer
     *
     * @return Empty Buffer
     */
    private ByteBuffer makeEmptyBuffer() {
        byte[] b = new byte[0];
        return ByteBuffer.wrap(b);
    }

    private S3Client generateClient(String bucket) {
        return S3Client.builder().region(utils.getRegionFromBucket(bucket))
                .credentialsProvider(ProfileCredentialsProvider.create("dev")).build();
    }

    private String convertPathToArchivedFolder(String path) {
        Pattern pattern = CommonPattern.MEDIA_FOLDER;
        if (!pattern.matcher(path).find()) {
            throw new IllegalArgumentException("It's not pivo cloud folder");
        }

        path = pattern.matcher(path).replaceAll("/$1/archived/$2/$3");

        return path;
    }

}
