# syntax=docker/dockerfile:1

# Stage build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY pom.xml .
COPY src src

RUN ./mvnw package


# Stage run
FROM eclipse-temurin:17-jre AS app
WORKDIR /app

ENV JAVA_OPTS=""
COPY --from=build /app/target/*.jar university.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar university.jar"]
