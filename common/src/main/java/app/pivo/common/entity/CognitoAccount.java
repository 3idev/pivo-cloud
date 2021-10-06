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
    private String _id = UUID.randomUUID().toString();

    private String userId;
    private String cognitoId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
