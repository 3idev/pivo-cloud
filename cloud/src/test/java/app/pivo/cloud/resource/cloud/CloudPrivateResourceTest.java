package app.pivo.cloud.resource.cloud;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@QuarkusTest
@TestHTTPEndpoint(CloudPrivateResource.class)
public class CloudPrivateResourceTest {

    @ConfigProperty(name = "auth.access-token")
    String accessToken;

    @Test
    public void test_generate_cognito_token_without_credentials() {
        when()
                .get("/me")
                .then()
                .statusCode(401);
    }

    @Test
    public void test_generate_cognito_token_with_credentials() {
        given()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .get("/me")
                .then()
                .statusCode(200)
                .body("isOk", is(true),
                        "data.identityId", is(instanceOf(String.class)),
                        "data.token", is(instanceOf(String.class)),
                        "data.ttl", is(instanceOf(Integer.class)));
    }

    @Test
    public void test_generate_shareable_url() {
        final Map<String, String> payload = new HashMap<>() {
            {
                put("path", "/images/hamster.gif");
            }
        };

        given()
                .body(payload)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post("/share")
                .then()
                .statusCode(200)
                .body("isOk", is(true),
                        "data.url", is(instanceOf(String.class)),
                        "data.ttl", is(instanceOf(Integer.class)));
    }

}
