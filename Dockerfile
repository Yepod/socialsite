
#BYGG APPLIKATION MED MAVEN
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package


FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/spring-boot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]