# Olympo Store — HTTP Tests

Archivos `.http` ejecutables con la extensión **REST Client** de VS Code (`humao.rest-client`).

## Requisitos

1. Instalar la extensión [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) en VS Code.
2. Tener los servicios corriendo en local (o apuntar al entorno deseado cambiando `@gateway`).

## Servicios y puertos

| Servicio          | Puerto directo | A través del gateway |
|-------------------|---------------|---------------------|
| api-gateway       | 8081          | — (es el gateway)   |
| login-service     | 8082          | 8081/auth           |
| producto-service  | 8083          | 8081/productos       |
| compras-service   | 8084          | 8081/api            |

## Orden de ejecución recomendado

1. **`auth.http`** — Hacer login y copiar el `accessToken` obtenido.
2. **`productos.http`** — Verificar catálogo (no requiere token para GET).
3. **`compras.http`** — Flujos de carrito, cupones, pedidos y dashboard (requieren token).

## Variables globales

Cada archivo define `@gateway = http://localhost:8081`. Puedes crear un archivo `http-client.env.json` en la raíz del workspace para sobreescribir esto por entorno:

```json
{
  "local": { "gateway": "http://localhost:8081" },
  "prod":  { "gateway": "https://api.olympo-store.com" }
}
```

## Flujo rápido (happy path)

```
1. auth.http      → POST /auth/login        → copia accessToken
2. compras.http   → POST /api/clientes      → copia clienteId
3. compras.http   → POST /api/carritos      → copia carritoId
4. compras.http   → PUT  /api/carritos/{id} → actualiza items
5. compras.http   → POST /api/pedidos       → crea el pedido
6. auth.http      → GET  /auth/usuarios     → (como admin) lista usuarios
7. compras.http   → GET  /api/dashboard/resumen → KPIs del panel admin
```

## Nota sobre multipart (subir imágenes)

`POST /productos` requiere `multipart/form-data`. El REST Client soporta esto con la sintaxis `--boundary`. Ver el ejemplo comentado en `productos.http`.
