package app.pivo.cloud.service.amazon.sdk.configuration;

import io.smallrye.config.ConfigMapping;

import java.util.Map;

@ConfigMapping(prefix = "aws.s3", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface S3Configuration {

    Map<String, String> buckets();

    String fallbackBucket();

}
