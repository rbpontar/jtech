# Multi-stage Dockerfile for building and running the Tasklist Spring Boot application

# Build stage: use Maven with JDK 17 to build the fat jar
FROM maven:3.9.10-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only what we need to leverage Docker cache
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Download dependencies and build the application (skip tests for speed; adjust if needed)
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

# Run stage: lightweight JRE image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy executable jar from build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
