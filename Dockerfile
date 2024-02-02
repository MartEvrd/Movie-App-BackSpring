FROM maven:3.9.6-eclipse-temurin-21 as BUILD

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

COPY --from=BUILD target/*.jar /app.jar

ENV DB_USERNAME=postgres
ENV DB_PASSWORD=admin
ENV DB_URL=jdbc:postgresql://localhost:5432/movieApp
ENV JWT_SECRET=D09A202702553A280AE2BF8D0EBD1625D3B09C9C88A76368D92C22D67FA854DA

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]