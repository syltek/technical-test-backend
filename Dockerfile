#Build stage
FROM maven:3.6.1-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml integration-test

#Package stage
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/exercise-wallet-0.0.2.jar /usr/local/lib/exercise-wallet.jar
COPY src/main/resources/application.yml /usr/local/lib/application.yml
EXPOSE 8090
ENTRYPOINT ["java", "-Dspring.config.location=/usr/local/lib/application.yml", "-jar", "/usr/local/lib/exercise-wallet.jar"]