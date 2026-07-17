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
# test github action by ayush

## Monitoring Stack

The project includes an end-to-end monitoring stack to observe the health and performance of the microservices.

### 1. Monitoring Architecture
Client Request -> Microservices (Mood Board & Geography)
Microservices -> Actuator + Micrometer -> Prometheus -> Grafana

### 2. Role of Spring Boot Actuator
Spring Boot Actuator provides built-in endpoints (like `/actuator/health` and `/actuator/prometheus`) that expose operational information about the running application.

### 3. Role of Micrometer
Micrometer acts as an application metrics facade (similar to SLF4J for logging). It formats the metrics collected by Actuator into the specific format required by Prometheus.

### 4. Role of Prometheus
Prometheus is a time-series database and monitoring system. It periodically scrapes (pulls) metrics from the `/actuator/prometheus` endpoints of both services and stores them.

### 5. Role of Grafana
Grafana is the visualization layer. It connects to Prometheus as a data source and displays the collected metrics on rich, interactive dashboards.

### 6. How Prometheus Scraping Works
Prometheus is configured in `monitoring/prometheus.yml` with a scrape interval of 15 seconds. It makes HTTP GET requests to `http://mood-board-compose:8081/actuator/prometheus` and `http://geography-compose:8082/actuator/prometheus` to fetch metrics.

### 7. How Docker Service Discovery Works
Because all containers run on the same Docker Compose network, they can resolve each other by container name. Prometheus uses `mood-board-compose` and `geography-compose` instead of `localhost` to reach the services.

### 8. How to Start the Monitoring Stack
Run the entire stack using Docker Compose:
```bash
docker-compose up --build -d
```
This will start Kafka, both Spring Boot services, Prometheus, and Grafana.

### 9. How to Access Actuator Endpoints
- Mood Board Service: `http://localhost:8081/actuator/health`, `http://localhost:8081/actuator/prometheus`
- Geography Service: `http://localhost:8082/actuator/health`, `http://localhost:8082/actuator/prometheus`

### 10. How to Access Prometheus
Open `http://localhost:9090` in your browser.

### 11. How to Access Grafana
Open `http://localhost:3000` in your browser.
Default login is not required for viewing if anonymous access is enabled, but you can log in with:
- **Username:** `admin`
- **Password:** `admin` (set via `GF_SECURITY_ADMIN_PASSWORD`)

### 12. How to Verify Prometheus Targets
In Prometheus (`http://localhost:9090`), navigate to **Status > Targets**. Both `mood-board-service` and `geography-service` should be listed with a State of **UP**.

### 13. How to View Dashboards
In Grafana (`http://localhost:3000`), navigate to **Dashboards**. The Spring Boot JVM Micrometer dashboard is automatically provisioned and ready to use.

### 14. Common Troubleshooting Steps
- **Target is DOWN in Prometheus:** Check if the Spring Boot service is actually running. Verify the service name and port in `prometheus.yml` match the `docker-compose.yml`.
- **No data in Grafana:** Ensure Prometheus is selected as the default data source and that Prometheus targets are UP. Check the time range in the Grafana dashboard.
- **Port Conflicts:** If ports 9090 or 3000 are already in use on your host, change the mapping in `docker-compose.yml` (e.g., `"9091:9090"`).


## Centralized Logging Stack

The project features a full centralized logging stack, complementing the monitoring stack, allowing deep-dive investigation into application events, errors, and traces.

### 1. Centralized Logging Architecture

```text
Spring Boot Services (mood-board-service, geography-service)
       |
    Logback (Writes to /var/logs/spring-boot/*.log)
       |
  Fluent Bit (Tails log files, enriches with metadata)
       |
  OpenSearch (Indexes and stores logs)
       |
OpenSearch Dashboards (Search, filter, and visualize)
```

### 2. Component Roles
* **Logback:** The default logging framework for Spring Boot. It formats logs with SLF4J, adds MDC correlation IDs (e.g., `requestId`), and writes them to rolling file appenders.
* **Fluent Bit:** A lightweight, high-performance log processor and forwarder. It reads the local log files generated by Logback, tags them by service, and forwards them to OpenSearch.
* **OpenSearch:** A distributed, RESTful search and analytics engine that securely indexes and stores the log streams in a centralized location.
* **OpenSearch Dashboards:** The UI for OpenSearch. It allows you to query, filter, and visualize the log data without querying containers individually.

### 3. Monitoring vs Logging
* **Monitoring (Prometheus + Grafana):** Focuses on system health, metrics, and detecting problems ("Is something wrong?"). It tracks memory, CPU, and request rates.
* **Logging (Fluent Bit + OpenSearch):** Focuses on investigating events and root causes ("Why is it wrong?"). It tracks the exact flow of execution, errors, and detailed application state.

### 4. How to Start the Complete Stack
Run the entire stack using Docker Compose:
```bash
docker-compose up --build -d
```
This starts Kafka, the microservices, Prometheus, Grafana, OpenSearch, OpenSearch Dashboards, and Fluent Bit.

### 5. Accessing OpenSearch Dashboards
1. Open `http://localhost:5601` in your browser.
2. Log in with the default credentials:
   * **Username:** `admin`
   * **Password:** `Admin@12345` (Configured in `docker-compose.yml`)

### 6. Searching and Filtering Logs
1. Navigate to **Discover** in OpenSearch Dashboards.
2. Create an Index Pattern for `microservices-logs*`.
3. **Filter by Service:** Use the filter bar to add `service: mood-board-service` or `service: geography-service`.
4. **Filter by Level:** Type `level: ERROR` or `level: WARN` in the search bar.
5. **Trace a Request:** Search for a specific `correlationId` to trace the entire event lifecycle as it moves from Mood Board Service to Geography Service through Kafka.

### 7. Resource Requirements
Running OpenSearch locally is resource-intensive. The `docker-compose.yml` configures a hard limit of 512MB for OpenSearch JVM heap (`-Xms512m -Xmx512m`). Ensure your Docker environment has at least 4GB of RAM allocated in total for the entire stack to run smoothly.

### 8. Troubleshooting
* **Logs not appearing:** Check if Fluent Bit is running (`docker logs local-fluent-bit`) and verify it connected to OpenSearch successfully.
* **OpenSearch crashes:** Typically an Out of Memory (OOM) error. Increase Docker Engine memory allocation.
* **Container name conflicts:** Ensure ports `9200` (OpenSearch) and `5601` (Dashboards) are free on your host machine.
