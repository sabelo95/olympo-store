# 🏪 Arka Project - Sistema de E-commerce

Sistema de comercio electrónico basado en microservicios desarrollado con Spring Boot.

## 📋 Descripción

Arka Project es un sistema de e-commerce compuesto por múltiples microservicios que trabajan juntos para proporcionar funcionalidades completas de gestión de productos, autenticación, compras y notificaciones.

## 🏗️ Arquitectura

El proyecto está dividido en los siguientes microservicios:

- **API Gateway** (Puerto 8081) - Punto de entrada para todas las peticiones
- **Login Service** (Puerto 8082) - Gestión de autenticación y usuarios
- **Producto Service** (Puerto 8083) - Gestión de productos, categorías y marcas
- **Compras Service** (Puerto 8084) - Gestión de pedidos, carritos y clientes
- **Notificaciones Service** (Puerto 8085) - Envío de notificaciones por correo

## 📚 Documentación API con Swagger

Todos los microservicios incluyen documentación interactiva con Swagger UI. Puedes acceder a la documentación de cada servicio a través de los siguientes enlaces:

### 🌐 Despliegue Local

Cuando ejecutes los servicios en tu máquina local, puedes acceder a la documentación en:

| Servicio | URL Swagger UI | URL API Docs (JSON) |
|----------|----------------|---------------------|
| **API Gateway** | http://localhost:8081/swagger-ui.html | http://localhost:8081/api-docs |
| **Login Service** | http://localhost:8082/swagger-ui.html | http://localhost:8082/api-docs |
| **Producto Service** | http://localhost:8083/swagger-ui.html | http://localhost:8083/api-docs |
| **Compras Service** | http://localhost:8084/swagger-ui.html | http://localhost:8084/api-docs |
| **Notificaciones Service** | http://localhost:8085/swagger-ui.html | http://localhost:8085/api-docs |

### 🐳 Despliegue Docker

Si ejecutas los servicios con Docker Compose, puedes acceder a la documentación en:

| Servicio | URL Swagger UI | URL API Docs (JSON) |
|----------|----------------|---------------------|
| **API Gateway** | http://localhost:8081/swagger-ui.html | http://localhost:8081/api-docs |
| **Login Service** | http://localhost:8082/swagger-ui.html | http://localhost:8082/api-docs |
| **Producto Service** | http://localhost:8083/swagger-ui.html | http://localhost:8083/api-docs |
| **Compras Service** | http://localhost:8084/swagger-ui.html | http://localhost:8084/api-docs |
| **Notificaciones Service** | http://localhost:8085/swagger-ui.html | http://localhost:8085/api-docs |

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 17 o superior
- PostgreSQL
- MongoDB (para el servicio de notificaciones)
- Docker y Docker Compose (opcional)

### Ejecución con Docker Compose

1. Clona el repositorio:
```bash
git clone <url-del-repositorio>
cd arka-project
```

2. Inicia todos los servicios con Docker Compose:
```bash
docker-compose up -d
```

3. Los servicios estarán disponibles en los puertos especificados arriba.

### Ejecución Local

Para ejecutar cada servicio individualmente:

```bash
# Compilar y ejecutar cada servicio
cd api-gateway && ./gradlew bootRun
cd login-service && ./gradlew bootRun
cd producto-service && ./gradlew bootRun
cd compras-service && ./gradlew bootRun
cd notificaciones-service && ./gradlew bootRun
```

## 📖 Endpoints Principales

### API Gateway
- Rutea las peticiones a los diferentes microservicios

### Login Service
- `POST /auth/login` - Iniciar sesión
- `POST /auth/crear` - Registrar nuevo usuario
- `GET /auth/obtener/{id}` - Obtener usuario por ID
- `POST /auth/refresh` - Refrescar token

### Producto Service
- `GET /productos` - Listar todos los productos
- `POST /productos` - Crear nuevo producto (requiere autenticación)
- `PUT /productos/{nombre}` - Actualizar producto
- `DELETE /productos/{nombre}` - Eliminar producto
- `GET /productos/categoria/{nombre}` - Filtrar por categoría
- `GET /productos/marca/{nombre}` - Filtrar por marca

### Compras Service
- `POST /api/pedidos/carrito/{id}/cliente/{clienteId}` - Crear pedido
- `PUT /api/pedidos/actualizar` - Actualizar pedido
- `DELETE /api/pedidos` - Eliminar pedido

### Notificaciones Service
- `POST /notificaciones` - Enviar notificación por correo
- `POST /notificaciones/con-adjunto` - Enviar notificación con archivo adjunto

## 🔐 Seguridad

Los servicios utilizan JWT (JSON Web Tokens) para autenticación. Al hacer login, recibirás un token que debes incluir en el header `Authorization: Bearer {token}` para acceder a endpoints protegidos.

## 🛠️ Tecnologías

- **Spring Boot 3.x**
- **Spring Security**
- **Spring Cloud Gateway**
- **PostgreSQL**
- **MongoDB**
- **JWT**
- **Lombok**
- **iTextPDF** (para reportes)
- **SpringDoc OpenAPI** (Swagger)

## 📝 Notas

- Los servicios están configurados para ejecutarse en puertos específicos para evitar conflictos.
- La configuración de base de datos está en los archivos `application.properties` o `application.yml` de cada servicio.
- Los reportes se generan en el directorio `producto-service/reportes/`.

## 👥 Contacto

- **Email**: santiagoberriolopez@gmail.com
- **Equipo**: Arka

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0.

