# StylePin

StylePin is a large, long-term Pinterest-inspired fashion discovery platform built with React + Vite, Java + Spring Boot, MySQL, and REST APIs. The current implementation supports outfit discovery, outfit details, and Shop the Look products.

## Current features

- Responsive Pinterest-inspired masonry discovery feed with multiple pin styles.
- Client-side search across outfit titles, categories, and tags, plus category filtering.
- Light/dark theme with a locally persisted preference.
- Outfit Detail route at `/outfits/:id`, with outfit information and Shop the Look product cards.
- Product prices formatted in INR using the Indian locale, with no fractional digits.
- Feed integration with `GET /api/outfits` and detail integration with `GET /api/outfits/{id}`.

The feed uses `frontend/src/dev/demoFashionFeed.js` when the API list request fails or returns no outfits, and displays a demo-data banner. This fallback is not restricted to development builds. Outfit Detail requires the API; demo feed entries are not guaranteed to match database records. Seeded product links are placeholders, not a checkout integration.

## Architecture and project structure

| Area | Implementation |
| --- | --- |
| `frontend/` | React 19, Vite 8, JavaScript, React Router, and Oxlint |
| `backend/` | Java 21, Spring Boot 3.5.6, Maven Wrapper, JPA/Hibernate, and MySQL |
| `docs/` | Notes and documentation area |

The frontend separates pages, reusable components, API requests, hooks, and utilities. React hooks manage state. Routes are `/` (discovery feed) and `/outfits/:id` (Outfit Detail). Search and category filtering run in the browser on loaded outfits.

The backend follows Controller → Service → Repository, returning DTOs rather than persistence entities. Packages are organized as `controller/entity/dto/repository/service/config/exception`.

The persistence model contains `Category`, `Outfit`, and `Product`. Outfits belong to categories and contain products; products belong to an outfit and may also reference a category. JPA/Hibernate persists these entities in MySQL.

## REST API

| Endpoint | Response |
| --- | --- |
| `GET /api/outfits` | List of outfit DTOs with category names, tags, and nested products |
| `GET /api/outfits/{id}` | One outfit DTO with products, or HTTP 404 when missing |

The frontend API module is `frontend/src/api/outfitApi.js`. Its base URL is currently hardcoded to `http://localhost:8080/api`.

## Prerequisites

- Java 21 (or a newer JDK that can compile for Java 21).
- Node.js compatible with the installed Vite package: `^20.19.0 || >=22.12.0`, and npm.
- A running MySQL server with a `stylepin` database accessible to the configured `root` user.
- `DB_PASSWORD` set in the backend process's environment.

A separate Maven installation is not required; use the included Maven Wrapper.

## Run the backend

From `backend/`, set the password for your local MySQL account and start Spring Boot.

PowerShell:

```powershell
$env:DB_PASSWORD = 'your-local-mysql-password'
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
export DB_PASSWORD='your-local-mysql-password'
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`. Current configuration connects to `jdbc:mysql://localhost:3306/stylepin` as `root` and uses Hibernate `ddl-auto=update` to update the schema. Database auto-configuration is enabled.

Development seed data is enabled through `stylepin.seed-data.enabled=true`. On startup, `DataInitializer` creates demo categories and outfits and synchronizes seeded products, including those on existing outfits. Images use external Unsplash URLs, prices are in INR, and product URLs are placeholders. Set the seed property to `false` when development seed data is not wanted, including production environments.

## Run the frontend

From `frontend/`:

```bash
npm install
npm run dev
```

Open the URL Vite prints, typically `http://localhost:5173`. Run the backend and MySQL to use persisted outfits and Outfit Detail pages.

## Build and validation

Backend, from `backend/`:

```powershell
.\mvnw.cmd package
```

On macOS/Linux, use `./mvnw package`. Tests run by default. Spring integration tests activate the `test` profile, which uses an in-memory H2 database with `create-drop` schema handling instead of MySQL. Tests cover application startup, outfit API responses (including missing outfits), and entity relationships.

Frontend, from `frontend/`:

```bash
npm run build
npm run lint
```

Linting uses Oxlint. There is currently no frontend automated test script configured.

## Future roadmap

These capabilities are planned, not completed end-to-end features:

- Authentication
- Saved outfits
- Boards
- Likes
- Style DNA
- Recommendations
- AI RE:STYLE

## Working on StylePin

Read `AGENTS.md` before making changes. This repository is shared with tools including Codex, Cursor, OpenCode, and Antigravity. Preserve existing work, keep changes scoped to the request, and explicitly call out API contract changes. Roadmap items should be implemented only when requested.
