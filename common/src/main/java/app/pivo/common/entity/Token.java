package app.pivo.common.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "tokens")
public class Token {

    @BsonId
    private ObjectId _id;

    private String accessToken;
    private String refreshToken;

    private Authz authz;

    private User user;
    private String userId;
    private String deviceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
