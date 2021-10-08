package app.pivo.cloud.resource.cloud.dto;

import app.pivo.common.define.UserLocation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CloudPrivateDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MakeShareableURL {

        @NotBlank(message = "path may not be empty")
        private String path;

        @NotBlank(message = "ttl may not be empty")
        @Positive(message = "ttl only allow positive value")
        private Long ttl;

        private UserLocation location;

    }

//    @Data
//    @Builder
//    @ToString
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SoftDeleteObject {
//
//        @QueryParam("path")
//        @NotBlank(message = "path may not be blank")
//        private String path;
//
//        @QueryParam("deleteAfter")
//        private Long deleteAfter;
//
//    }
//
//    @Data
//    @Builder
//    @ToString
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class HardDeleteObject {
//
//        @QueryParam("path")
//        @NotBlank(message = "path may not be blank")
//        private String path;
//
//        @QueryParam("location")
//        private UserLocation location;
//
//    }

}
