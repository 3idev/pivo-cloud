package app.pivo.cloud.service.amazon.impl;

import app.pivo.cloud.define.S3Bucket;
import app.pivo.cloud.domain.CognitoToken;
import app.pivo.cloud.domain.PreSignedURL;
import app.pivo.cloud.service.amazon.AmazonService;
import app.pivo.cloud.service.amazon.sdk.CognitoSDK;
import app.pivo.cloud.service.amazon.sdk.S3PreSignerSDK;
import app.pivo.cloud.service.amazon.sdk.S3SDK;
import app.pivo.cloud.utils.CognitoUtils;
import app.pivo.common.entity.CognitoAccount;
import app.pivo.common.entity.User;
import app.pivo.common.repository.CognitoAccountRepository;
import app.pivo.common.util.aws.configuration.AWSProperty;
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
    AWSProperty aws;

    @Inject
    S3SDK s3;

    @Inject
    CognitoSDK cognito;

    @Inject
    S3PreSignerSDK s3PreSigner;

    @Inject
    CognitoAccountRepository cognitoAccountRepository;

    @Override
    public boolean checkObject(User user, String bucket) {
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
                    .filter(s -> s3.checkObjectIsExists(user, s))
                    .map(S3Bucket::from)
                    .collect(Collectors.toList());
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean checkObject(String key, String bucket) {
        try {
            return s3.checkObjectIsExists(key, bucket);
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkObjectInEveryBuckets(String key) {
        try {
            return aws.buckets().values()
                    .stream()
                    .anyMatch(s -> s3.checkObjectIsExists(key, s));
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public PreSignedURL makePreSignedURL(String path, String bucket) {
        return s3PreSigner.generatePreSignedURL(path, bucket);
    }

    @Override
    public void initializeUserResource(String prefix, String bucket) {
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

    private Uni<Void> initializeUserResourceWorker(String root, String bucket) {
        log.debug("Start to make folders");
        try {
            // Create root folder for user
            log.debug("Making root folder");
            s3.createFolder(CognitoUtils.pathResolve(root), bucket);
            // Create images folder
            log.debug("Making image folder");
            s3.createFolder(CognitoUtils.pathResolve(root, "images"), bucket);
            // Create videos folder
            log.debug("Making video folder");
            s3.createFolder(CognitoUtils.pathResolve(root, "videos"), bucket);
            // Create archive folder
            log.debug("Making archive root folder");
            s3.createFolder(CognitoUtils.pathResolve(root, "archived"), bucket);
            log.debug("Making image folder in archive folder");
            s3.createFolder(CognitoUtils.pathResolve(root, "archived", "images"), bucket);
            log.debug("Making video folder in archive folder");
            s3.createFolder(CognitoUtils.pathResolve(root, "archived", "videos"), bucket);
        } catch (Exception ignore) {
            log.error("Failed to make default folders");
        }

        log.debug("initializeUserResource is Done");
        return Uni.createFrom().voidItem();
    }

}
