# Product management

The `/admin/products` workspace manages the existing `products` table through Spring controllers, services, repositories, and MySQL. It supports creation, recent-first pagination, editing, confirmed deletion, image preview, INR preview, availability, and validation. No product catalog is stored in localStorage. The existing theme preference still uses localStorage.

## Database upgrade

Stop the backend and take your normal database backup. Open the MySQL client with a password prompt (do not put the password in a command or source file):

```powershell
mysql -u root -p stylepin
```

Run this inside the MySQL client:

```sql
SOURCE C:/Users/anila/StylePin/backend/scripts/product-management-mysql.sql;
```

The script targets the existing `stylepin` database. It checks `information_schema` before adding columns or relaxing nullability and uses `CREATE TABLE IF NOT EXISTS`. It can be rerun without resetting records or changing existing roles.

- Adds `users.role VARCHAR(20) NOT NULL DEFAULT 'USER'`. Existing users become USER; no account is promoted.
- Adds nullable `products.retailer` and `products.available` defaulting to true, preserving legacy behavior.
- Makes `products.outfit_id` nullable; existing outfit IDs and foreign keys remain intact.
- Adds `product_tags` with a foreign key to products.
- No existing tables are dropped/recreated and no records are deleted.

Run the script before the updated application on an existing MySQL database. Hibernate's existing `ddl-auto=update` setting remains unchanged, but it is not a substitute for this explicit upgrade. Do not use the H2 test profile against MySQL.

## Make exactly one development account an administrator

1. Apply the migration, start the normal backend with your existing `DB_PASSWORD` and `JWT_SECRET` environment setup, and register your chosen account through StylePin if needed. For local HTTP development use `AUTH_COOKIE_SECURE=false`; retain secure cookies for HTTPS deployments.
2. In the MySQL client, look up the exact account. Replace the email below locally; it is not an application configuration or committed credential:

```sql
SELECT id, username, email, role FROM users
WHERE email = 'replace-with-your-own-account-email';
```

3. Confirm the result is the intended account. Set the ID below to that numeric ID, then execute the transaction. `LIMIT 1` and the primary key restrict the update to one row:

```sql
SET @stylepin_admin_id = 123; -- replace 123 with the verified ID
START TRANSACTION;
SELECT id, username, email, role FROM users
WHERE id = @stylepin_admin_id FOR UPDATE;
UPDATE users SET role = 'ADMIN'
WHERE id = @stylepin_admin_id AND role = 'USER' LIMIT 1;
SELECT ROW_COUNT() AS promoted_accounts;
SELECT id, username, email, role FROM users WHERE id = @stylepin_admin_id;
```

4. If the displayed account is correct and `promoted_accounts` is 1, execute `COMMIT;`. Otherwise execute `ROLLBACK;` and inspect the account again. An already-admin account reports zero changes; it needs no promotion.
5. Log out and sign in again to refresh the role displayed by the frontend, then open `http://localhost:5173/admin/products`.

To revoke that account's admin access:

```sql
UPDATE users SET role = 'USER'
WHERE id = @stylepin_admin_id AND role = 'ADMIN' LIMIT 1;
```

Backend permission checks read the database on each request, so revocation takes effect without waiting for JWT expiry. The frontend may still display its admin link until the session refreshes, but the API will reject access. There is no role-management HTTP endpoint, signup role field, hard-coded admin identity, or automatic first-user promotion.

## API

| Method | Path | Result |
| --- | --- | --- |
| POST | `/api/admin/products` | 201, created product |
| GET | `/api/admin/products?page=0&size=24` | 200, existing `items/page/size/totalElements/totalPages` pagination shape |
| PUT | `/api/admin/products/{id}` | 200, updated product |
| DELETE | `/api/admin/products/{id}` | 204; 409 when attached to an outfit |
| GET | `/api/admin/categories` | 200, sorted category-name array |

Request fields: `name`, `brand`, `price`, `imageUrl`, `productUrl`, optional `retailer`, `category` (name), optional `tags` (string array), and `available` (boolean). Price must be positive INR, up to 99,999,999.99, with at most two decimal places. Tags are trimmed, lowercased, deduplicated, and limited to 30 entries of 50 characters. Categories reuse existing names case-insensitively; a new name creates a Category with a unique generated slug.

Responses also include `id`, `outfitId` (nullable), and `createdAt`. Recent ordering uses creation time and ID descending. A missing product is 404; malformed fields are 400. Both URLs must be HTTP(S) with a host and no embedded credentials. New/updated shopping URLs reject example.com/org/net and their subdomains, localhost, and `.test`/`.invalid` placeholders. Validation does not verify inventory or retailer-page existence. Image requests are made by the browser for display; the server does not fetch, download, or scrape them.

All admin routes use the existing JWT resource-server authentication followed by a database ADMIN check: logged out/invalid token → 401, authenticated USER → 403, ADMIN → allowed. Existing JWT issuer/audience/session validation, refresh rotation/replay handling, cookie settings, and auth-route CSRF protection are preserved. CORS additionally permits PUT. `UserResponseDTO` adds `role` to registration, login, refresh, and current-user responses. Outfit API response shapes are unchanged.

