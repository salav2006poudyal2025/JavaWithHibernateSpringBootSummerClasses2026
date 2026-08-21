# Use official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application skipping tests
RUN mvn clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the JAR file
CMD ["sh", "-c", "java -jar target/*.jar"]
