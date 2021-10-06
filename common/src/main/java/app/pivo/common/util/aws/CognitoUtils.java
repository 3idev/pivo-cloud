package app.pivo.common.util.aws;

public class CognitoUtils {

    public static String pathResolve(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path);
            if (!path.endsWith("/")) {
                sb.append("/");
            }
        }

        return sb.toString();
    }

}
