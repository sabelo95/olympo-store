FROM gradle:8.14-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src/ src/
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN apk add --no-cache wget
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
USER appuser
EXPOSE 8084
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx200m -Xms64m -XX:+UseSerialGC"
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD wget -qO- http://localhost:8084/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
