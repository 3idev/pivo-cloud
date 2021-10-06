package app.pivo.cloud.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum S3Bucket {

    US("us.cloud.pivo.dev", "us"),

    EU("eu.cloud.pivo.dev", "eu");

    private final String name;
    private final String code;

    @Override
    public String toString() {
        return this.name;
    }

    public static S3Bucket from(String str) {
        Optional<S3Bucket> maybeBucket = Arrays.stream(S3Bucket.values()).filter(item -> {
            if (item.name.equalsIgnoreCase(str)) {
                return true;
            }
            return item.code.equalsIgnoreCase(str);
        }).findFirst();
        if (maybeBucket.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return maybeBucket.get();
    }

}
