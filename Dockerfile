FROM openjdk:17-jdk-slim AS builder

# Set working directory for Gradle build
WORKDIR /app

# Copy project files
COPY . .

# Install Gradle dependencies (adjust based on your build command)
RUN ./gradlew build

# Build a thin jar using optimizations
RUN ./gradlew bootJar

# Create a new minimal image based on Alpine Linux for the final application
FROM alpine:latest

RUN apk add --no-cache openjdk17-jre

# Set working directory for the application
WORKDIR /app

# Copy only the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port where your Spring Boot application listens (adjust as needed)
EXPOSE 8080

# Start the application by running the JAR
CMD ["java","-Dspring.profiles.active=docker", "-jar", "app.jar"]