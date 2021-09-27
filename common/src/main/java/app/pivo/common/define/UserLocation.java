package app.pivo.common.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
