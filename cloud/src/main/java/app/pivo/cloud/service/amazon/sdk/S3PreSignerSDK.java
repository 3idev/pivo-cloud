package app.pivo.cloud.service.amazon.sdk;

import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.common.define.RedisPrefix;
import app.pivo.common.repository.RedisRepository;
import app.pivo.common.util.PivoUtils;
import app.pivo.common.util.aws.configuration.S3PreSignerConfiguration;
import io.vertx.redis.client.Response;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;

@Slf4j
@ApplicationScoped
public class S3PreSignerSDK {

    @Inject
    RedisRepository redis;

    @Inject
    PivoUtils utils;

    @Inject
    S3PreSignerConfiguration configuration;

    private S3PreSignerSDK() {
    }

    public PreSignedURL generatePreSignedURL(String key, String bucketName) {
        log.debug("bucket name: {}", bucketName);
        try (S3Presigner client = S3Presigner.builder().region(utils.getRegionFromBucket(bucketName)).build()) {
            PreSignedURL result = this.generateGetPreSignedURL(client, key, bucketName);

            return PreSignedURL.builder()
                    .url(result.getUrl())
                    .ttl(result.getTtl())
                    .build();
        }
    }

    private PreSignedURL generateGetPreSignedURL(S3Presigner client, String key, String bucketName) {
        Response result = redis.get(RedisPrefix.S3_GET.getName(key, bucketName));
        if (null != result) {
            try {
                log.debug("Found get url from redis!");
                long ttl = redis.ttl(RedisPrefix.S3_GET.getName(key, bucketName));
                if (ttl > configuration.VALIDATE_TIME()) {
                    return PreSignedURL.builder()
                            .url(result.toString())
                            .ttl(ttl)
                            .build();
                }
                log.debug("TTL is almost expire, generate new one");
            } catch (Exception ignore) {
                redis.del(RedisPrefix.S3_GET.getName(key, bucketName));
                log.error("failed to get url from redis");
            }
        }
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(configuration.TTL()))
                .getObjectRequest(request)
                .build();

        PresignedGetObjectRequest presignedRequest = client.presignGetObject(presignRequest);
        String url = presignedRequest.url().toString();

        log.debug("get url: {}", url);

        redis.setWithExpire(RedisPrefix.S3_GET.getName(key, bucketName), url, configuration.TTL());

        return PreSignedURL.builder()
                .url(url)
                .ttl(configuration.TTL())
                .build();
    }

}
