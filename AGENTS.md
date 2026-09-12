# AGENTS.md

Guidance for agents working in StylePin, including Codex, Cursor, OpenCode, and Antigravity.

## Project purpose

StylePin is a large, long-term Pinterest-inspired fashion discovery platform with a React frontend, Java/Spring Boot backend, MySQL persistence, and REST APIs. Current functionality includes outfit discovery and outfit details with Shop the Look products.

## Repository layout

- `backend/` — Java 21, Spring Boot 3.5.6, Maven Wrapper. Packages: `controller/entity/dto/repository/service/config/exception`.
- `frontend/` — React 19 + Vite 8, JavaScript (not TypeScript), React Router, and Oxlint. Source includes `pages`, `components`, `api`, `hooks`, `utils`, `data`, and `dev`.
- `docs/` — notes/documentation area.

## Current frontend

- Responsive Pinterest-inspired masonry discovery feed with multiple pin presentations.
- Client-side search across outfit titles, categories, and tags, plus category filtering on loaded outfits.
- Light/dark theme managed by `useTheme`, with the selection persisted in local storage.
- React Router routes: `/` for discovery and `/outfits/:id` for Outfit Detail.
- Outfit Detail includes outfit information and Shop the Look product cards. `utils/formatPrice.js` formats prices in INR using `en-IN`, with no fractional digits.
- `api/outfitApi.js` integrates `GET /api/outfits` and `GET /api/outfits/{id}` via `fetch`, with the base URL currently hardcoded to `http://localhost:8080/api`.
- The feed falls back to `dev/demoFashionFeed.js` on a failed or empty list response. This fallback is not restricted to development builds. Outfit Detail requires the API and handles loading, 404, and other errors.
- State uses React hooks; no separate state-management library is configured.

## Current backend and database

- Requests follow Controller → Service → Repository. DTOs handle response boundaries; entities handle persistence.
- `OutfitController` exposes `GET /api/outfits` and `GET /api/outfits/{id}`. Outfit responses include category names, tags, and nested products. Missing outfit IDs return HTTP 404.
- `OutfitService` reads through `OutfitRepository`; `CategoryRepository` supports category persistence and seed setup.
- JPA/Hibernate and MySQL auto-configuration are enabled. Entities are `Category`, `Outfit`, and `Product`. Outfits belong to categories and contain products; products also have an optional category reference.
- MySQL is configured at `jdbc:mysql://localhost:3306/stylepin`, with username `root` and password supplied through the backend process's `DB_PASSWORD` environment variable.
- Hibernate schema handling is `spring.jpa.hibernate.ddl-auto=update`.
- `DataInitializer` seeds development categories, outfits, and products on startup when `stylepin.seed-data.enabled=true` (currently enabled). It also synchronizes seeded products on existing outfits. Seed images use Unsplash; product links are placeholders.
- Spring integration tests use the `test` profile with an in-memory H2 database and `create-drop` schema handling. Tests cover application startup, outfit endpoints, and entity relationships.

## Commands and validation

- Backend commands run from `backend/`; use `.\mvnw.cmd`, not a local Maven installation.
- Run API: `.\mvnw.cmd spring-boot:run`, served at `http://localhost:8080`; requires MySQL and `DB_PASSWORD`.
- Backend build with tests: `.\mvnw.cmd package`.
- Single test: `.\mvnw.cmd -Dtest=StylePinApplicationTests test`.
- Frontend commands run from `frontend/`: `npm install`, then `npm run dev` (typically `http://localhost:5173`).
- Frontend build/lint: `npm run build` and `npm run lint` (Oxlint).
- Current lint rules include `react/rules-of-hooks` and `react/only-export-components`.

## Future roadmap

These are planned capabilities, not completed end-to-end functionality:

- Authentication
- Saved outfits
- Boards
- Likes
- Style DNA
- Recommendations
- AI RE:STYLE

## Large-project structure intent

- Preserve the backend package layout and Controller → Service → Repository boundaries. Add domains within the established packages.
- Extend the existing frontend routing, API layer, components, and hooks as requested features grow. Keep `App.jsx` focused on application composition.

## Working rules

- **Do not add features without being asked.** The roadmap is not authorization to implement features.
- **Do not change API contracts silently.** Call out endpoint or request/response shape changes because the frontend depends on these contracts.
- **Respect shared work.** Inspect Git status before editing, preserve changes made by the user or other tools, and stay within the requested scope.
- **Run builds/tests after changes.** Backend: `.\mvnw.cmd package` (runs tests). Frontend: `npm run build` and `npm run lint`.
- Keep this file and `README.md` aligned with implementation changes.

## Look Breakdown experience

The /outfits/:id page uses components in frontend/src/components/look for a centered, uncropped outfit hero and garment-aware product modules. Desktop and tablet use side columns; mobile stacks the image, metadata, and pieces. Shop the Look repeats all API products with INR prices. Related outfits use real API data ranked by category, shared tags, then aesthetic terms; the current outfit is excluded. Matching alternative pieces appear only with non-placeholder HTTP(S) store URLs. Seed example.com links are shown as unavailable. Light/dark themes, Save and Board actions, reduced motion, loading, and error states are preserved. No API contracts or discovery feed layout changed.
