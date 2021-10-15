package app.pivo.cloud.define;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum UserLocation {

    US("US"),

    EU("EU");

    private final String location;

    @Override
    public String toString() {
        return this.location;
    }

    public static UserLocation fromString(@NonNull final String location) {
        if (location.equalsIgnoreCase("eu")) {
            return UserLocation.EU;
        } else if (location.equalsIgnoreCase("us")) {
            return UserLocation.US;
        }

        return null;
    }

}
