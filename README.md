# 🎬 Cinemaland – Discover and Manage Movies & Series

Cinemaland is a full-stack web application for browsing, adding, and managing movies and TV series. Built with Java (Spring Boot) on the backend and Vue 3 on the frontend, this project demonstrates modern full-stack architecture and responsive design.

---

## 🚀 Tech Stack

**Frontend (Vue.js):**
- Vue 3
- Vue Router
- Axios
- Vue CLI
- Babel & ESLint

**Backend (Spring Boot):**
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- REST API + Thymeleaf admin pages
- Flyway (database migrations)

---

## 🎯 Features

- 🔍 Browse movies and TV series
- ➕ Add new movie/series entries
- 🗑️ Edit or delete entries
- ⚙️ RESTful communication between frontend and backend
- 💡 Responsive, clean UI built with Vue CLI and Axios

---

## 🛠️ Project Structure

```
cinemaland-fullstack/
├── backend/    Spring Boot (port 3000)
│   ├── src/main/java/at/Cinemaland/
│   │   ├── Controller/api/   REST API used by the Vue frontend (/api/...)
│   │   ├── Controller/web/   Thymeleaf pages (/, /movie/{id}, /administration, ...)
│   │   ├── Model/            JPA entities (Movie, Actor, MovieCategory, FileData)
│   │   ├── Repository/       database access
│   │   └── Service/          business logic + image upload
│   └── src/main/resources/
│       ├── db/migration/     Flyway SQL scripts
│       └── templates/        Thymeleaf HTML pages
└── frontend/   Vue 3 (port 8400)
```

---

## ⚙️ Backend Setup

1. Create the database:
   ```sql
   CREATE DATABASE cinemaland;
   ```
2. Set your database password as an environment variable (it is **not** stored in the code):
   ```bash
   # Linux / macOS
   export DB_PASSWORD=your_mysql_password
   # Windows PowerShell
   $env:DB_PASSWORD="your_mysql_password"
   ```
3. On a **new, empty** database, also set `FLYWAY_ENABLED=true` – Flyway then creates all tables for you.
4. Start:
   ```bash
   cd backend
   ./gradlew bootRun
   ```

| Variable | Default | Meaning |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/cinemaland` | database URL |
| `DB_USERNAME` | `root` | database user |
| `DB_PASSWORD` | *(empty)* | database password |
| `FLYWAY_ENABLED` | `false` | create tables automatically (new database) |
| `UPLOAD_DIR` | `./uploads/` | folder for uploaded images |
| `CORS_ORIGINS` | `http://localhost:8400,http://localhost:8080` | allowed frontend URLs |

Admin area (Thymeleaf): http://localhost:3000/administration

---

## ⚙️ Frontend Setup

```bash
cd frontend
npm install
npm run serve   # http://localhost:8400
npm run lint
```

The backend URL can be changed in a `.env` file (see `.env.example`).
