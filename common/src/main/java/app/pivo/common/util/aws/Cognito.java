package app.pivo.common.util.aws;

import app.pivo.common.define.RedisPrefix;
import app.pivo.common.domain.CognitoToken;
import app.pivo.common.entity.User;
import app.pivo.common.repository.RedisRepository;
import app.pivo.common.util.aws.configuration.CognitoConfiguration;
import io.quarkus.runtime.configuration.ProfileManager;
import io.vertx.redis.client.Response;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class Cognito {

    @Inject
    CognitoConfiguration configuration;

    @Inject
    RedisRepository redis;

    public CognitoToken issueToken(User user) {
        Response redisResponse = redis.get(RedisPrefix.COGNITO.getName(user.get_id()));
        if (null != redisResponse) {
            try {
                String[] infos = redisResponse.toString().split("/");
                if (infos[0].isEmpty() || infos[1].isEmpty()) {
                    redis.del(RedisPrefix.COGNITO.getName(user.get_id()));
                    log.debug("CognitoToken format is different");
                    throw new Exception("CognitoToken format is different");
                }
                long ttl = redis.ttl(RedisPrefix.COGNITO.getName(user.get_id()));
                if (ttl > configuration.VALIDATE_TIME()) {
                    return CognitoToken.builder()
                            .identityId(infos[0])
                            .token(infos[1])
                            .ttl(ttl)
                            .build();
                }
            } catch (Exception ignore) {
                log.warn("Failed to get CognitoToken from redis");
            }
        }

        try (CognitoIdentityClient client = generateClient()) {
            GetOpenIdTokenForDeveloperIdentityRequest request = GetOpenIdTokenForDeveloperIdentityRequest.builder()
                    .identityPoolId(configuration.IDENTITY_POOL_ID())
                    .logins(this.generateLogins(user))
                    .principalTags(generatePrincipalTags())
                    .tokenDuration(configuration.TTL())
                    .build();

            GetOpenIdTokenForDeveloperIdentityResponse res = client.getOpenIdTokenForDeveloperIdentity(request);

            log.debug("GetOpenIdTokenForDeveloperIdentityResponse: {}", res);

            redis.setWithExpire(RedisPrefix.COGNITO.getName(user.get_id()), res.identityId() + "/" + res.token(), configuration.TTL());

            return CognitoToken.builder()
                    .token(res.token())
                    .ttl(configuration.TTL())
                    .build();
        }
    }

    protected CognitoIdentityClient generateClient() {
        return CognitoIdentityClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("dev"))
                .build();
    }

    private Map<String, String> generateLogins(User user) {
        Map<String, String> returnMap = new HashMap<>();
        {
            returnMap.put(configuration.PROVIDER(), user.get_id()); // Set user's uuid as unique id for cognito
        }

        return returnMap;
    }

    private Map<String, String> generatePrincipalTags() {
        String profile = ProfileManager.getActiveProfile();
        Map<String, String> returnMap = new HashMap<>();
        {
            returnMap.put("environment", profile);
        }

        return returnMap;
    }

}
