FROM maven:3.6.3-jdk-11 AS target

WORKDIR /build/

COPY ../pom.xml .

COPY ../src/ ./src/
RUN mvn install -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /build/
COPY --from=target /build/target/*.jar /app/freeacademy.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","/app/freeacademy.jar"]