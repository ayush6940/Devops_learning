# Minimal Microservices Architecture

This repository contains two minimal, independent Spring Boot microservices designed to serve as a clean foundation for implementing DevOps practices (Docker, CI/CD, Kubernetes, etc.) later on.

## Overview

1. **mood-board-service** (Runs on port `8081`)
2. **geography-service** (Runs on port `8082`)

Both services are extremely lightweight. They contain **no database, no CRUD operations, no security, and no external dependencies** other than Spring Web. They exist purely to accept HTTP requests and return JSON responses.

---

## Prerequisites

*   Java 21 installed.
*   Maven installed.

---

## How to Run Locally

Because these are independent microservices with no shared infrastructure (like a message broker or database), you can run them simultaneously in separate terminal windows.

### 1. Running the `mood-board-service`

Open a terminal and navigate to the `mood-board-service` directory:
```bash
cd mood-board-service
```

Run the application using Maven:
```bash
mvn spring-boot:run
```
The service will start on `http://localhost:8081`.

**Test the endpoints:**
*   GET Health: `curl http://localhost:8081/api/mood/health`
*   GET Hello: `curl http://localhost:8081/api/mood/hello`
*   POST Echo:
    ```bash
    curl -X POST http://localhost:8081/api/mood/echo \
         -H "Content-Type: application/json" \
         -d '{"message": "testing echo"}'
    ```

### 2. Running the `geography-service`

Open a **new** terminal window and navigate to the `geography-service` directory:
```bash
cd geography-service
```

Run the application using Maven:
```bash
mvn spring-boot:run
```
The service will start on `http://localhost:8082`.

**Test the endpoints:**
*   GET Health: `curl http://localhost:8082/api/geography/health`
*   GET Capital: `curl http://localhost:8082/api/geography/capital`
*   POST Echo:
    ```bash
    curl -X POST http://localhost:8082/api/geography/echo \
         -H "Content-Type: application/json" \
         -d '{"lat": "48.8566", "lon": "2.3522"}'
    ```

---

## Why these files exist

*   **`pom.xml`**: Manages the project's dependencies (only `spring-boot-starter-web` is used to expose REST APIs) and configures the build process.
*   **`application.properties`**: Configures the application port (`server.port`) so the two services don't collide when running simultaneously.
*   **`*Application.java`**: The main class that bootstraps and launches the embedded Tomcat web server.
*   **`*Controller.java`**: The REST layer that defines the URL endpoints and maps incoming HTTP requests to Java methods.
*   **`*Service.java`**: The service layer that contains the minimal logic (returning predefined Maps/JSON) to simulate business logic without any actual complexity or data persistence.
