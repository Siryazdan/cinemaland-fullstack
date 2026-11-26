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

### 🔐 Accounts & personal collections

- Sign up at http://localhost:3000/signup (or in the Vue app) and you land in your own **My Collection** admin area.
- Every user has a **private** collection: movies, categories and actors belong to the user who created them.
  Nobody can see, edit or delete another user's entries (the API answers `404`, the admin pages redirect back).
- Passwords are stored only as BCrypt hashes. All pages and `/api/**` require a login; images (`/files/{id}`) are public.
- **Existing database:** the migration `V20261125200000__create_app_user_and_ownership.sql` adds the `app_user` table
  and a `user_id` column. Entries created before it have no owner and are hidden until you assign them to an account:
  ```sql
  SET @me = (SELECT id FROM app_user WHERE email = 'you@example.com');
  UPDATE movie SET user_id = @me WHERE user_id IS NULL;
  UPDATE movie_category SET user_id = @me WHERE user_id IS NULL;
  UPDATE actor SET user_id = @me WHERE user_id IS NULL;
  ```

---

## ⚙️ Frontend Setup

```bash
cd frontend
npm install
npm run serve   # http://localhost:8400
npm run lint
```

The backend URL can be changed in a `.env` file (see `.env.example`).
