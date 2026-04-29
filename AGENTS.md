## Commands

### Spring Boot API
- `./gradlew bootRun` — start API at http://localhost:8080
- `./gradlew test` — run all tests (Testcontainers spins up Postgres automatically)
- `cd localdeployment && docker compose up -d` — start local Postgres for manual testing

### React UI (`ui-react/`)
- `npm run dev` — dev server at http://localhost:5173 (proxies `/api` → `localhost:8080`)
- `npm run build` — type-check (`tsc -b`) then Vite build; run this to verify no TS errors
- `npm run lint` — ESLint

## Architecture

### API (Spring Boot)
- Base API path: `/api/checklist-items`
- Single controller: `ChecklistItemController`
- DTOs are Java `record` types in `dev.checklist.dto`
- MapStruct handles DTO/entity mapping — configured via `-Amapstruct.defaultComponentModel=spring` compiler arg in `build.gradle`; never wire mappers manually
- Flyway owns the schema — `ddl-auto: validate` in `application.yml`, JPA never creates tables
- Migrations live in `src/main/resources/db/migration/` — follow `V{n}__{description}.sql` naming
- No authentication on any endpoint

### UI (React)
- Lives in `ui-react/` — standalone Vite project, not served by Spring Boot
- Tailwind CSS v4 loaded via `@tailwindcss/vite` plugin — no `tailwind.config.*` file needed
- `src/api/checklistApi.ts` — all `fetch` calls go here; returns typed `ChecklistItem` from `src/types/`
- Vite proxy in `vite.config.ts` handles `/api` → `localhost:8080`; no CORS config needed in dev

## Testing

- Integration tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` — port is random, not 8080
- REST Assured is used (not MockMvc)
- Testcontainers manages the database — no manual DB setup needed for `./gradlew test`
- Integration test base class: `IntegrationTestBase`
- HTTP test file for manual API testing: `src/test/http/checklist-items.http` (IntelliJ HTTP Client)
