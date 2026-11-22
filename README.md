# Delivery App API

[![Java](https://img.shields.io/badge/Java-21-437291?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![Maven](https://img.shields.io/badge/Maven-3.9.15-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![H2](https://img.shields.io/badge/H2-in--memory-04496B?logo=h2&logoColor=white)](https://www.h2database.com/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.1-6BA539?logo=openapiinitiative&logoColor=white)](http://localhost:8080/swagger)

Role-based REST API for a food-delivery backend: consumers order from nearby stores, merchants run the catalog and move orders forward, riders take confirmed deliveries, and an administrator oversees users and orders.

Academic final project for the Universidad Tecnológica Nacional (UTN). The repository name and the original README identify it as the backend of that project. It is a stateless HTTP API backed by an in-memory database that is recreated on every start.

## What it solves

A delivery marketplace needs one API that four kinds of users can share without sharing the same permissions:

- A **consumer** has to find stores and products near a point, place an order, and read that order later.
- A **merchant** has to own stores and products, and accept, cancel, or hand off orders for those stores.
- A **rider** has to see confirmed orders that nobody has taken, claim one, update where it is, and mark it delivered.
- An **administrator** has to list and manage users, stores, products, and every order.

The API keeps those rules in Spring Security and in the service layer, and it only allows the order-status changes coded for each role.

## Features

- Registration and login that return a signed JWT. Public registration accepts `ROLE_CONSUMER`, `ROLE_MERCHANT`, and `ROLE_RIDER`. `ROLE_ADMINISTRATOR` is rejected with HTTP 403.
- BCrypt password hashing and a stateless session policy. Protected routes expect `Authorization: Bearer <token>`.
- CRUD for users, stores, products, and orders, with role rules on each method.
- Self-service profile at `/api/v1/users/me` for consumer, merchant, and rider. An administrator token receives HTTP 403 on that path.
- Paginated search with allow-listed `sortBy` / `direction` values.
- Dynamic filters built with Spring Data JPA `Specification`s (name, price, status, payment type, totals, and a distance box).
- Consumers must send `latitude`, `longitude`, and `distance` when listing stores or products. Missing any of them returns HTTP 400.
- Order totals are computed on the server from product prices. A product in `UNAVAILABLE` status is rejected when the order is created.
- A fixed status machine for merchants and riders (see below). Any other jump returns HTTP 400.
- Bean Validation on request bodies, and one error JSON from `GlobalExceptionHandler`.
- OpenAPI UI and an H2 console for local inspection.
- A `DataSeeder` that loads demo users, stores, products, and orders on startup.

## Tech stack

Versions below are taken from `pom.xml` or, where noted, from the log of a local start.

| Piece | Version | Where it is set |
| --- | --- | --- |
| Java | 21 | `pom.xml` `java.version` |
| Spring Boot | 4.0.6 | parent POM |
| Spring Security | managed by Spring Boot | `spring-boot-starter-security` |
| Spring Data JPA / Hibernate | Hibernate ORM 7.2.12.Final on this run | `spring-boot-starter-data-jpa` |
| Servlet container | Apache Tomcat 11.0.21 on this run | Spring Boot |
| H2 | 2.4.240 on this run | `com.h2database:h2` |
| JWT | jjwt 0.11.5 (HS256) | `pom.xml` |
| Bean Validation | Jakarta Validation via starter | `spring-boot-starter-validation` |
| OpenAPI | springdoc 3.0.2 | `pom.xml` |
| Sample data | Datafaker 2.5.4, locale `es` | `pom.xml`, `DataSeeder` |
| Lombok | managed by Spring Boot | `pom.xml` |
| Build | Maven Wrapper 3.9.15 | `.mvn/wrapper/maven-wrapper.properties` |

The documented API version is `1.0.0` (`app.custom.api.version` in `application.yaml`). The Maven artifact version is `0.0.1-SNAPSHOT`.

## Architecture

Packages are split by feature. Each of `user`, `store`, `product`, and `order` has a controller, service, repository, JPA model, DTOs, mapper, and domain exceptions. Search modules also have a filter object and a `Specification` class. Shared security, pagination, errors, and the seeder live under `common`.

```mermaid
flowchart LR
  Client[HTTP client] --> Jwt[JwtFilter]
  Jwt --> Sec[SecurityFilterChain]
  Sec --> Api["REST controllers /api/v1"]
  Api --> Svc[Services]
  Svc --> Spec[JPA Specifications]
  Svc --> Repo[Spring Data repositories]
  Repo --> Db[(H2 in memory)]
  Seed[DataSeeder] --> Repo
```

`JwtFilter` reads the Bearer token, loads the user by email, and sets the security context. `SecurityFilterChain` is stateless, CSRF is disabled, and H2 console frames are allowed from the same origin. `AuthFacadeService` exposes the current user and role to the services. Services map entities to response DTOs. `UserResponseDTO` carries id, name, last name, email, and role.

### Order status

```mermaid
stateDiagram-v2
  [*] --> PENDING: consumer creates the order
  PENDING --> CONFIRMED: owning merchant
  PENDING --> CANCELLED: owning merchant
  CONFIRMED --> CONFIRMED_ASSIGNED: rider claims it
  CONFIRMED_ASSIGNED --> DISPATCHED: owning merchant
  DISPATCHED --> COMPLETED: rider
```

Those are the only transitions `OrderService` accepts. The merchant path checks that the caller owns the store. The rider path sets the order's rider to the authenticated user. `GET /api/v1/orders/unassigned` returns orders in `CONFIRMED` whose rider is null.

Payment types are `EFECTIVO` and `TRANSFERENCIA`.

An order stores the delivery point (`customerLatitude`, `customerLongitude`, `customerAddress`) and a separate `latitude` / `longitude`. On create, that second pair is copied from the store. A rider can replace it with `PATCH /api/v1/orders/{id}/location`.

## Project structure

```text
src/main/java/com/deliveryapp/backend/
├── BackendApplication.java
├── common/
│   ├── config/          SecurityConfig, OpenAPIConfig
│   ├── data/            DataSeeder
│   ├── exceptions/      GlobalExceptionHandler, ErrorMessage
│   ├── filters/         JwtFilter
│   ├── pagination/      PaginationQuery, PaginationResult
│   └── services/        JwtService, AuthFacadeService
├── user/                auth + user administration
├── store/
├── product/
└── order/
    └── model/           Order, join table order_product
src/main/resources/application.yaml
```

There is no `src/test` tree. The generated `BackendApplicationTests` class was removed in commit `b306c2b`.

Relationships in the JPA model:

- `User` 1—N `Store` (owner), 1—N `Order` as consumer, 1—N `Order` as rider
- `Store` 1—N `Product`
- `Order` N—N `Product` through `order_product`

## Run locally

Verified with JDK 21 (`21.0.13`) by running `./mvnw spring-boot:run` and calling the API on port 8080.

Requirements: JDK 21. Maven Wrapper 3.9.15 is included, so a separate Maven install is optional. The wrapper scripts `mvnw` and `mvnw.cmd` were already in the repo; they need `.mvn/wrapper/maven-wrapper.properties` (Maven 3.9.15), which is part of the project.

`application.yaml` reads the signing key and the two timing values from the environment, with no defaults. Export them before startup. The secret below is the development value already commented in `application.yaml`.

```bash
export JWT_SECRET="ID32WQ74uayoGI3nW7o9MEZKoaNzDongyYltCai2aY8kdWIqlrn4dmnbA3EFngsi"
export JWT_EXPIRATION=86400000
export JWT_REFRESH_WINDOW=604800000

./mvnw spring-boot:run
```

`JWT_SECRET` must be Base64. jjwt decodes it and signs with HS256. `86400000` and `604800000` are the commented 24-hour lifetime and 7-day figure.

`JwtService` stores the expiration and the refresh window in `static` fields. On startup Spring Boot logs that `@Value` is not applied to those static fields, and a token issued during this run had `exp` equal to `iat`. `JwtFilter` still authenticates the call: an expired token that `canBeTokenRenewed` accepts is allowed through, and the response carries a replacement token in the `Authorization` header. Use the token from the latest response when you chain calls.

A successful start logs `Started BackendApplication` and `Datos de prueba cargados`. On the verification run Tomcat came up on port 8080 in 2.823 seconds.

| URL | What it is |
| --- | --- |
| `http://localhost:8080/swagger` | Redirects to Swagger UI (`/swagger-ui/index.html`). Public. |
| `http://localhost:8080/v3/api-docs` | OpenAPI document. Title `Delivery App API`, version `1.0.0`, 16 paths. Public. |
| `http://localhost:8080/h2-console` | H2 console (redirects to `/h2-console/`). JDBC URL `jdbc:h2:mem:deliveryappdb`, user `admin`, empty password. |

The datasource is `jdbc:h2:mem:deliveryappdb` with `ddl-auto: create-drop`. Stopping the process drops the data. The seeder deletes and reinserts its rows on every boot.

### Seeded accounts

Defined in `DataSeeder` and recreated on startup. These are demo passwords stored in source, hashed with BCrypt at boot.

| Email | Password | Role |
| --- | --- | --- |
| `juanSky@mail.com` | `passDeJuan1` | `ROLE_CONSUMER` |
| `lucas@mail.com` | `passDeLucas` | `ROLE_CONSUMER` |
| `pedronight@mail.com` | `passDePedro1` | `ROLE_MERCHANT` |
| `monicaazul@mail.com` | `passDeMonica` | `ROLE_MERCHANT` |
| `ana@mail.com` | `passDeAna` | `ROLE_RIDER` |
| `maria@mail.com` | `passDeMaria` | `ROLE_ADMINISTRATOR` |

On the verification run, immediately after startup, the API reported 6 users, 22 stores, 154 products, and 4 orders. The fixed stores include `Pizza Hut de Pedro` (Calle 13, -37.9940443, -57.5552022, owned by Pedro) and `Lomitos Hut de Monica`. Twenty other stores and most products are generated with Datafaker (Spanish locale), so their names change between boots. The count does not. Store 1 also has a fixed product `Napolitana` (price 10.50, status `AVAILABLE`). The four seeded orders belong to `juanSky@mail.com` at store 1 and cover `PENDING`, `CONFIRMED`, `COMPLETED`, and `CANCELLED`. The seeded `CONFIRMED` order already has rider Ana, so `GET /api/v1/orders/unassigned` is empty until a merchant confirms another pending order.

## API

Base path: `/api/v1`. Send JSON (`Content-Type: application/json`) and, except for register and login, `Authorization: Bearer <token>`.

A call with no token to a protected path returns **HTTP 403** with an empty body.

Validation and domain errors use this body:

```json
{
  "message": "Error en validacion",
  "exception": "MethodArgumentNotValidException",
  "path": "/api/v1/auth/register",
  "errors": {
    "name": "El nombre no puede estar vacío",
    "email": "El email debe tener un formato válido",
    "password": "La contraseña debe tener entre 6 y 20 caracteres"
  }
}
```

That payload is the HTTP 400 response from `POST /api/v1/auth/register` with an empty name, `not-an-email`, and a one-character password. Field messages in the code are written in Spanish.

### Pagination

`page` (default 0), `size` (default 10), `sortBy` (default `id`), `direction` (default `asc`). `direction` accepts `asc` or `desc`. Anything else returns HTTP 400.

| Resource | Allowed `sortBy` |
| --- | --- |
| Users | `id`, `name`, `lastName`, `email`, `role` |
| Stores | `id`, `name`, `address` |
| Products | `id`, `name`, `price`, `status` |
| Orders for one store | `id`, `total`, `status`, `paymentType`, `lastUpdate` |
| All orders (admin) | `id`, `total`, `status`, `paymentType`, `lastUpdate`, `customerAddress` |

List responses look like this:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalPages": 0,
  "totalElements": 0
}
```

`GET /api/v1/orders/my-orders` and `GET /api/v1/orders/unassigned` return a JSON array, not a page.

### Filters

Omitted parameters are ignored. Text filters are case-insensitive substrings.

| List | Query parameters |
| --- | --- |
| `GET /api/v1/users` | `id`, `name`, `lastName`, `email`, `role` |
| `GET /api/v1/stores` | `name`, `latitude`, `longitude`, `distance` |
| `GET /api/v1/products` | `name`, `description`, `priceMin`, `priceMax`, `status`, `storeId`, `latitude`, `longitude`, `distance` |
| `GET /api/v1/orders/store/{storeId}` | `status`, `paymentType`, `consumerId`, `totalMin`, `totalMax` |
| `GET /api/v1/orders` | `id`, `riderId`, `consumerId`, `storeId`, `customerAddress`, `totalMin`, `totalMax`, `status`, `paymentType`, `lastUpdateFrom`, `lastUpdateTo` |

`distance` is an integer. The store and product specifications convert it to degrees with `distance / 111` and keep rows whose latitude and longitude both fall inside that box. For products, the box is applied to the product's store.

### Access by route

Roles in the table are the Spring Security rules in `SecurityConfig`. Some routes add a service check, noted in the last column.

| Method and path | Roles | Also checked in the service |
| --- | --- | --- |
| `POST /api/v1/auth/register` | public | Role must be consumer, merchant, or rider |
| `POST /api/v1/auth/login` | public | |
| `GET /api/v1/users` | administrator | |
| `GET /api/v1/users/{id}` | administrator | |
| `POST /api/v1/users` | administrator | |
| `PUT /api/v1/users/{id}` | administrator | |
| `DELETE /api/v1/users/{id}` | administrator | |
| `GET /api/v1/users/me` | consumer, merchant, rider | |
| `PATCH /api/v1/users/me` | consumer, merchant, rider | |
| `DELETE /api/v1/users/me` | consumer, merchant, rider | |
| `GET /api/v1/stores` | all four roles | Consumer must send a location |
| `GET /api/v1/stores/{id}` | all four roles | |
| `POST /api/v1/stores` | merchant | Owner is the current user |
| `PUT /api/v1/stores/{id}` | merchant | Caller must own the store |
| `DELETE /api/v1/stores/{id}` | administrator, merchant | Merchant must own it; administrator may delete |
| `GET /api/v1/products` | all four roles | Consumer must send a location |
| `GET /api/v1/products/{id}` | all four roles | |
| `POST /api/v1/products` | merchant | Caller must own the store |
| `PUT /api/v1/products/{id}` | merchant | Caller must own the current store and the target store |
| `DELETE /api/v1/products/{id}` | administrator, merchant | Owner or administrator |
| `GET /api/v1/orders` | administrator | |
| `GET /api/v1/orders/my-orders` | consumer | Filtered to the current user |
| `GET /api/v1/orders/unassigned` | rider | `CONFIRMED` and no rider |
| `GET /api/v1/orders/store/{storeId}` | all four roles | Caller must own that store |
| `GET /api/v1/orders/{id}` | all four roles | Consumer, rider, store owner, or administrator |
| `POST /api/v1/orders` | consumer | |
| `PUT /api/v1/orders/{id}` | all four roles | |
| `PATCH /api/v1/orders/{id}/status` | merchant, rider | Status machine above |
| `PATCH /api/v1/orders/{id}/location` | rider | |
| `DELETE /api/v1/orders/{id}` | administrator | |

### Examples

Login as the seeded consumer. The response is a real token from this run, shortened. `exp` and `iat` in the payload are the same second.

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"juanSky@mail.com","password":"passDeJuan1"}'
```

```json
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0NPTlNVTUVSIl0sInN1YiI6Imp1YW5Ta3lAbWFpbC5jb20iLCJpYXQiOjE3OTUzMjAwNTYsImV4cCI6MTc5NTMyMDA1Nn0.ku3yXuEY1W-…"}
```

Search stores within 5 km of that store, as the consumer:

```bash
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/v1/stores?latitude=-37.9940&longitude=-57.5552&distance=5&name=Pizza&page=0&size=5"
```

```json
{
  "content": [
    {
      "id": 1,
      "name": "Pizza Hut de Pedro",
      "address": "Calle 13",
      "ownerId": 2,
      "latitude": -37.9940443,
      "longitude": -57.5552022
    }
  ],
  "page": 0,
  "size": 5,
  "totalPages": 1,
  "totalElements": 1
}
```

The same call without `latitude`, `longitude`, and `distance` returns HTTP 400:

```json
{
  "message": "Falta parametro Latitude, Longitude y Distance",
  "exception": "StoreSearchMissingLocationException",
  "path": "/api/v1/stores",
  "errors": {}
}
```

Create an order as the consumer. The total in the response is the sum of the product prices on the server. On this run, product `101` was `Napolitana` (inserted after the 100 generated products of store 1) at price 10.50. Confirm the id with a product search on your own boot before reusing it.

```bash
curl -s -X POST http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "customerAddress": "Av Colon 2000",
    "products": [101],
    "customerLatitude": -37.997,
    "customerLongitude": -57.55,
    "paymentType": "EFECTIVO"
  }'
```

The response was HTTP 201, status `PENDING`, `total` 10.5, `storeId` 1, and `latitude` / `longitude` set to the store.

Pedro (the owner of store 1) then moved that order, and Ana claimed it. On a fresh start the new id was 5, after the four seeded orders. Substitute the id from the 201 response if it differs.

```bash
# merchant
curl -s -X PATCH http://localhost:8080/api/v1/orders/5/status \
  -H "Authorization: Bearer $MERCHANT" \
  -H 'Content-Type: application/json' \
  -d '{"status":"CONFIRMED"}'

# rider
curl -s -X PATCH http://localhost:8080/api/v1/orders/5/status \
  -H "Authorization: Bearer $RIDER" \
  -H 'Content-Type: application/json' \
  -d '{"status":"CONFIRMED_ASSIGNED"}'
```

`CONFIRMED` made the order show up in `GET /api/v1/orders/unassigned`. `CONFIRMED_ASSIGNED` set `riderId` to Ana's id (`4`). A merchant `PATCH` to `COMPLETED` from `CONFIRMED_ASSIGNED` returned HTTP 400, `InvalidOrderStatusException`, message `Operación inválida`. `DISPATCHED` from the owning merchant was accepted.

Register a consumer (the password in the body is hashed before it is stored):

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Lucia",
    "lastName": "Perez",
    "email": "lucia@mail.com",
    "password": "clave123",
    "role": "ROLE_CONSUMER"
  }'
