package app.pivo.cloud.domain;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class URLWithTTL {

    private String url;

    private long ttl;

}
