package app.pivo.common.domain;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedURL {

    private String get;
    private String put;

    private Long ttl;

}
