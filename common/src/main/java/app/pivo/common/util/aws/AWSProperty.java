package app.pivo.common.util.aws;

import io.smallrye.config.ConfigMapping;

import java.util.Map;

@ConfigMapping(prefix = "aws.s3", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface AWSProperty {

    Map<String, String> buckets();

    String fallbackBucket();

}
