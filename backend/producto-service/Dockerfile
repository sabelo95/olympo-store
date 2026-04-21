## ===========================
## Etapa 1: Construcción
## ===========================
#FROM gradle:8.14-jdk17-alpine AS build
#
## Directorio de trabajo
#WORKDIR /app
#
## Copiar los archivos de configuración Gradle (para usar cache)
#COPY build.gradle settings.gradle gradlew ./
#COPY gradle/ gradle/
#
## Descargar dependencias
#RUN ./gradlew dependencies --no-daemon || return 0
#
## Copiar el código fuente
#COPY src/ src/
#
## Construir el JAR (sin tests para acelerar)
#RUN ./gradlew bootJar --no-daemon -x test
#
## ===========================
## Etapa 2: Imagen final (runtime)
## ===========================
#FROM eclipse-temurin:17-jdk-alpine
#
## Información de la imagen
#LABEL maintainer="Equipo de desarrollo"
#LABEL description="Microservicio de Productos - Arka Project"
#LABEL version="1.0.0"
#
## Crear usuario no-root
#RUN addgroup -g 1001 -S appgroup && \
#    adduser -u 1001 -S appuser -G appgroup
#
## Instalar herramientas útiles
#RUN apk add --no-cache curl
#
## Directorio de trabajo
#WORKDIR /app
#
## Copiar el JAR desde la etapa de construcción
#COPY --from=build /app/build/libs/*.jar app.jar
#
## Cambiar propietario
#RUN chown -R appuser:appgroup /app
#USER appuser
#
## Puerto expuesto (ajústalo si tu microservicio usa otro)
#EXPOSE 8083
#
## Variables de entorno por defecto
#ENV SPRING_PROFILES_ACTIVE=docker
#ENV JAVA_OPTS="-Xmx512m -Xms256m"
#
## Healthcheck para monitoreo
#HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
#    CMD curl -f http://localhost:8083/actuator/health || exit 1
#
## Comando de ejecución
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# ===========================
# Imagen final (runtime only)
# ===========================
# ===========================
# Imagen final (runtime only) - Compatible Windows/Linux
# ===========================
FROM eclipse-temurin:17-jre

# Información de la imagen
LABEL maintainer="Equipo de desarrollo"
LABEL description="Microservicio de Productos - Arka Project"
LABEL version="1.0.0"

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR ya compilado
COPY producto-service-*.jar app.jar

# Puerto expuesto
EXPOSE 8083

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck para monitoreo
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher --spring.main.web-application-type=none || exit 1

# Comando de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

#probanbo en expo
