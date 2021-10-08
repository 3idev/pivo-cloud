package app.pivo.common.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenUser {

    @BsonProperty(value = "id")
    private String id;

    private String username;
    private String email;

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

    private String passResetCode;
    private LocalDate passResetCodeExpiredAt;
    private String confirmationCode;

    private LocalDate lastLoginAttempt;
    private LocalDate lastLoginSuccess;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public User convert() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .roleId(roleId)
                .gender(gender)
                .storeType(storeType)
                .birthDate(birthDate)
                .phoneNumber(phoneNumber)
                .fileName(fileName)
                .thumbName(thumbName)
                .verified(verified)
                .active(active)
                .blocked(blocked)
                .type(type)
                .version(version)
                .passResetCode(passResetCode)
                .passResetCodeExpiredAt(passResetCodeExpiredAt)
                .confirmationCode(confirmationCode)
                .lastLoginAttempt(lastLoginAttempt)
                .lastLoginSuccess(lastLoginSuccess)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
