package com.banreservas.ivr.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class RoutesTest {

    @Test
    void whenGetRouteOne_thenReturnsRutaUno() {
        given()
            .when()
            .get("/api/route-one")
            .then()
            .statusCode(200)
            .body(is("ruta 1"));
    }

    @Test
    void whenGetRouteTwo_thenReturnsRutaDos() {
        given()
            .when()
            .get("/api/route-two")
            .then()
            .statusCode(200)
            .body(is("ruta 2"));
    }
}

