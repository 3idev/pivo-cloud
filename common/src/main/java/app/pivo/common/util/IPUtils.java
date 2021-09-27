package app.pivo.common.util;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;

import java.util.Arrays;
import java.util.List;

public class IPUtils {

    private static final List<String> IP_HEADERS = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    public static String getRemoteIP(HttpServerRequest request) {
        MultiMap map = request.headers();
        for (String header : IP_HEADERS) {
            if (map.contains(header)) {
                return map.get(header);
            }
        }
        return request.remoteAddress().toString();
    }

    public static String formatIP(String ip) {
        if (ip.contains(":")) {
            return ip.split(":")[0];
        }

        return ip;
    }
}
