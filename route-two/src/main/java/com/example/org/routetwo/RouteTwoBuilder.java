package com.example.org.routetwo;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

/**
 * Ruta Camel que expone un endpoint REST que responde "ruta 2".
 */
@ApplicationScoped
public class RouteTwoBuilder extends RouteBuilder {

    private static final String DIRECT_ROUTE_TWO = "direct:route-two";
    private static final String ROUTE_TWO_RESPONSE = "ruta 2";

    @Override
    public void configure() {
        rest("/api")
            .get("/route-two")
            .description("Endpoint que responde ruta 2")
            .produces("text/plain")
            .to(DIRECT_ROUTE_TWO);

        from(DIRECT_ROUTE_TWO)
            .routeId("route-two")
            .log("Procesando solicitud en ruta 2")
            .setBody(constant(ROUTE_TWO_RESPONSE));
    }
}

