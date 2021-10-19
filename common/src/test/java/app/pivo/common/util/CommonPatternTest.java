package app.pivo.common.util;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class CommonPatternTest {

    @Test
    public void test_uuid() {
        final String uuid = UUID.randomUUID().toString();

        assertTrue(CommonPattern.UUID.matcher(uuid).find());
    }

    @Test
    public void test_media_folder() {
        final String path = "/ap-northeast-2:" + UUID.randomUUID().toString() + "/images/hamster_punch.gif";

        assertTrue(CommonPattern.MEDIA_FOLDER.matcher(path).find());
    }

}
