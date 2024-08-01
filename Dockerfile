FROM openjdk:17-jdk-slim

COPY /target/*.jar saga-orchestrator-service-app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "saga-orchestrator-service-app.jar"]