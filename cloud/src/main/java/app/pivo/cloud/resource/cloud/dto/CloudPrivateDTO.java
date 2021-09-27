package app.pivo.cloud.resource.cloud.dto;

import app.pivo.common.define.UserLocation;
import lombok.*;

public class CloudPrivateDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SoftDeleteObject {

        private String path;

        private Long deleteAfter;

    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HardDeleteObject {

        private String path;

        private UserLocation location;

    }

}
