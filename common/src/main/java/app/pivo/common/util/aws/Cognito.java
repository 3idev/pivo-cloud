package app.pivo.common.util.aws;

import app.pivo.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResponse;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class Cognito {

    @ConfigProperty(name = "aws.cognito.identity-pool-id")
    String identityPoolId;

    private final long TOKEN_DURATION = 60 * 5L;

    public void issueToken(User user) {
        try (CognitoIdentityClient client = generateClient()) {
            GetOpenIdTokenForDeveloperIdentityRequest request = GetOpenIdTokenForDeveloperIdentityRequest.builder()
                    .identityPoolId(this.identityPoolId)
                    .logins(this.generateLogins(user))
                    .tokenDuration(this.TOKEN_DURATION)
                    .principalTags(generateLogins(user))
                    .build();
            GetOpenIdTokenForDeveloperIdentityResponse res = client.getOpenIdTokenForDeveloperIdentity(request);

            log.debug("GetOpenIdTokenForDeveloperIdentityResponse: {}", res);
        }
    }

    private CognitoIdentityClient generateClient() {
        return CognitoIdentityClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("dev"))
                .build();
    }

    private Map<String, String> generateLogins(User user) {
        Map<String, String> returnMap = new HashMap<>();
        {
            returnMap.put("", user.get_id());
        }

        return returnMap;
    }

}
