# ===========================
# Etapa 1: Construcción
# ===========================
FROM gradle:8.14-jdk17-alpine AS build

WORKDIR /app

# Copiar configuración base (para cache de dependencias)
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY src/ src/

# Construir el JAR
RUN ./gradlew bootJar --no-daemon -x test

# ===========================
# Etapa 2: Imagen final
# ===========================
FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="Equipo de desarrollo"
LABEL description="Microservicio de Compras - Arka Project"
LABEL version="0.0.1-SNAPSHOT"

# Crear usuario no-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto del microservicio (ajústalo si no es 8084)
EXPOSE 8084

# Variables por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check opcional
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
