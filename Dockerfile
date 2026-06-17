FROM openjdk:11-jre-slim


WORKDIR /app

COPY edu-365-back-end/target/*.jar /app/

RUN mv /app/*.jar  backend_freeacademy.jar


CMD ["java","-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "backend_freeacademy.jar"]
