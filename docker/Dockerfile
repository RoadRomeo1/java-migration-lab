# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy root pom and common-lib first for better caching
COPY pom.xml .
COPY common-lib/pom.xml common-lib/
COPY people-management-service/pom.xml people-management-service/
COPY tax-engine-service/pom.xml tax-engine-service/

# Cache dependencies
RUN mvn dependency:go-offline -B

# Copy all source code
COPY . .

# Build all modules
RUN mvn clean package -DskipTests -B

# Final runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Add a generic JAR argument to pick the right one
ARG MODULE_JAR
COPY ${MODULE_JAR} app.jar

EXPOSE 8080
EXPOSE 8081

ENV JAVA_OPTS="-XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  --enable-preview"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
