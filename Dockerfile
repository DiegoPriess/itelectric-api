FROM maven:3.9.4-amazoncorretto:21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=builder /app/target/iteletric-api-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]