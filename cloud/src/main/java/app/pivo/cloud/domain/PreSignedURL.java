package app.pivo.cloud.domain;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedURL {

    private String url;

    private Long ttl;

}
