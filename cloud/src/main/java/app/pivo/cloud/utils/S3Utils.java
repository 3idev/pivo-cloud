package app.pivo.cloud.utils;

import java.util.regex.Pattern;

public class S3Utils {

    private static final Pattern FILE_PATTERN = Pattern.compile("(.*)(.)(.*)$", Pattern.CASE_INSENSITIVE);

    public static String pathResolve(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            sb.append(paths[i]);
            if (i == paths.length - 1) {
                if (!paths[i].contains(".")) {
                    sb.append("/");
                }
            } else if (i < paths.length - 1) {
                if (!paths[i + 1].startsWith("/")) {
                    sb.append("/");
                }
            }
        }

        return sb.toString();


//        StringBuilder sb = new StringBuilder();
//        for (String path : paths) {
//            sb.append(path);
//            if (!FILE_PATTERN.matcher(path).find()) {
//                sb.append("/");
//            } else if (!path.endsWith("/")) {
//                sb.append("/");
//            }
//        }
//
//        return sb.toString();
    }

}
