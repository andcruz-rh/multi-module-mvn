package com.banreservas.ivr.routeone;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

/**
 * Ruta Camel que expone un endpoint REST que responde "ruta 1".
 */
@ApplicationScoped
public class RouteOneBuilder extends RouteBuilder {

    private static final String DIRECT_ROUTE_ONE = "direct:route-one";
    private static final String ROUTE_ONE_RESPONSE = "ruta 1";

    @Override
    public void configure() {
        rest("/api")
            .get("/route-one")
            .description("Endpoint que responde ruta 1")
            .produces("text/plain")
            .to(DIRECT_ROUTE_ONE);

        from(DIRECT_ROUTE_ONE)
            .routeId("route-one")
            .log("Procesando solicitud en ruta 1")
            .setBody(constant(ROUTE_ONE_RESPONSE));
    }
}

