# Smart-Rwanda-tourism (Backend)

Spring Boot + PostgreSQL backend for Smart Rwanda Tourism.

## Prerequisites

- Docker and Docker Compose

## Getting started

### 1. Clone the repository

```bash
git clone <repository-url>
cd SRT-BE
```

### 2. Set up environment variables

Copy the example file and adjust the values as needed:

```bash
cp .env.example .env
```

### 3. Run with Docker Compose

```bash
docker compose up --build
```

This starts:

- **db** — PostgreSQL
- **app** — the Spring Boot application, available at http://localhost:8080

To stop and remove the containers:

```bash
docker compose down
```
