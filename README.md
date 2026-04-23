# Checklist Backend

A production-ready REST API for managing checklist items, built with Spring Boot 3.4 and Java 21.

This project was generated entirely by **Claude Sonnet 4.6** via [OpenCode](https://opencode.ai) using 5 sequential prompts — as part of a benchmark comparing AI coding agents and models.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Runtime | Java 21 |
| Framework | Spring Boot 3.4 |
| Build | Gradle (Groovy DSL) |
| Database | PostgreSQL 17 |
| Migrations | Flyway |
| ORM | Spring Data JPA |
| Mapping | MapStruct 1.6 |
| Validation | Jakarta Validation |
| Testing | JUnit 5, Mockito, Testcontainers, REST Assured |

---

## Getting Started

### Prerequisites

- Java 21+
- Docker + Docker Compose

### 1. Start the Database

```bash
cd localdeployment
docker compose up -d
```

This starts a PostgreSQL 17 instance on port `5432` with:
- **User:** `checklist`
- **Password:** `checklist`
- **Database:** `checklist`

### 2. Run the Application

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080`.

Flyway runs automatically on startup and creates the schema.

---

## API Reference

Base path: `/api/checklist-items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/checklist-items` | Get all items (optional `?completed=true/false`) |
| `GET` | `/api/checklist-items/{id}` | Get item by ID |
| `POST` | `/api/checklist-items` | Create a new item |
| `PUT` | `/api/checklist-items/{id}` | Update an existing item |
| `DELETE` | `/api/checklist-items/{id}` | Delete an item |

### Example Requests

**Create an item**
```bash
curl -X POST http://localhost:8080/api/checklist-items \
  -H "Content-Type: application/json" \
  -d '{"title": "Buy groceries", "description": "Milk, eggs, bread"}'
```

**Get all incomplete items**
```bash
curl http://localhost:8080/api/checklist-items?completed=false
```

**Update an item**
```bash
curl -X PUT http://localhost:8080/api/checklist-items/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Buy groceries", "description": "Milk, eggs, bread", "completed": true}'
```

**Delete an item**
```bash
curl -X DELETE http://localhost:8080/api/checklist-items/1
```

---

## Project Structure

```
src/
├── main/
│   ├── java/dev/checklist/
│   │   ├── ChecklistApplication.java
│   │   ├── controller/        # REST controllers
│   │   ├── dto/               # Request/response records
│   │   ├── entity/            # JPA entities
│   │   ├── exception/         # Custom exceptions + global handler
│   │   ├── mapper/            # MapStruct mappers
│   │   ├── repository/        # Spring Data JPA repositories
│   │   └── service/           # Business logic
│   └── resources/
│       ├── application.yml
│       └── db/migration/      # Flyway SQL scripts
└── test/
    └── java/dev/checklist/
        ├── service/           # Unit tests (Mockito)
        └── controller/        # Integration tests (Testcontainers + REST Assured)
```

---

## Running Tests

```bash
./gradlew test
```

Integration tests spin up a real PostgreSQL instance automatically via Testcontainers — no manual setup needed.

---

## Context: AI Benchmark

This project is part of a benchmark evaluating AI coding agents. Four models were given identical prompts to build this application from scratch:

| Model | Total Time | Cost | Runnable OOTB |
|-------|-----------|------|---------------|
| Gemini 2.5 Flash | 5m 57s | $0.21 | Almost |
| Big Pickle (Free) | 6m 47s | Free | Almost |
| Consol Coke | 7m 39s | — | No |
| **Claude Sonnet 4.6** | 28m 15s | $2.49 | **Yes** |

Claude produced the only fully runnable project out of the box — at the cost of being the slowest and most expensive.

> Full benchmark details and presentation: [opencode-playground-springboot](https://github.com/afuerthmaier/opencode-playground)
