# --- Stage 1: Build the Spring Boot application ---
# Use a Maven image that includes OpenJDK 17 (Eclipse Temurin distribution)
FROM maven:3.9.6-eclipse-temurin-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files from the 'AirBnb' subfolder
COPY AirBnb/pom.xml .
COPY AirBnb/src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final production image ---
# Use a slim OpenJDK image for the final application, as it's smaller
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Define the entry point to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]