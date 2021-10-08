package app.pivo.common.util.aws;

import java.util.regex.Pattern;

public class CognitoUtils {

    private static final Pattern FILE_PATTERN = Pattern.compile("(.*).(.*)$", Pattern.CASE_INSENSITIVE);

    public static String pathResolve(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path);
            if (!FILE_PATTERN.matcher(path).find()) {
                sb.append("/");
            } else if (!path.endsWith("/")) {
                sb.append("/");
            }
        }

        return sb.toString();
    }

}
