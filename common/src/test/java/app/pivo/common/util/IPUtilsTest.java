package app.pivo.common.util;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class IPUtilsTest {

    @Test
    public void test_formatIP() {
        final List<String> ips = Arrays.asList("127.0.0.1", "127.0.0.1:12345");

        for (String ip : ips) {
            assertEquals(ip, "127.0.0.1");
        }
    }

}
