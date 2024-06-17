# Use an official Maven image as a parent image
FROM maven:3.8.5-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the entire source code
COPY src/ ./src/

# Build the Maven project
RUN mvn clean package -DskipTests -Dmaven.build.finalName=automation-suite

# Use a lighter base image for the final container
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/automation-suite.jar .

# Command to run your application
CMD ["java", "-jar", "automation-suite.jar"]
