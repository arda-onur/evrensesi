# 🌟 EvrenSesi

**EvrenSesi** is a full-stack web application where users can register, log in, and place a star anywhere on a shared cosmic canvas. Each star carries a personal message click any star to read what someone left behind. Search stars by keyword using Elasticsearch.

🌐 **Live:** [evrensesi.com](https://evrensesi.com)

---

## Tech Stack

**Backend**
- Java 21 + Spring Boot 4.0.3
- Spring Security (session-based auth)
- Spring Data JPA + PostgreSQL
- Spring Data Elasticsearch 9.x
- Spring Data Redis 7
- Flyway (database migrations)
- Actuator + Prometheus + Grafana
- Swagger / OpenAPI (springdoc 3.x)

**Frontend**
- React (Vite) — embedded and served by the backend

**Infrastructure**
- Docker + Docker Compose
- Elasticsearch 9.2.5
- Redis 7
- PostgreSQL
- Prometheus + Grafana

---

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) installed and running
- [Docker Compose](https://docs.docker.com/compose/) v2+
- Git

### 1. Clone the repository

```bash
git clone https://github.com/kullanici-adi/evrensesi.git
cd evrensesi
```

### 2. Create the `.env` file

Create a `.env` file in the project root. Use the template below:

```env
# =========================
# POSTGRES
# =========================
POSTGRES_DB=evrensesi
POSTGRES_USER=evrensesi
POSTGRES_PASSWORD=your_password

# =========================
# DATABASE
# =========================
DB_URL=jdbc:postgresql://postgres:5432/evrensesi
DB_USERNAME=evrensesi
DB_PASSWORD=your_password

# =========================
# REDIS
# =========================
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=your_password

# =========================
# ELASTICSEARCH
# =========================
ELASTICSEARCH_URI=http://elasticsearch:9200

# =========================
# CORS
# =========================
APP_CORS_ALLOWED_ORIGINS=http://localhost:8080

# =========================
# SESSION
# =========================
SERVER_SERVLET_SESSION_TIMEOUT=30m
```

### 3. Start the application

```bash
docker compose -f docker-compose.dev.yml up -d --build
```

This will start the following services:

| Service | URL |
|---|---|
| **App (Frontend + Backend)** | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Elasticsearch | http://localhost:9200 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

> ⏳ The application may take ~90 seconds to become available while Elasticsearch and Redis start up.

> 💡 The frontend is embedded in the backend — no separate frontend server needed. Everything runs on `http://localhost:8080`.

---

## Stopping the application

```bash
docker compose -f docker-compose.dev.yml down
```

To also remove all volumes (database, Elasticsearch index, Grafana data):

```bash
docker compose -f docker-compose.dev.yml down -v
```

> ⚠️ Using `-v` will delete all data. Only use this for a clean reset.

---

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Notes

- On first run, Elasticsearch may take longer to become healthy depending on your machine's resources.
- If you previously ran an older version of Elasticsearch, remove the volume before upgrading:
  ```bash
  docker compose -f docker-compose.dev.yml down -v
  ```
