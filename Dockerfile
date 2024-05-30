FROM openjdk:17
EXPOSE 8088

# Copy the JAR file into the container
ADD target/fitness-network.jar fitness-network.jar

# Copy the templates directory into the container
COPY src/main/resources/templates /templates



# Set the entrypoint to run the JAR file
ENTRYPOINT ["java","-jar","/fitness-network.jar"]
