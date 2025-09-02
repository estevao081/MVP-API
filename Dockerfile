FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/target/neoProjectAPI-0.0.1-SNAPSHOT.jar /app/neo-project-api.jar
ENTRYPOINT ["java", "-jar", "neo-project-api.jar"]