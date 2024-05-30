# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Expose the application port
EXPOSE 8088

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/fitness-network.jar fitness-network.jar

# Copy the templates directory into the container
COPY src/main/resources/templates /app/templates

# Set environment variables for Spring Boot to find the templates
ENV SPRING_THYMELEAF_PREFIX=classpath:/app/templates/
ENV SPRING_THYMELEAF_SUFFIX=.html

# Set the entrypoint to run the JAR file
ENTRYPOINT ["java", "-jar", "fitness-network.jar"]
