package de.bitwars;

import de.bitwars.api.models.Player;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class ExampleResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from bitDealer REST"));

    }

    @Test
    void testGetPlayersEndpoint() {
        given()
                .when().get("/players")
                .then()
                .statusCode(200)
                .body(is("[]"));

    }

    @Test
    void testPostPlayersEndpointWithoutAuthorization() {
        given()
                .when()
                .body(new Player(1L, "Peter", "http://fgf.de/"))
                .post("/players")
                .then()
                .statusCode(401)
                .body(Matchers.notNullValue());

    }

    @Test
    void testPostPlayersEndpointWithAuthorization() {
        given()
                .when()
                .auth().basic("admin", "admin")
                .body(new Player(1L, "Peter", "http://myurl.fsit/"))
                .contentType(ContentType.JSON)
                .post("/players")
                .then()
                .statusCode(200)
                .body(is("{\"id\":1,\"name\":\"Peter\",\"provider_url\":\"http://myurl.fsit/\"}"));

    }

}