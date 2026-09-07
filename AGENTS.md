# AGENTS.md

Guidance for agents working in the StylePin repository.

## Project purpose

StylePin is a Pinterest-inspired fashion discovery platform (Java/Spring Boot backend + React frontend). Future roadmap includes authentication, outfits, products, boards, likes/saves, search, recommendations, Style DNA, and AI RE:STYLE.

## Repository layout

- `backend/` — Java 21, Spring Boot 3.5.6, Maven (use the wrapper `.\mvnw.cmd`, not a local Maven). Layered package structure: `controller/entity/dto/repository/service/config/exception`.
- `frontend/` — React 19 + Vite 8, JavaScript (not TypeScript), Oxlint for linting.
- `docs/` — empty notes/documentation area.

## Backend (Java + Spring Boot)

- DB is intentionally **off** right now: `StylePinApplication.java` excludes `DataSourceAutoConfiguration` and `HibernateJpaAutoConfiguration`, and `application.properties` has the datasource lines commented out. Don't flip these on until real entities exist.
- MySQL connector, JPA, and validation starters are on the classpath but unused yet.
- Run the API: `.\mvnw.cmd spring-boot:run` (from `backend/`), served at `http://localhost:8080`.
- Build with tests: `.\mvnw.cmd package` (tests run by default).
- Run a single test (adjust path as needed): `.\mvnw.cmd -Dtest=StylePinApplicationTests test`.

## Frontend (React)

- Run dev: `npm run dev` (from `frontend/`), typically `http://localhost:5173`.
- Build: `npm run build`. Lint: `npm run lint` (oxlint).
- Feed currently uses static data in `src/data/outfits.js` (external Unsplash URLs) — no backend calls yet, no router, no state management.
- Current lint rules: `react/rules-of-hooks`, `react/only-export-components`.

## MySQL and REST API architecture

- When DB work begins: uncomment/configure `spring.datasource.*` and `spring.jpa.*` in `application.properties` and remove the auto-config exclusions in `StylePinApplication.java`.
- Backend exposes REST APIs (Spring Boot `@RestController` in the `controller` package); frontend should consume them via an API layer (none exists yet — establish one when adding API-backed views).
- Follow the layered convention: controllers → services → repositories, with DTOs handling request/response boundaries and entities reserved for persistence.

## Large-project structure intent

- Backend package layout (`controller/entity/dto/repository/service/config/exception`) is the target structure — add new domains within these packages, not new top-level packages.
- Frontend is currently a single `App.jsx`; as features (auth, boards, search, AI) grow, organize code (routing, API layer, hooks, feature folders) before code volume grows — the codebase-review recommendation is to add a router and an API/data-fetching layer before auth lands, and to consider TypeScript.

## Working rules

- **Do not add features without being asked.** Feature work (auth, outfits, products, boards, likes/saves, search, recommendations, Style DNA, AI RE:STYLE) is done only on request.
- **Do not change API contracts silently.** If a REST response/request shape or endpoint must change, call it out — the frontend depends on the backend contract.
- **Run builds/tests after changes.** Backend: `.\mvnw.cmd package` (runs tests). Frontend: `npm run build` and `npm run lint`.