```

`name` max 20, `lastName` max 30, `email` max 40, `password` 6–20 characters.

## Screenshots

Swagger UI after `GET /swagger` followed the redirect to `/swagger-ui/index.html`. The document title is Delivery App API 1.0.0.

![Swagger UI for Delivery App API 1.0.0](docs/swagger-ui.png)

Local session: startup, anonymous 403, consumer login, store search, the four seeded orders, and the admin merchant filter.

![Local API session against port 8080](docs/api-session.png)

## Technical highlights

- Feature packages with a controller / service / repository split, and DTOs mapped by hand so entities stay off the wire.
- Authorization in two places: HTTP method rules in `SecurityFilterChain`, and ownership checks in the store, product, and order services.
- A small status machine instead of a free-form status field. The merchant and rider branches are separate methods.
- Query objects (`UserFilter`, `StoreFilter`, `ProductFilter`, `OrderFilter`, `AdminOrderFilter`) composed with `Specification.allOf`, plus a shared `PaginationResult`.
- Server-side order totals and a rejection path for unavailable products.
- Proximity search implemented as a degree bounding box (`distance / 111`) on the store coordinates.
- Externalized JWT settings, BCrypt, and a filter that can return a renewed token on the response.
- One `@RestControllerAdvice` for validation errors, not-found cases, bad status transitions, and authentication failures.
- Repeatable demo data: six fixed users, fixed Mar del Plata–area coordinates on the named stores, and Datafaker rows for the rest. The OpenAPI definition is generated from the controllers (springdoc), with the UI mounted at `/swagger`.

## Repository scope

What is in this repository, checked against the tree and a local run:

- The HTTP API, security configuration, seed data, Maven Wrapper, and this README.
- H2 in memory only. `application.yaml` does not define MySQL, PostgreSQL, or another persistent URL.
- No `src/test` sources, no Dockerfile, and no GitHub Actions workflow.

## Team

History on `main` runs from 29 Apr 2026 (`6c29de9`, initial commit) through 13 Nov 2026 (`2054534`), 135 commits. Three people appear in that history. The names below match the previous README and the git author names.

**Nicolás Iavicoli Dulcet** — repository owner. Junior developer. Computer Engineering student at UNMDP and Programming Technician student at UTN FRMdP, Mar del Plata, Argentina. Git authors: `iaviNicolas`, `IavicoliNicolas`, `Nicolas Iavicoli`, `NyoSwat`. Commits in his name add user administration, `GET /api/v1/orders/my-orders`, unassigned-order listing with rider self-assignment, merchant order-status viewing, and administrator functionality.

**Tomás Amato Lebron** — `TomasAmatoLebron` / `Tomás Amato Lebron`. Store and order modules, the dispatched-status flow, user and admin order filters, and the previous README.

**Lucas José Lomuoio** — `lucas` / `Lucas Lomuoio` / `Bstiasss`. Spring Boot project setup, the move to Java 21, the product module, `GlobalExceptionHandler`, `DataSeeder`, and OpenAPI.

## Resumen en español

API REST de un backend de delivery, proyecto final académico de la UTN. Cuatro roles comparten la misma API con permisos distintos: el consumidor busca comercios y productos cerca de un punto y crea pedidos; el comerciante administra sus tiendas y productos y avanza el estado de sus pedidos; el repartidor toma pedidos confirmados sin asignar, actualiza la ubicación y los completa; el administrador gestiona usuarios, comercios, productos y pedidos.

Stack: Java 21, Spring Boot 4.0.6, Spring Security con JWT (jjwt, HS256), Spring Data JPA, H2 en memoria, Validación de Jakarta, springdoc OpenAPI 3.0.2, Lombok, Datafaker y Maven Wrapper 3.9.15. La base se recrea en cada arranque (`ddl-auto: create-drop`) y `DataSeeder` carga usuarios, comercios, productos y cuatro pedidos de ejemplo.

Para ejecutarlo hace falta JDK 21 y estas variables (el secreto es el valor de desarrollo ya comentado en `application.yaml`):

```bash
export JWT_SECRET="ID32WQ74uayoGI3nW7o9MEZKoaNzDongyYltCai2aY8kdWIqlrn4dmnbA3EFngsi"
export JWT_EXPIRATION=86400000
export JWT_REFRESH_WINDOW=604800000
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080`. Swagger: `http://localhost:8080/swagger`. Consola H2: `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:deliveryappdb`, usuario `admin`, contraseña vacía). Una cuenta de prueba es `juanSky@mail.com` / `passDeJuan1` (consumidor). El resto de las cuentas está en la tabla de la sección *Seeded accounts*.

Los estados de un pedido avanzan así: `PENDING` → `CONFIRMED` o `CANCELLED` (comerciante dueño) → `CONFIRMED_ASSIGNED` (repartidor) → `DISPATCHED` (comerciante) → `COMPLETED` (repartidor). Los mensajes de validación de la API están en español.
