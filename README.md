# StylePin

StylePin is a Pinterest-inspired fashion discovery platform. This repository currently contains the project foundation only: a Spring Boot backend and a React frontend.

## Project structure

- `backend/` — Java 21 + Spring Boot + Maven
- `frontend/` — React + Vite
- `docs/` — notes and documentation (empty for now)

## Prerequisites

- Java 21 (or a newer JDK that can compile for Java 21)
- Node.js 18 or newer (needed for the frontend)

Maven is not required. The backend includes the Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Run the backend

From the `backend` folder:

```bash
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API starts at http://localhost:8080.

MySQL is on the classpath for later, but it is not connected yet. Database auto-configuration is turned off until entities are added.

## Run the frontend

From the `frontend` folder:

```bash
npm install
npm run dev
```

The homepage opens at the URL Vite prints, usually http://localhost:5173.

## Build without starting the apps

Backend:

```bash
cd backend
.\mvnw.cmd -DskipTests=false package
```

Frontend:

```bash
cd frontend
npm run build
```