## Existing data and future outfit attachment

Products created here are independent catalog records with nullable `outfit_id`. Existing Outfit → Product relationships and nested public product DTOs remain compatible. Attaching/reusing catalog pieces across outfits is a later feature; there is no attachment UI or API in this milestone. The current relationship still supports at most one outfit per product and retains its existing cascade/orphan-removal semantics. A future attachment workflow must deliberately handle those semantics.

Admin deletion is blocked for attached products. Existing seed products can be edited, but a valid real shopping URL is required when saving; their legacy placeholder URLs are not silently replaced. Seeding only populates products when creating a new seeded outfit, never synchronizing, overwriting, removing, or recreating products on an existing outfit. It also leaves existing outfit images unchanged.

Availability is stored and displayed in the admin catalog. Public outfit product responses retain their existing shape; displaying availability in Shop the Look is a later additive integration. No automatic attachment or matching is implied by creating a product.

## Verification

Run from `backend`: `.\mvnw.cmd package`. Run from `frontend`: `npm run build`, `npm run lint`, and `node scripts/check-experiences.mjs`.

Backend integration coverage includes real JWT login, USER defaults, 401/403 on all admin routes, successful ADMIN access, immediate role revocation, CRUD persistence, validation, CORS PUT, attached-product deletion protection, and repeated seeding preserving edited/manual products. Existing authentication, refresh, collection, outfit, and entity tests remain part of the full suite.

MySQL acceptance: apply the migration twice; verify existing entity counts/IDs and roles; sign in as the explicitly promoted admin; create a real product; reload Recent Products; confirm the row and tags in MySQL; edit and reload; confirm delete and verify the row disappears. Repeat API access with no token and an ordinary USER. Test Explore, Discover, outfit detail/Look Breakdown/Shop the Look, Save, Boards, Login/Register, and both themes. Do not use seed data as proof that a live retailer URL or image is valid.

## Results from this implementation (2026-09-14)

- Backend `mvnw.cmd package`: PASS, 23 tests, 0 failures/errors/skips. Four new admin integration tests exercise multiple security and validation cases.
- Frontend `npm run build`: PASS.
- Frontend `npm run lint`: PASS, no warnings.
- `node scripts/check-experiences.mjs`: PASS (complete 40-look Discover, search/categories, Save/Board controls, real detail links, bounded Explore, navigation, preview link safety).
- Browser against an isolated H2-backed server: PASS for admin login, logged-out redirect, required fields, invalid shopping URL, image preview, INR preview, create/reset/recent listing, edit price and availability, reload persistence, delete cancellation/confirmation, and absence after reload.
- Browser regressions: PASS for Explore navigation/field rendering, Discover catalog, outfit detail, Look Breakdown, Shop the Look/INR, saving an outfit, Saved listing, creating a board and adding the outfit, Boards listing/detail, register/login/logout, ordinary-user admin denial, light/dark switching, and mobile admin layout at 390px. Full animation timing/reduced-motion testing was not repeated because the Explore implementation was untouched; the existing render regression check passed.
- Some pre-existing remote seed images were unavailable; the existing image fallbacks rendered.
- MySQL migration execution, repeating that migration against MySQL, and live MySQL CRUD remain UNVERIFIED: no listener/credentials were available in this session. No existing MySQL data was changed and no real account was promoted.
- Temporary H2 test server, accounts, products, board, and local smoke credentials were disposed of after verification. Browser and temporary frontend server were closed. No commit or push was made.

## Complete source-file inventory

Modified:

- `AGENTS.md`
- `README.md`
- `backend/mvnw.cmd`
- `backend/src/main/java/com/stylepin/config/DataInitializer.java`
- `backend/src/main/java/com/stylepin/config/SecurityConfig.java`
- `backend/src/main/java/com/stylepin/dto/UserResponseDTO.java`
- `backend/src/main/java/com/stylepin/entity/Product.java`
- `backend/src/main/java/com/stylepin/entity/User.java`
- `backend/src/main/java/com/stylepin/repository/CategoryRepository.java`
- `frontend/src/App.jsx`
- `frontend/src/components/layout/AccountMenu.jsx`

Created:

- `backend/scripts/product-management-mysql.sql`
- `backend/src/main/java/com/stylepin/controller/AdminProductController.java`
- `backend/src/main/java/com/stylepin/dto/AdminProductRequestDTO.java`
- `backend/src/main/java/com/stylepin/dto/AdminProductResponseDTO.java`
- `backend/src/main/java/com/stylepin/repository/ProductRepository.java`
- `backend/src/main/java/com/stylepin/service/AdminAuthorizationService.java`
- `backend/src/main/java/com/stylepin/service/AdminProductService.java`
- `backend/src/test/java/com/stylepin/controller/AdminProductControllerTest.java`
- `frontend/src/api/adminProductApi.js`
- `frontend/src/auth/AdminRoute.jsx`
- `frontend/src/pages/AdminProductsPage.jsx`
- `frontend/src/pages/admin-products.css`
- `docs/product-management.md`
