# Olympo Store

E-commerce de suplementos deportivos — monorepo con frontend React y microservicios Spring Boot.

## Estructura

```
olympo-store/
├── frontend/          # React + Vite
├── backend/
│   ├── login-service/       # Autenticación JWT
│   ├── compras-service/     # Pedidos, carrito, cupones, clientes
│   ├── producto-service/    # Productos, marcas, categorías, imágenes
│   ├── api-gateway/         # Gateway
│   └── notificaciones-service/
└── docker-compose.yml
```

## Levantar en local

```bash
# Backend (todos los servicios)
docker-compose up -d

# Frontend
cd frontend
cp .env.example .env.local   # configura las URLs
npm install
npm run dev
```

## Variables de entorno

Ver `frontend/.env.example` para las variables requeridas del frontend.  
Cada servicio backend tiene su `application.properties` con las variables de base de datos y JWT.
