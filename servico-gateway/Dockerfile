FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

RUN apk add --no-cache maven

COPY . .

RUN mvn clean package -DskipTests -pl servico-gateway -am

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/servico-gateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]