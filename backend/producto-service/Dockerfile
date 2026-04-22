FROM gradle:8.14-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src/ src/
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8083
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx200m -Xms64m -XX:+UseSerialGC"
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
