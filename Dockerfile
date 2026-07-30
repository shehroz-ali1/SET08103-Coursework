# Use a lightweight Java 17 image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the self-contained JAR file into the container
COPY target/set08103-coursework-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
