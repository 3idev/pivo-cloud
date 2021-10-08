package app.pivo.common.util.aws.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "aws.s3-presigner")
public interface S3PreSignerConfiguration {

    @WithName("ttl")
    Long TTL();

    @WithName("validate-time")
    Long VALIDATE_TIME();

}
