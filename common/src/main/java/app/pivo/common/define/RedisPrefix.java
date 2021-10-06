package app.pivo.common.define;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RedisPrefix {

    S3_GET("S3_GET"),
    S3_PUT("S3_PUT"),
    COGNITO("COGNITO"),
    NONE("");

    private String name;

    public String getName() {
        return this.name;
    }

    public String getName(String key) {
        if (this.name.equals("")) {
            return key;
        }
        return this.name + "/" + key;
    }

    public String getName(String... keys) {
        StringBuilder sb = new StringBuilder();
        if (this.name.equals("")) {
            for (String key: keys) {
                sb.append(key).append("/");
            }
            return sb.toString();
        }
        sb.append(this.name).append("/");
        for (String key: keys) {
            sb.append(key).append("/");
        }

        return sb.toString();
    }

}
