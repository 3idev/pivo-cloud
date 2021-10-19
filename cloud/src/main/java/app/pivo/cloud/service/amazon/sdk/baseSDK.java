package app.pivo.cloud.service.amazon.sdk;

import io.quarkus.runtime.configuration.ProfileManager;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

@Slf4j
public abstract class baseSDK {

    private final String profile = ProfileManager.getActiveProfile();

    protected AwsCredentialsProvider generateCredentials() {
        if (profile.equals("dev") || profile.equals("test")) {
            return ProfileCredentialsProvider.create("dev");
        } else if (profile.equals("prod")) {
            // FIXME: Refactor this code:
            return InstanceProfileCredentialsProvider.create();
        } else {
            return ProfileCredentialsProvider.create();
        }
    }

}
