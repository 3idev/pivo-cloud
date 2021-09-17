package app.pivo.common.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class Role {
    private String id;
    private String name;
    private String displayName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BsonCreator
    public Role(
            @BsonProperty("id") String id,
            @BsonProperty("name") String name,
            @BsonProperty("displayName") String displayName,
            @BsonProperty("createdAt") LocalDateTime createdAt,
            @BsonProperty("updatedAt") LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
