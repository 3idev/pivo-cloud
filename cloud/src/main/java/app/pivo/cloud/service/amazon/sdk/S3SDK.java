package app.pivo.cloud.service.amazon.sdk;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.utils.CloudUtils;
import app.pivo.cloud.utils.S3Utils;
import app.pivo.common.util.CommonPattern;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@ApplicationScoped
public class S3SDK extends baseSDK {

    @Inject
    CloudUtils utils;

    /**
     * @param key    object path
     * @param bucket bucket name
     * @return is it exists or not
     */
    public boolean checkObjectIsExists(String key, S3Bucket bucket) {
        log.debug("Finding {} key from {} bucket", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            HeadObjectResponse res = this.headObject(key, bucket, client);

            log.debug("checkObjectIsExists,hasMetadata: {}", res.hasMetadata());
            return res.hasMetadata();
        } catch (NoSuchKeyException e) {
            log.debug("{} is not exist in {}", key, bucket);
            return false;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * Create folder
     *
     * @param key    folder path
     * @param bucket bucket name
     */
    public void createFolder(String key, S3Bucket bucket) {
        log.debug("Creating {} folder into {}", key, bucket);
        try (S3Client client = generateClient(bucket)) {
            PutObjectResponse res = this.putObject(key, bucket, this.makeEmptyBuffer(), client);

            log.debug("PutObjectResponse: {}", res);
        }
    }

    public void initializeUserResourceFolder(String root, S3Bucket bucket) {
        try (S3Client client = generateClient(bucket)) {
            Arrays.asList(
                    S3Utils.pathResolve(root)
                    , S3Utils.pathResolve(root, "images")
                    , S3Utils.pathResolve(root, "videos")
                    , S3Utils.pathResolve(root, "archived")
                    , S3Utils.pathResolve(root, "archived", "images")
                    , S3Utils.pathResolve(root, "archived", "videos")
            ).forEach(path -> {
                log.debug("Make {} folder", path);
                this.putObject(path, bucket, this.makeEmptyBuffer(), client);
            });
        }
    }

    /**
     * Move object into archived folder
     *
     * @param key    target path
     * @param bucket bucket name
     */
    public void softDelete(String key, S3Bucket bucket) {
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
    public void hardDelete(String key, S3Bucket bucket) {
        if (!CommonPattern.ARCHIVED_MEDIA_FOLDER.matcher(key).find()) {
            throw new IllegalArgumentException("Only can delete object in archived folder");
        }

        try (S3Client client = generateClient(bucket)) {
            this.deleteObject(key, bucket, client);
        }
    }

    /**
     * Move object
     *
     * @param from   source target path
     * @param to     destination path
     * @param bucket bucket name
     */
    public void moveObject(String from, String to, S3Bucket bucket) {
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
    public void copyObject(String from, String to, S3Bucket bucket) {
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
    private void copyObject(String from, String to, S3Bucket bucket, S3Client client) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucket.getName())
                .sourceKey(from)
                .destinationBucket(bucket.getName())
                .destinationKey(to)
                .build();
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
    public void deleteObject(String key, S3Bucket bucket) {
        log.debug("Delete {} object", key);
        try (S3Client client = generateClient(bucket)) {
            this.deleteObject(key, bucket, client);
        }
    }

    /**
     * !!!DANGEROUS!!!
     * <p>
     * use it very carefully
     *
     * @param key    target path
     * @param bucket bucket name
     * @param client client
     */
    private DeleteObjectResponse deleteObject(String key, S3Bucket bucket, S3Client client) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket.getName())
                .key(key)
                .build();
        DeleteObjectResponse response = client.deleteObject(request);
        log.debug("DeleteObjectResponse: {}", response);

        return response;
    }

    private HeadObjectResponse headObject(String key, S3Bucket bucket, S3Client client) throws NoSuchKeyException {
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucket.getName())
                .key(key)
                .build();

        HeadObjectResponse response = client.headObject(request);
        log.debug("HeadObjectResponse: {}", response);

        return response;
    }

    private PutObjectResponse putObject(String key, S3Bucket bucket, ByteBuffer payload, S3Client client) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket.getName())
                .key(key)
                .build();

        PutObjectResponse response = client.putObject(request, RequestBody.fromByteBuffer(payload));
        log.debug("PutObjectResponse: {}", response);

        return response;
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

    private S3Client generateClient(S3Bucket bucket) {
        return S3Client.builder()
                .region(utils.getRegionFromBucket(bucket))
                .credentialsProvider(this.generateCredentials())
                .build();
    }

    private String convertPathToArchivedFolder(String path) throws IllegalArgumentException {
        Pattern pattern = CommonPattern.MEDIA_FOLDER;
        if (!pattern.matcher(path).find()) {
            throw new IllegalArgumentException("It's not pivo cloud folder");
        }

        path = pattern.matcher(path).replaceAll("/$1/archived/$2/$3");

        return path;
    }

}
