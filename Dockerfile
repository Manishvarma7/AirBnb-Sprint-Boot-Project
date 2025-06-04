# --- Stage 1: Build the Spring Boot application ---
FROM openjdk:17-jdk-slim as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files (pom.xml, src folder) to the build stage
# We copy pom.xml separately to leverage Docker cache if only source code changes
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
# -DskipTests skips running tests during build (faster, but make sure tests pass locally)
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final production image ---
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
# The path 'target/*.jar' is a wildcard to handle potential version changes in the JAR name
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Define the entry point to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]