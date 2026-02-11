
# Personal Finance Tracker (Backend) — Spring Boot + MySQL + Flyway

---

## Tech Stack

- **Java 21**
- **Spring Boot** (Web + Data JPA)
- **Maven**
- **MySQL**
- **Flyway** (database migrations)
- **JUnit** (baseline tests)

---

## Project Goals (MVP)

- Users can manage:
    - Accounts (e.g., Cash, Bank)
    - Categories (Income/Expense)
    - Transactions (Income/Expense records)
- Clean architecture progression: **Entity → Repository → Service → Controller**
- Reproducible setup: clone, configure DB, run migrations, run app.

---

## Phase 0 — Repo Setup (Portfolio Summary)

In Phase 0, I set up the repository and development environment for my Personal Finance Tracker backend.
I created a GitHub repo, cloned it locally, and initialized a Spring Boot + Maven project so it can build and run immediately.
I added basic configuration (`application.yml`) and a simple controller endpoint to confirm the app starts and responds correctly.
I made sure baseline testing works (the project builds and tests run), and I verified dependencies resolve correctly—fixing a transitive
dependency security warning by overriding AssertJ to version **3.27.7**.
At the end of Phase 0, the project is reproducible: someone can clone the repo, run the tests, and start the application without extra setup.

### Quick one-liner definitions (junior-friendly)

- **Spring Boot:** framework to quickly build Java backends (REST APIs) with autoconfiguration.
- **Maven:** build + dependency tool (`pom.xml`) that compiles, tests, and downloads libraries.
- **Git:** tracks your code changes locally (commits, branches).
- **GitHub:** hosts your Git repo online and can run CI (GitHub Actions).
- **IntelliJ:** IDE to code, run, test, and manage Maven/Git easily.

---
## Phase 1 — Database Setup + Flyway (Infrastructure)

In Phase 1, I connected the Spring Boot app to a local **MySQL** database and introduced **Flyway** migrations so the schema is versioned and reproducible.
Instead of letting Hibernate auto-create tables, the database structure is created and updated using migration scripts inside the repository.
This makes the project easier to set up for others and closer to real-world backend workflows.

### Key results
- MySQL connection configured in `application.yml`
- Flyway enabled and pointing to `classpath:db/migration`
- Hibernate DDL set to `validate` so it checks schema instead of generating it
- Schema changes tracked in `flyway_schema_history`

---
## Phase 2 — Domain Model + Persistence

### Domain Model (Entities)
- **User**
- **Account**
- **Category**
- **Transaction**

### Key Decisions
- **IDs:** UUID in Java (`java.util.UUID`)
- **Storage in MySQL:** `CHAR(36)` for IDs (readable UUID strings)
- **Database migrations:** Flyway (`src/main/resources/db/migration`)
- **Hibernate DDL:** validation only (Flyway owns schema)

### Tables
- `users`
- `accounts`
- `categories`
- `transactions`
- `flyway_schema_history` (Flyway metadata)

---
## Phase 3 — Authentication (JWT)

In Phase 3, I implemented user authentication so the API can securely identify who is calling it. I added an AuthController with register and login endpoints. 
After a successful login, the backend returns a JWT access token. This token must be sent with every protected request using the Authorization: Bearer <token> header.
Instead of using server sessions, the application is stateless: the JWT contains the user identifier (stored as the token subject), and a security filter validates the token on each request and sets the authenticated principal. 
If the token is missing or invalid, the API responds with 401 Unauthorized.

### Key results
- **Register + login endpoints: /api/auth/register, /api/auth/login**
- **JWT generation and validation**
- **Stateless security (no server-side sessions)**
- **Protected endpoints require a valid Bearer token**

### Phase 4 — CRUD APIs (Accounts, Categories, Transactions + Filtering)

In Phase 4, I built the main MVP REST APIs for the Personal Finance Tracker: Accounts, Categories, and Transactions. 
Each module follows the same clean flow: Entity → Repository → Service → Controller, with DTOs used for request/response and validation added on inputs.
All endpoints are protected by JWT, and access rules ensure users can only work with their own data.

### Accounts endpoints (CRUD)
- **POST /api/accounts**
- **GET /api/accounts**
- **GET /api/accounts/{id}**
- **PUT /api/accounts/{id}**
- **DELETE /api/accounts/{id}**

### Rules implemented
- **Validate inputs (e.g., name not blank, currency format, openingBalance >= 0)**
- **Only access your own accounts (ownership enforced via authenticated userId)**

### Categories endpoints (CRUD)
Categories represent labels for transactions and also define whether money is INCOME or EXPENSE using CategoryType.

- **POST /api/categories**
- **GET /api/categories**
- **GET /api/categories/{id}**
- **PUT /api/categories/{id}**
- **DELETE /api/categories/{id}**

### Rules implemented
- **Validate inputs (name not blank, type required)**
- **type must be INCOME or EXPENSE**
- **Only access your own categories**

### Transactions endpoints (CRUD + filtering)
Transactions represent money events linked to an Account (“where the money changed”) and optionally a Category (“why it changed”). 
Transactions support CRUD operations and a list endpoint with filtering, pagination, and sorting.

- **POST /api/transactions**
- **GET /api/transactions (filtering + pagination + sorting)**
- **GET /api/transactions/{id}**
- **PUT /api/transactions/{id}**
- **DELETE /api/transactions/{id}**
- 
### List endpoint supports 
- **date range: from / to**
- **filter by accountId**
- **filter by categoryId**
- **filter by type (INCOME / EXPENSE, derived from category)**
- **pagination: page, size**
- **sorting: sort (example: sort=occurredAt,desc)**

### Rules implemented
- **Validate inputs (amount > 0, occurredAt required)**
- **Only access your own transactions (ownership enforced through the account owner)**
- **Category is optional (nullable)**

### Commits (per module)
- **feat: accounts crud**
- **feat: categories crud**
- **feat: transactions crud + filtering**