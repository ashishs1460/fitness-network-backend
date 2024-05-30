FROM openjdk:17
EXPOSE 8088
ADD target/fitness-network.jar fitness-network.jar
ENTRYPOINT ["java","-jar","/fitness-network.jar"]