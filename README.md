# 🎬 Cinemaland

A full-stack web app for browsing and managing movies, actors and categories. It has a public website, an admin area, and a REST API used by a separate Vue frontend.

Built with **Spring Boot** (backend) and **Vue 3** (frontend).

## Features

- Browse movies by category, see top-rated movies, and search
- Movie and actor detail pages
- Admin area to create, edit and delete movies, actors and categories
- Image upload for movie posters
- Website available in English, German and Persian
- REST API for the Vue frontend

## Tech Stack

| Part | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Data JPA, Thymeleaf, Flyway |
| Frontend | Vue 3, Vue Router, Axios, Vue CLI |
| Database | MySQL |

## Project Structure

```
cinemaland-fullstack/
├── backend/    Spring Boot: website, admin area and REST API (port 3000)
└── frontend/   Vue 3 app (port 8400)
```

## Getting Started

**You need:** Java 21, Node.js and MySQL.

**1. Create the database**

```sql
CREATE DATABASE cinemaland;
```

**2. Start the backend**

```bash
cd backend
export DB_PASSWORD=your_mysql_password   # Windows: $env:DB_PASSWORD="..."
export FLYWAY_ENABLED=true               # first start only: creates the tables
./gradlew bootRun
```

- Website: http://localhost:3000
- Admin area: http://localhost:3000/administration

**3. Start the frontend**

```bash
cd frontend
npm install
npm run serve
```

Then open http://localhost:8400

### Settings (environment variables)

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/cinemaland` | Database URL |
| `DB_USERNAME` | `root` | Database user |
| `DB_PASSWORD` | *(empty)* | Database password |
| `FLYWAY_ENABLED` | `false` | Create tables automatically on a new database |
| `UPLOAD_DIR` | `./uploads/` | Folder for uploaded images |
| `CORS_ORIGINS` | `http://localhost:8400,http://localhost:8080` | Allowed frontend URLs |

The frontend's backend URL can be changed in a `.env` file (see `frontend/.env.example`).

## API

| Endpoint | Description |
|---|---|
| `/api/movies/` | Movies and their actors (GET, POST, PUT, DELETE) |
| `/api/categories/` | Categories (GET, POST, PUT, DELETE) |
| `/api/actors` | Actors (GET, POST, PUT, DELETE) |
| `/api/files/` | Image upload and download |

## Author

**Armin Jazdanpanahi** · [GitHub](https://github.com/Siryazdan) · arminyazdanpanahi3@gmail.com
