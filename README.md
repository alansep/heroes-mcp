# heroes-mcp

Superhero CRUD built with Java + Spring Boot using **hexagonal architecture (ports & adapters)**.

## Stack

- Java 25
- Spring Boot 4
- Gradle
- springdoc-openapi 3.2.0 (Swagger UI / OpenAPI 3)

## Architecture

```
src/main/java/com/alansep/heroes_mcp
├── domain                     # core: business rules, framework-free
│   ├── model                 # Hero, Universe
│   └── exception             # HeroNotFoundException, InvalidHeroException
├── application
│   ├── port/in               # HeroUseCase, CreateHeroCommand, UpdateHeroCommand
│   ├── port/out              # HeroRepositoryPort
│   └── service               # HeroService (implements HeroUseCase)
├── adapter
│   ├── in/web                # HeroController, DTOs, mapper, ApiExceptionHandler
│   └── out/persistence       # InMemoryHeroRepository (implements HeroRepositoryPort)
└── config                    # BeanConfiguration (wires the service into Spring)
```

The domain and application layers know nothing about Spring, HTTP, or persistence. Dependencies
always point inward: `adapter -> application -> domain`. Swapping the database or exposing the
CRUD over MCP only requires a new adapter implementing `HeroRepositoryPort` or `HeroUseCase`.

## Model

`Hero`: `id` (UUID), `name`, `powers` (list of strings), `age`, `gender`, `universe`.
`Universe`: `DC`, `MARVEL`, `THE_BOYS`.

## Endpoints

| Method | Path                | Description                  | Status      |
|--------|---------------------|------------------------------|-------------|
| POST   | `/api/heroes`       | Create a hero                | 201 Created |
| GET    | `/api/heroes`       | List heroes (sorted by name) | 200 OK      |
| GET    | `/api/heroes/{id}`  | Find a hero by id            | 200 / 404   |
| PUT    | `/api/heroes/{id}`  | Update a hero                | 200 / 404   |
| DELETE | `/api/heroes/{id}`  | Delete a hero                | 204 / 404   |

Example payload:

```json
{
  "name": "Superman",
  "powers": ["Flight", "Super strength"],
  "age": 35,
  "gender": "male",
  "universe": "DC"
}
```

Errors are returned as `ProblemDetail` (`application/problem+json`): 400 for invalid payloads or
parameters and 404 for a nonexistent hero.

## API documentation (Swagger / OpenAPI)

With the application running at `http://localhost:8080`:

- **Swagger UI:** http://localhost:8080/swagger-ui.html (redirects to `/swagger-ui/index.html`)
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **OpenAPI YAML:** http://localhost:8080/v3/api-docs.yaml

The documentation is generated automatically from the controller and DTOs and can be used
directly to test the endpoints through the **Try it out** button.

## Running

```bash
./gradlew bootRun
```

## Tests

```bash
./gradlew test
```

The suite covers the domain, the application service, the in-memory adapter, and the REST
controller (including error scenarios), as well as OpenAPI document generation and Swagger UI
availability.
