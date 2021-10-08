package app.pivo.common.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "cognitoaccounts")
public class CognitoAccount {

    @BsonId
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String userId;
    private String cognitoId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

}
