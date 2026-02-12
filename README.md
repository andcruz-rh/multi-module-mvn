# Example Multi Module - Camel Quarkus

Proyecto POC multi-módulo Maven con **Apache Camel Quarkus** que expone múltiples rutas REST desde módulos independientes, integradas en una sola aplicación.

## Estructura del proyecto

```
multi-module-mvn/
├── pom.xml                        # Parent POM (gestión de dependencias y plugins)
├── route-one/                     # Módulo librería: ruta Camel "ruta 1"
│   ├── pom.xml
│   └── src/main/java/.../RouteOneBuilder.java
├── route-two/                     # Módulo librería: ruta Camel "ruta 2"
│   ├── pom.xml
│   └── src/main/java/.../RouteTwoBuilder.java
└── app/                           # Aplicación Quarkus (integra todos los módulos)
    ├── pom.xml
    └── src/
        ├── main/resources/application.properties
        └── test/java/.../RoutesTest.java
```

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17+ |
| Quarkus | 3.15.1 |
| Apache Camel Quarkus | 3.15.0 |
| Apache Camel | 4.8.0 |
| Maven | 3.9+ |

### Extensiones Camel Quarkus utilizadas

- `camel-quarkus-rest` — DSL REST de Camel para definir endpoints
- `camel-quarkus-platform-http` — Servidor HTTP basado en Vert.x
- `camel-quarkus-direct` — Componente para enrutamiento interno síncrono
- `camel-quarkus-microprofile-health` — Health checks con MicroProfile

## Arquitectura

Los módulos `route-one` y `route-two` son **librerías JAR** que contienen únicamente clases `RouteBuilder` con rutas Camel. No son aplicaciones ejecutables por sí mismas.

El módulo `app` es la **aplicación Quarkus** que agrega ambas librerías como dependencias. Al arrancar, Quarkus descubre automáticamente los beans `@ApplicationScoped` en los módulos gracias al **índice Jandex** generado en tiempo de compilación (`jandex-maven-plugin`).

Todas las rutas se registran en un **único contexto Camel** y se sirven en el **mismo puerto HTTP**.

## Endpoints

| Método | Ruta | Respuesta | Content-Type |
|---|---|---|---|
| GET | `/api/route-one` | `ruta 1` | `text/plain` |
| GET | `/api/route-two` | `ruta 2` | `text/plain` |
| GET | `/q/health` | Health check general | `application/json` |
| GET | `/q/health/live` | Liveness probe | `application/json` |
| GET | `/q/health/ready` | Readiness probe | `application/json` |

## Requisitos previos

- **JDK 17** o superior
- **Maven 3.9+**
- (Opcional) **GraalVM** para compilación nativa

## Compilar el proyecto

```bash
mvn clean verify
```

## Ejecutar en modo desarrollo

```bash
cd app
mvn quarkus:dev
```

La aplicación arranca en `http://localhost:8080`. Quarkus Dev Mode habilita recarga automática de código.

### Probar los endpoints

```bash
curl http://localhost:8080/api/route-one
# ruta 1

curl http://localhost:8080/api/route-two
# ruta 2

curl http://localhost:8080/q/health
# {"status":"UP", ...}
```

## Ejecutar tests

```bash
mvn test
```

Los tests de integración (`@QuarkusTest`) se encuentran en el módulo `app` y cubren:

- Respuesta exitosa de cada ruta (GET 200)
- Verificación parameterizada de cuerpo y content-type
- Rutas no existentes (404)
- Métodos HTTP no permitidos — POST, PUT, DELETE (405)
- Health checks (liveness y readiness)

## Empaquetar para producción

```bash
mvn clean package -DskipTests
```

El JAR ejecutable se genera en `app/target/quarkus-app/`.

```bash
java -jar app/target/quarkus-app/quarkus-run.jar
```

## Compilación nativa (GraalVM)

```bash
mvn clean package -Pnative -DskipTests
```

```bash
./app/target/app-1.0.0-SNAPSHOT-runner
```

## Agregar un nuevo módulo de ruta

1. Crear un nuevo módulo Maven (ej. `route-three/`)
2. Agregar las dependencias `camel-quarkus-rest`, `camel-quarkus-direct` y `quarkus-arc`
3. Agregar el plugin `jandex-maven-plugin` al build
4. Crear una clase `@ApplicationScoped` que extienda `RouteBuilder`
5. Registrar el módulo en el `pom.xml` padre (`<modules>`)
6. Agregar la dependencia en `app/pom.xml`

El nuevo `RouteBuilder` será descubierto automáticamente por Quarkus al arrancar.

