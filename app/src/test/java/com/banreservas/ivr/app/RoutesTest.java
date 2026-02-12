package com.banreservas.ivr.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class RoutesTest {

    private static final String ROUTE_ONE_PATH = "/api/route-one";
    private static final String ROUTE_TWO_PATH = "/api/route-two";
    private static final String ROUTE_ONE_RESPONSE = "ruta 1";
    private static final String ROUTE_TWO_RESPONSE = "ruta 2";
    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    // --- Successful Retrieval (GET) ---

    @Test
    void whenGetRouteOne_thenReturnsRutaUno() {
        given()
            .when()
            .get(ROUTE_ONE_PATH)
            .then()
            .statusCode(200)
            .body(is(ROUTE_ONE_RESPONSE));
    }

    @Test
    void whenGetRouteTwo_thenReturnsRutaDos() {
        given()
            .when()
            .get(ROUTE_TWO_PATH)
            .then()
            .statusCode(200)
            .body(is(ROUTE_TWO_RESPONSE));
    }

    // --- Parameterized: verify each route returns correct response and content type ---

    @ParameterizedTest(name = "GET {0} should return \"{1}\"")
    @CsvSource({
        "/api/route-one, ruta 1",
        "/api/route-two, ruta 2"
    })
    void whenGetRoute_thenReturnsExpectedBody(String path, String expectedBody) {
        given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .contentType(CONTENT_TYPE_TEXT_PLAIN)
            .body(is(expectedBody));
    }

    // --- Not Found: non-existing endpoints ---

    @ParameterizedTest(name = "GET {0} should return 404")
    @ValueSource(strings = {
        "/api/route-three",
        "/api/unknown",
        "/invalid",
        "/api",
        "/api/route-one/extra"
    })
    void whenGetNonExistingRoute_thenReturns404(String path) {
        given()
            .when()
            .get(path)
            .then()
            .statusCode(404);
    }

    // --- Method Not Allowed: POST/PUT/DELETE on GET-only endpoints ---

    @ParameterizedTest(name = "POST {0} should return 405")
    @ValueSource(strings = {ROUTE_ONE_PATH, ROUTE_TWO_PATH})
    void whenPostToRoute_thenReturns405(String path) {
        given()
            .when()
            .post(path)
            .then()
            .statusCode(405);
    }

    @ParameterizedTest(name = "PUT {0} should return 405")
    @ValueSource(strings = {ROUTE_ONE_PATH, ROUTE_TWO_PATH})
    void whenPutToRoute_thenReturns405(String path) {
        given()
            .when()
            .put(path)
            .then()
            .statusCode(405);
    }

    @ParameterizedTest(name = "DELETE {0} should return 405")
    @ValueSource(strings = {ROUTE_ONE_PATH, ROUTE_TWO_PATH})
    void whenDeleteToRoute_thenReturns405(String path) {
        given()
            .when()
            .delete(path)
            .then()
            .statusCode(405);
    }

    // --- Health Check ---

    @Test
    void whenGetHealth_thenReturnsUp() {
        given()
            .when()
            .get("/q/health")
            .then()
            .statusCode(200);
    }

    @Test
    void whenGetHealthLive_thenReturnsUp() {
        given()
            .when()
            .get("/q/health/live")
            .then()
            .statusCode(200);
    }

    @Test
    void whenGetHealthReady_thenReturnsUp() {
        given()
            .when()
            .get("/q/health/ready")
            .then()
            .statusCode(200);
    }
}
