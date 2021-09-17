package app.pivo.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
public class Authz {
    private Role role;
    private List<String> permissions;

    @JsonCreator
    public Authz(
            @BsonProperty("role") Role role,
            @BsonProperty("permissions") List<String> permissions) {
        this.role = role;
        this.permissions = permissions;
    }
}
