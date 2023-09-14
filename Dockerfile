# FROM gradle:7.3.3-jdk17 AS build
# WORKDIR /home/gradle/project
# FROM openjdk:17-jdk-alpine
# WORKDIR /app
# COPY ./build/libs/*.jar app.jar
# EXPOSE

FROM openjdk:17-jdk-alpine
ARG JAR-FILE=build/*.jar
COPY ./build/libs/e-voting-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80

ENTRYPOINT ["java", "-jar","/app.jar"]