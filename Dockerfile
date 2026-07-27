# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Create a non-root user
RUN groupadd --system spring && useradd --system --gid spring spring

# Create the upload dir and give the spring user ownership (must happen as root, before USER)
RUN mkdir -p /data/uploads/profiles && chown -R spring:spring /data

# Now drop to the non-root user
USER spring:spring

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]