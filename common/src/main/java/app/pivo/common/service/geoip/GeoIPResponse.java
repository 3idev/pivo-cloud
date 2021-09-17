package app.pivo.common.service.geoip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoIPResponse {

    private String ip;
    private String hostname;
    private String continent_code;
    private String continent_name;

    private Boolean is_eu;

    public Boolean getIs_eu() {
        if (null != continent_code) {
            if (continent_code.equalsIgnoreCase("EU")) {
                return true;
            }
        }

        return is_eu;
    }

}
