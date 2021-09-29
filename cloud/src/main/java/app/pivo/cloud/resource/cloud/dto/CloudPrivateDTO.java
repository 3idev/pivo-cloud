package app.pivo.cloud.resource.cloud.dto;

import app.pivo.common.define.UserLocation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.QueryParam;

public class CloudPrivateDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SoftDeleteObject {

        @QueryParam("path")
        @NotBlank(message = "path may no be blank")
        private String path;

        @QueryParam("deleteAfter")
        private Long deleteAfter;

    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HardDeleteObject {

        @QueryParam("path")
        @NotBlank(message = "path may not be blank")
        private String path;

        @QueryParam("location")
        private UserLocation location;

    }

}
