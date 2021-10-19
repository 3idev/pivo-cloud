package app.pivo.common.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "users")
public class User implements Principal {

    @BsonId
    private String id;

    private String username;
    private String email;

    @BsonIgnore
    private String password;

    private String firstName;
    private String lastName;

    public String roleId;

    private String gender;
    private String storeType;
    private LocalDate birthDate;
    private String phoneNumber;

    private String fileName;
    private String thumbName;

    private Boolean verified;
    private Boolean active;
    private Boolean blocked;

    private List<String> type;

    private String version;

    @BsonIgnore
    private String passResetCode;
    @BsonIgnore
    private LocalDate passResetCodeExpiredAt;
    @BsonIgnore
    private String confirmationCode;

    private LocalDate lastLoginAttempt;
    private LocalDate lastLoginSuccess;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    public String transformToObjectKey(String... keys) {
        StringBuilder sb = new StringBuilder(this.id).append("/");
        for (String key : keys) {
            sb.append(key).append("/");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return user.id.equals(this.id) && user.email.equals(this.email);
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.email.hashCode();
        return result;
    }

}
