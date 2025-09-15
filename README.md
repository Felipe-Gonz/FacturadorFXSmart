# Facturador FX Smart --- API

API REST para crear y listar **facturas** en una moneda base y
**convertir** sus montos a otras divisas usando **tasas diarias** (con
cache en BD por ahora).

## 🚀 Stack

-   **Java 17**
-   **Spring Boot 3.5.5** (Web, Data JPA, Validation, Actuator)
-   **PostgreSQL 17**
-   **Flyway** (migraciones)
-   **springdoc-openapi 2.7.x** (Swagger UI)
-   (A futuro) **Redis** + Spring Cache, `@Scheduled`, Resilience4j

------------------------------------------------------------------------

## 📦 Requisitos

-   Java 17
-   Maven 3.9+
-   PostgreSQL 14+ (probado con 17)
-   (Opcional) Docker para levantar Postgres

------------------------------------------------------------------------

## ⚙️ Configuración

`src/main/resources/application.properties`

``` properties
spring.application.name=FacturadorFXSmart

spring.datasource.url=jdbc:postgresql://localhost:5432/fxsmart
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.flyway.enabled=true

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

> Si baselinaste una BD creada a mano:\
> `spring.flyway.baseline-on-migrate=true` y (opcional)
> `spring.flyway.baseline-version=1`.

------------------------------------------------------------------------

## 🗃️ Migraciones (Flyway)

Carpeta: `src/main/resources/db/migration/`

-   `V1__init.sql` → crea tablas `invoice` y `fx_rate`.
-   `V2__fix_char_to_varchar.sql` → cambia columnas `CHAR(3)` a
    `VARCHAR(3)`.

> Importante: el nombre debe ser \*\*V{n}\_\_descripcion.sql\*\* (doble
> guion bajo).

------------------------------------------------------------------------

## 🧱 Modelo de datos

### `invoice`

-   `id` (UUID, PK)
-   `date` (DATE)
-   `customer` (VARCHAR 120)
-   `base_currency` (VARCHAR 3)
-   `base_amount` (DECIMAL 19,6)
-   `tax_rate` (DECIMAL 5,4)
-   `created_at` (TIMESTAMP)

### `fx_rate`

-   PK compuesta: (`rate_date`, `base`, `target`)
-   `rate` (DECIMAL 19,8)
-   `source` (VARCHAR 40)
-   `retrieved_at` (TIMESTAMP)

> Por ahora las tasas se guardan con fuente `"demo"` si no existen;
> luego se conectará proveedor real (Frankfurter / ExchangeRate.host).

------------------------------------------------------------------------

## 🏗️ Estructura de paquetes

    com.example.FacturadorFXSmart
     ├─ config/            # OpenAPI config
     ├─ controller/        # FxController, InvoiceController
     ├─ dto/               # CreateInvoiceRequest, InvoiceResponse, ConversionResponse
     ├─ entity/            # Invoice, FxRate (+ FxRateId)
     ├─ repository/        # InvoiceRepository, FxRateRepository
     └─ service/           # InvoiceService, FxService

------------------------------------------------------------------------

## ▶️ Cómo ejecutar

### Opción A: con Postgres local

1.  Crea la DB `fxsmart` en tu Postgres local.

2.  Arranca la app:

    ``` bash
    mvn clean spring-boot:run
    ```

3.  Revisa que Flyway migre:

    -   `flyway_schema_history` con V1 (y V2 si aplica)
    -   Tablas `invoice` y `fx_rate` presentes

### Opción B: Docker (Postgres)

`docker-compose.yml` (ejemplo mínimo):

``` yaml
version: "3.8"
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: fxsmart
    ports: ["5432:5432"]
    volumes: ["pgdata:/var/lib/postgresql/data"]
volumes:
  pgdata:
```

Levanta:

``` bash
docker compose up -d
mvn clean spring-boot:run
```

------------------------------------------------------------------------

## 📚 Swagger / OpenAPI

-   UI: `http://localhost:8080/swagger-ui.html`
-   JSON: `http://localhost:8080/v3/api-docs`
-   YAML: `http://localhost:8080/v3/api-docs.yaml`

------------------------------------------------------------------------

## 🔌 Endpoints (v1)

### 1) Crear factura

`POST /v1/invoices`

**Body**

``` json
{
  "date": "2025-09-05",
  "customer": "ACME",
  "baseCurrency": "USD",
  "baseAmount": 1250.00,
  "taxRate": 0.13
}
```

