package app.pivo.common.util;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public interface CommonPattern {

    /**
     * UUID
     */
    Pattern UUID = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", CASE_INSENSITIVE);
    String UUID_STR = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    /**
     * $1 = User uuid
     * $2 = images or videos
     * $3 = file name
     */
    Pattern MEDIA_FOLDER = Pattern.compile("^/((.*):" + UUID_STR + ")/(images|videos)/(.*)$", CASE_INSENSITIVE);
    String MEDIA_FOLDER_STR = "/((.*):" + UUID_STR + ")/(images|videos)/(.*)";

    Pattern MEDIA_FOLDER_WITHOUT_UUID = Pattern.compile("^//(images|videos)/(.*)/$", CASE_INSENSITIVE);
    String MEDIA_FOLDER_WITHOUT_UUID_STR = "/(images|videos)/(.*)";

    /**
     * $1 = User uuid
     * $2 = archived
     * $3 = images or videos
     * $4 = file name
     */
    Pattern ARCHIVED_MEDIA_FOLDER = Pattern.compile("^/((.*):" + UUID_STR + ")/(archived)/(images|videos)/(.*)$", CASE_INSENSITIVE);
    String ARCHIVED_MEDIA_FOLDER_STR = "/((.*):" + UUID_STR + ")/(archived)/(images|videos)/(.*)";

    Pattern ARCHIVED_MEDIA_FOLDER_WITHOUT_UUID = Pattern.compile("^/(archived)/(images|videos)/(.*)$");
    String ARCHIVED_MEDIA_FOLDER_WITHOUT_UUID_STR = "/(archived)/(images|videos)/(.*)";

}
