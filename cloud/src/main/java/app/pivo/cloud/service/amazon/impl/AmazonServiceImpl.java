package app.pivo.cloud.service.amazon.impl;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.cloud.service.amazon.sdk.CognitoSDK;
import app.pivo.cloud.service.amazon.sdk.S3PreSignerSDK;
import app.pivo.cloud.service.amazon.sdk.S3SDK;
import app.pivo.cloud.service.amazon.sdk.configuration.S3Configuration;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;
import app.pivo.common.repository.CognitoAccountRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class AmazonServiceImpl implements AmazonService {

    @Inject
    S3Configuration aws;

    @Inject
    S3SDK s3;

    @Inject
    CognitoSDK cognito;

    @Inject
    S3PreSignerSDK presigner;

    @Inject
    CognitoAccountRepository cognitoAccountRepository;

    @Override
    public boolean checkObject(User user, S3Bucket bucket) {
        try {
            return s3.checkObjectIsExists(user, bucket);
        } catch (NoSuchKeyException e) {
            log.warn("We can't find the key: {}", user.getId());
            return false;
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<S3Bucket> checkObjectInEveryBuckets(User user) {
        try {
            return aws.buckets()
                    .values()
                    .stream()
                    .map(S3Bucket::from)
                    .filter(bucket -> s3.checkObjectIsExists(user, bucket))
                    .collect(Collectors.toList());
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean checkObject(String key, S3Bucket bucket) {
        try {
            return s3.checkObjectIsExists(key, bucket);
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<S3Bucket> checkObjectInEveryBuckets(String key) {
        try {
            return aws.buckets().values()
                    .stream()
                    .map(S3Bucket::from)
                    .filter(bucket -> s3.checkObjectIsExists(key, bucket))
                    .collect(Collectors.toList());
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

    @Override
    public PreSignedURL makePreSignedURL(String path, S3Bucket bucket) {
        return presigner.generatePreSignedURL(path, bucket);
    }

    @Override
    public void initializeUserResource(String prefix, S3Bucket bucket) {
        Uni.createFrom()
                .voidItem()
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe()
                .with(ignore -> this.initializeUserResourceWorker(prefix, bucket));
    }

    @Override
    public CognitoAccount createCognitoAccount(User user) {
        final CognitoToken res = this.issueToken(user);

        Optional<CognitoAccount> maybeCloudAccount = cognitoAccountRepository.findByUserIdOptional(user.getId());
        if (maybeCloudAccount.isEmpty()) {
            log.debug("Try to create CognitoAccount");
            CognitoAccount cognitoAccount = CognitoAccount.builder()
                    .cognitoId(res.getIdentityId())
                    .userId(user.getId())
                    .build();
            log.debug("CognitoAccount: {}", cognitoAccount.toString());
            cognitoAccountRepository.persist(cognitoAccount);

            return cognitoAccount;
        }

        return maybeCloudAccount.get();
    }

    @Override
    public CognitoToken issueToken(User user) {
        return cognito.issueToken(user);
    }

    private Uni<Void> initializeUserResourceWorker(String root, S3Bucket bucket) {
        log.debug("Start to make folders");
        try {
            this.s3.initializeUserResourceFolder(root, bucket);
        } catch (Exception err) {
            log.error("Failed to make default folders", err);
        }

        log.debug("initializeUserResource is Done");
        return Uni.createFrom().voidItem();
    }

}
