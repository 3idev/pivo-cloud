package app.pivo.common.util.aws;

import software.amazon.awssdk.core.SdkClient;

public abstract class AbstractAWSClient<T extends SdkClient> {

    protected abstract T generateClient(String bucketName);

}
