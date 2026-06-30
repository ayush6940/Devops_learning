# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build a release artifact.
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image to run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8081

# Run the web service on container startup
ENTRYPOINT ["java", "-jar", "app.jar"]
