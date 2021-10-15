package app.pivo.cloud.service.amazon.sdk.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "aws.cognito")
public interface CognitoConfiguration {

    @WithName("identity-pool-id")
    String IDENTITY_POOL_ID();

    @WithName("time-to-live")
    Long TTL();

    @WithName("provider")
    String PROVIDER();

    @WithName("validate-time")
    Long VALIDATE_TIME();

}
