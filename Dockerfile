FROM maven:3.9.6-eclipse-temurin-21-alpine as BUILD

COPY . .

RUN mvn clean package -DskipTest

FROM openjdk:21-jdk-slim
COPY --from=BUILD target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app-jar"]