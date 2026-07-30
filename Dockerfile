# Stage 1: Build the self-contained JAR
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /app/target/set08103-coursework-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]