# --- Stage 1: Build the Spring Boot application ---
FROM openjdk:17-jdk-slim as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files from the 'AirBnb' subfolder
# The path is relative to the Dockerfile's location (repository root)
COPY AirBnb/pom.xml .
COPY AirBnb/src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final production image ---
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
# This path is relative to the WORKDIR in the build stage (/app/target/...)
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Define the entry point to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]