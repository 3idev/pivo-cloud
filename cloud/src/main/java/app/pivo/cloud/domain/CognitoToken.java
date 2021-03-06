package app.pivo.cloud.domain;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CognitoToken {

    private String identityId;

    private String token;

    private Long ttl;

}
