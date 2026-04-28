## Commands

- `./gradlew bootRun` — start app (http://localhost:8080)
- `./gradlew test` — run all tests (Testcontainers spins up Postgres automatically)
- `cd localdeployment && docker compose up -d` — start local Postgres for manual testing

## Architecture

- Base API path: `/api/checklist-items`
- Single controller: `ChecklistItemController`
- DTOs are Java `record` types in `dev.checklist.dto`
- MapStruct handles DTO/entity mapping (configured via `-Amapstruct.defaultComponentModel=spring` compiler arg)
- Flyway owns schema (`ddl-auto: validate` in application.yml — JPA never auto-creates tables)
- No authentication on any endpoint

## Testing

- Integration tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` — port is random, not 8080
- REST Assured is used (not MockMvc)
- Testcontainers manages the database — no manual DB setup needed for `./gradlew test`
- Integration test base: `IntegrationTestBase`