**201 Created**

``` json
{
  "id": "2d6d5f1b-4a2a-4c82-9d97-d8b2b7f4b9d2",
  "date": "2025-09-05",
  "customer": "ACME",
  "baseCurrency": "USD",
  "baseAmount": 1250.00,
  "taxRate": 0.13
}
```

------------------------------------------------------------------------

### 2) Listar facturas (paginado y rango opcional)

`GET /v1/invoices?from=YYYY-MM-DD&to=YYYY-MM-DD&page=0&size=10&sort=date,desc`

**200 OK**

``` json
{
  "content": [ { "...": "..." } ],
  "pageable": { "...": "..." },
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

------------------------------------------------------------------------

### 3) Convertir una factura a otra moneda

`GET /v1/invoices/{id}/convert?target=EUR`

**200 OK (demo rates 1:1 por ahora)**

``` json
{
  "invoiceId": "2d6d5f1b-4a2a-4c82-9d97-d8b2b7f4b9d2",
  "targetCurrency": "EUR",
  "rate": 1.00,
  "rateDate": "2025-09-15",
  "subtotalBase": 1250.00,
  "taxBase": 162.50,
  "totalBase": 1412.50,
  "subtotalTarget": 1250.00,
  "taxTarget": 162.50,
  "totalTarget": 1412.50
}
```

------------------------------------------------------------------------

### 4) Últimas tasas de cambio (cache en BD por día)

`GET /v1/fx/latest?base=USD&targets=EUR,CRC,MXN`

**200 OK (demo)**

``` json
{ "EUR": 1.0, "CRC": 1.0, "MXN": 1.0 }
```

**Errores comunes** - Sin `base` o `targets` → `400 Bad Request`
(validados) - `targets` vacío → `400` con mensaje explicativo

------------------------------------------------------------------------

## 🧪 cURL de prueba

``` bash
# Crear factura
curl -X POST http://localhost:8080/v1/invoices   -H "Content-Type: application/json"   -d '{"date":"2025-09-05","customer":"ACME","baseCurrency":"USD","baseAmount":1250.00,"taxRate":0.13}'

# Listar (último mes por defecto si no pasas from/to)
curl "http://localhost:8080/v1/invoices?page=0&size=10"

# Convertir a EUR
curl "http://localhost:8080/v1/invoices/{UUID}/convert?target=EUR"

# Tasas del día
curl "http://localhost:8080/v1/fx/latest?base=USD&targets=EUR,CRC"
```

------------------------------------------------------------------------

## 🧰 Troubleshooting (lecciones aprendidas)

-   **Flyway no aplicaba migraciones**\
    Carpeta correcta: `src/main/resources/db/migration/` y archivo
    `V1__init.sql` (doble `__`).\
    Usa `mvn clean` si el recurso no se copiaba al `target/`.

-   **`Schema-validation: missing table [fx_rate]`**\
    No se aplicó `V1`. Verifica rutas/nombres/logs de Flyway.

-   **`wrong column type… CHAR vs VARCHAR`**\
    Postgres tenía `CHAR(3)` → JPA esperaba `VARCHAR(3)`.\
    Solución: migración `V2__fix_char_to_varchar.sql` **o**
    `columnDefinition="char(3)"` en la entidad.

-   **Swagger `NoSuchMethodError ControllerAdviceBean`**\
    Incompatibilidad de versiones. Usa
    `springdoc-openapi-starter-webmvc-ui:2.7.x` con Spring Boot 3.5.x.

------------------------------------------------------------------------

## 🗺️ Roadmap (próximos pasos)

1.  **Proveedor FX real** con `WebClient` (Frankfurter → fallback
    ExchangeRate.host), persistir en `fx_rate`.\
2.  **Cache** con Redis + `@Cacheable` y TTL por día.\
3.  **Scheduler** `@Scheduled` para precargar tasas cada mañana.\
4.  **Resilience4j** (`@Retry`, `@CircuitBreaker`) para robustecer
    llamadas HTTP.\
5.  **Filtros extra** en `/v1/invoices` (por `customer`), y
    **orden/sort**.\
6.  (Opcional) **Auth** (API Key/JWT) y documentarla en Swagger.\
7.  **Frontend React**: tabla de facturas + modal con conversión en vivo
    a 2--3 monedas.
