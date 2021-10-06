//package app.pivo.common.util.aws;
//
//import app.pivo.common.define.RedisPrefix;
//import app.pivo.common.domain.CognitoToken;
//import app.pivo.common.domain.URLWithTTL;
//import app.pivo.common.entity.User;
//import app.pivo.common.repository.RedisRepository;
//import app.pivo.common.util.PivoUtils;
//import io.vertx.redis.client.Response;
//import lombok.extern.slf4j.Slf4j;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import java.time.Duration;
//
//@Slf4j
//@ApplicationScoped
//public class S3PreSignerClient {
//
//    @Inject
//    RedisRepository redis;
//
//    @Inject
//    PivoUtils utils;
//
//    // 30 Minutes
//    private final long DEFAULT_TTL = 60 * 30;
//
//    // If remain ttl is less than 5 minutes, We will re-generate url.
//    private final long EXPIRE_TTL = 60 * 5;
//
//    private S3PreSignerClient() {
//    }
//
//    public CognitoToken generatePreSignedURL(User user, String bucketName) {
//        return this.generatePreSignedURL(user.get_id(), bucketName);
//    }
//
//    public CognitoToken generatePreSignedURL(String key, String bucketName) {
//        log.debug("bucket name: {}", bucketName);
//        try (S3Presigner client = S3Presigner.builder().region(utils.getRegionFromBucket(bucketName)).build()) {
//            URLWithTTL get = this.generateGetPreSignedURL(client, key, bucketName);
//            URLWithTTL put = this.generatePutPreSignedURL(client, key, bucketName);
//
//            return CognitoToken.builder()
//                    .get(get.getUrl())
//                    .put(put.getUrl())
//                    .ttl(get.getTtl())
//                    .build();
//        }
//    }
//
//    private URLWithTTL generatePutPreSignedURL(S3Presigner client, String key, String bucketName) {
//        Response result = redis.get(RedisPrefix.S3_PUT.getName(key, bucketName));
//        if (null != result) {
//            try {
//                log.debug("Found put url from redis!");
//                long ttl = redis.ttl(RedisPrefix.S3_PUT.getName(key, bucketName));
//                if (ttl > this.EXPIRE_TTL) {
//                    return URLWithTTL.builder()
//                            .url(result.toString())
//                            .ttl(ttl)
//                            .build();
//                }
//                log.debug("TTL is almost expire, generate new one");
//            } catch (Exception ignore) {
//            }
//        }
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(30))
//                .putObjectRequest(request)
//                .build();
//
//        PresignedPutObjectRequest presignedRequest = client.presignPutObject(presignRequest);
//        String url = presignedRequest.url().toString();
//
//        log.debug("put url: {}", url);
//
//        redis.setWithExpire(RedisPrefix.S3_PUT.getName(key, bucketName), url, 1800L);
//
//        return URLWithTTL.builder()
//                .url(url)
//                .ttl(this.DEFAULT_TTL)
//                .build();
//    }
//
//    private URLWithTTL generateGetPreSignedURL(S3Presigner client, String key, String bucketName) {
//        Response result = redis.get(RedisPrefix.S3_GET.getName(key, bucketName));
//        if (null != result) {
//            try {
//                log.debug("Found get url from redis!");
//                long ttl = redis.ttl(RedisPrefix.S3_GET.getName(key, bucketName));
//                if (ttl > this.EXPIRE_TTL) {
//                    return URLWithTTL.builder()
//                            .url(result.toString())
//                            .ttl(ttl)
//                            .build();
//                }
//                log.debug("TTL is almost expire, generate new one");
//            } catch (Exception ignore) {
//            }
//        }
//        GetObjectRequest request = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(30))
//                .getObjectRequest(request)
//                .build();
//
//        PresignedGetObjectRequest presignedRequest = client.presignGetObject(presignRequest);
//        String url = presignedRequest.url().toString();
//
//        log.debug("get url: {}", url);
//
//        redis.setWithExpire(RedisPrefix.S3_GET.getName(key, bucketName), url, 1800L);
//
//        return URLWithTTL.builder()
//                .url(url)
//                .ttl(this.DEFAULT_TTL)
//                .build();
//    }
//
//}
