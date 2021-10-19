package app.pivo.cloud.service.amazon.sdk;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.sdk.configuration.S3PreSignerConfiguration;
import app.pivo.cloud.utils.CloudUtils;
import app.pivo.common.define.RedisPrefix;
import app.pivo.common.repository.RedisRepository;
import io.vertx.redis.client.Response;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;

@Slf4j
@ApplicationScoped
public class S3PreSignerSDK extends baseSDK {

    @Inject
    RedisRepository redis;

    @Inject
    CloudUtils utils;

    @Inject
    S3PreSignerConfiguration configuration;

    private S3PreSignerSDK() {
    }

    public PreSignedURL generatePreSignedURL(String key, S3Bucket bucket) {
        Region region = utils.getRegionFromBucket(bucket.getName());
        log.debug("bucket name: {}, region: {}", bucket.getName(), region);
        try (S3Presigner client = generateClient(region)) {
            PreSignedURL result = this.generateGetPreSignedURL(client, key, bucket.getName());

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
                log.debug("TTL is almost expired, generate new one");
            } catch (Exception ignore) {
                redis.del(RedisPrefix.S3_GET.getName(key, bucketName));
                log.error("failed to get url from redis");
            }
        }

        log.debug("Signing object...");
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

    private S3Presigner generateClient(Region region) {
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(this.generateCredentials())
                .build();
    }

}
