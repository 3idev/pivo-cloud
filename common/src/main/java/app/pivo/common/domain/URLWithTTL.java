package app.pivo.common.domain;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class URLWithTTL {
    private String url;
    private Long ttl;
}
