# Dockerfile
FROM openjdk:23-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=/app/config/application.properties"]

COPY ./src/main/resources/application.properties /app/config/application.properties