package app.pivo.common.util.token;

import lombok.Getter;

@Getter
public enum Role {

    USER("user"),
    ADMIN("admin");

    Role(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }

}
