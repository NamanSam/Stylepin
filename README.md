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

## Look Breakdown experience

The /outfits/:id page uses components in frontend/src/components/look for a centered, uncropped outfit hero and garment-aware product modules. Desktop and tablet use side columns; mobile stacks the image, metadata, and pieces. Shop the Look repeats all API products with INR prices. Related outfits use real API data ranked by category, shared tags, then aesthetic terms; the current outfit is excluded. Matching alternative pieces appear only with non-placeholder HTTP(S) store URLs. Seed example.com links are shown as unavailable. Light/dark themes, Save and Board actions, reduced motion, loading, and error states are preserved. No API contracts or discovery feed layout changed.

## Explore and shared visual system

The homepage opens with a bounded spatial fashion campaign, and `/explore` provides the dedicated experience plus an accessible outfit gallery. `components/explore` and `hooks/useExploreMotion.js` use CSS perspective and one requestAnimationFrame loop; no animation or WebGL dependencies were added. The field uses up to 18 image slots and cycles through the available API catalog rather than claiming unlimited new records. Pointer movement and native page scroll add restrained parallax. Motion pauses on hover/focus, when offscreen, when the tab is hidden, and with the Pause control. The animation frame, observers, media listeners, and input listeners are cleaned up on unmount.

Touch/mobile uses six static images with smaller sources; hidden desktop images use an inline transparent source. Reduced motion disables the controller and presents a static composition. Discovery, Saved, Boards, authentication, and Look Breakdown share warm neutral/charcoal materials, serif headlines, restrained controls, image frames, and motion timing through `editorial.css`. All API outfit records remain available in discovery. The campaign curates known seed photography toward outfit/model images. Demo previews are non-navigable, and only real API outfits link to detail pages. An outfit opened from the spatial field offers a return to Explore. The homepage also retains editorial stories, search/category filtering, masonry discovery, style directions, and selected looks.

Authentication, refresh, Saved, Boards, API contracts, and INR formatting are unchanged. Product isolation still depends on supplied image assets; seed example.com shopping links remain unavailable. No recommendation AI or payment functionality is added.

Final polish keeps keyboard focus pauses independent of pointer exit, waits for field visibility before starting motion, guards queued callbacks after unmount, and clears pending reveal effects when reduced motion is enabled. Reduced motion suppresses hover scaling too. Collection content wraps safely and primary navigation/collection actions have 44px targets. Current verification results and outstanding environment-dependent checks are recorded in `docs/explore-verification.md`.

The editorial polish pass replaces the active legacy stock fallback with `data/editorialPreview.js`: six campaign previews, including three local AI-created full-body studies. Known corporate images are excluded from campaign placement, while real API imagery and complete live discovery records remain unchanged. The spatial field has staggered positions, varied scales, restrained rotations, slower depth-dependent drift, soft image entrances, hover lift and subtle neighbor recession. Mobile retains six static, asymmetrically placed images. Shared typography, borders and image frames extend through discovery, Look Breakdown and private collections. Asset prompts and provenance are in `docs/editorial-image-direction.md`.

Explore and Discover are separate experiences. `/explore` retains the animated spatial field and leads to `/#discover` through an editorial transition; it does not repeat the discovery grid. Discover keeps the editorial masonry layout and renders all matching API outfits with lazy-loaded images, without the former eight-look display cap. Categories use larger labels and a stronger selected underline. Offline, six studies are explicitly labeled as a featured campaign preview, with a retry action that replaces them with the complete live response when the API returns. No preview images are assigned fabricated live detail links. Run `node scripts/check-experiences.mjs` from `frontend/` to check the 40-record render fixture, filters, links and distinct layouts.
