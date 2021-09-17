package app.pivo.cloud.resource.cloud.dto;

import lombok.*;

public class CloudPrivateDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteObject {

        private String path;

        private Long deleteAfter;

    }

}
