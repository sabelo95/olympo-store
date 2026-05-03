# Olympo Store — QA Manual Checklist

Ejecutar en orden. Marcar `[x]` cuando el ítem pasa, `[!]` si falla (añadir nota).

---

## Flujo 1 — Catálogo (sin sesión)

- [ ] Navegar a `/productos` sin estar logueado — la página carga sin error.
- [ ] La grilla de productos muestra imágenes correctamente.
- [ ] Un producto que tiene imagen solo en una variante: **la imagen aparece en todas sus variantes** (seleccionar sabores/tamaños y verificar que la imagen no desaparece).
- [ ] Producto con `cantidad = 0` **no aparece** en el catálogo (verificar en BD o admin).
- [ ] Filtros por categoría, marca y precio funcionan y actualizan la lista.
- [ ] Paginación avanza y retrocede correctamente.
- [ ] Click en una tarjeta de producto abre `/productos/:nombre` (página de detalle).
- [ ] En detalle, seleccionar sabor/tamaño actualiza precio y stock mostrado.
- [ ] Zoom de imagen funciona (click en imagen → se abre overlay ampliado).

---

## Flujo 2 — Carrito (invitado → login → logout)

- [ ] Agregar un producto al carrito sin estar logueado — el contador del carrito sube.
- [ ] Cerrar y reabrir la pestaña — los items del carrito siguen ahí (localStorage persiste).
- [ ] Abrir el panel del carrito — los items muestran nombre, precio y cantidad correctos.
- [ ] Cambiar la cantidad de un item en el carrito — el subtotal se actualiza.
- [ ] Eliminar un item — desaparece del carrito y el total se recalcula.
- [ ] Vaciar carrito — el carrito queda vacío.
- [ ] **BUG CHECK**: Agregar items sin login → ir a checkout → el sistema pide login.
  - Después de hacer login, ¿el carrito sigue con los items? (debe seguir).
- [ ] **Aislamiento de sesiones**: Login como USUARIO_A con carrito → logout → login como USUARIO_B → el carrito de B está vacío (no mezcla carritos).
- [ ] Logout → `olympo_carrito` se elimina de `localStorage` (verificar en DevTools → Application → Local Storage).
- [ ] Login de nuevo con el mismo usuario → el carrito se restaura desde el servidor (los items deben aparecer).
- [ ] Agregar items logueado → esperar ~2 segundos (debounce) → verificar en BD o via `/api/carritos/cliente/:id` que el carrito fue guardado.

---

## Flujo 3 — Checkout completo

- [ ] Navegar a `/checkout` sin sesión → redirige al login.
- [ ] Con sesión, `/checkout` muestra el formulario y el resumen del carrito.
- [ ] Formulario de dirección valida campos obligatorios (nombre, dirección, ciudad, teléfono) antes de enviar.
- [ ] Campo de cupón acepta un código válido → muestra el descuento aplicado.
- [ ] Cupón con monto menor al mínimo → muestra mensaje de error, no aplica descuento.
- [ ] Cupón expirado → muestra mensaje de error.
- [ ] Código de cupón inexistente → muestra mensaje de error.
- [ ] Subtotal > $150.000 → envío gratis (verifica que el costo de envío sea $0).
- [ ] Subtotal < $150.000 → muestra el costo de envío estándar.
- [ ] Método de pago "Transferencia bancaria" → click en confirmar → se crea el pedido.
- [ ] **BUG CHECK (B2)**: Después de crear el pedido exitosamente:
  - El carrito en `localStorage` se elimina (`olympo_carrito` ya no existe).
  - El carrito en el servidor también se elimina (GET `/api/carritos/cliente/:id` devuelve vacío o 404).
  - Si alguno de los dos NO se limpia, el bug B2 está presente.
- [ ] Después de crear el pedido se muestra un mensaje de éxito o se redirige correctamente.

---

## Flujo 4 — Pago con Wompi

- [ ] En checkout, seleccionar método de pago Wompi/tarjeta.
- [ ] El widget de Wompi carga correctamente sin errores en consola del navegador.
- [ ] La firma es generada por el backend (`/api/wompi/firma`) antes de montar el widget.
- [ ] Completar el pago de prueba → el sistema redirige a `/checkout/resultado`.
- [ ] La página `/checkout/resultado` muestra el estado de la transacción.
- [ ] Verificar en consola del navegador que no hay errores 4xx/5xx durante todo el flujo.

---

## Flujo 5 — Mi perfil y Mis pedidos

- [ ] Navegar a `/perfil` con sesión activa → carga los datos del usuario.
- [ ] Nombre, correo y teléfono se muestran correctamente.
- [ ] Click en "Editar" → los campos se vuelven editables.
- [ ] Modificar el nombre → guardar → el nombre se actualiza y se persiste al recargar.
- [ ] Click en "Cancelar" → los cambios se descartan y los campos vuelven a solo-lectura.
- [ ] Navegar a `/mis-pedidos` → se listan los pedidos del usuario autenticado.
- [ ] Pedidos en distintos estados muestran el badge de estado correcto (PENDIENTE, EN_PROCESO, ENVIADO, ENTREGADO).
- [ ] Click en un pedido muestra el detalle (productos, dirección, total).
- [ ] Si el usuario no tiene pedidos, se muestra un mensaje informativo.

---

## Flujo 6 — Admin: Productos

- [ ] Navegar a `/admin/productos` con rol ADMINISTRADOR → carga la tabla.
- [ ] La tabla muestra nombre, precio, stock, categoría y marca.
- [ ] Filtros y búsqueda de la tabla funcionan.
- [ ] Paginación funciona.
- [ ] Click en "Agregar producto" → se abre el formulario.
- [ ] Crear un producto con todos los campos → aparece en la tabla.
- [ ] Subir imagen desde el thumbnail de la tabla (click directo en la imagen) → la imagen se actualiza.
- [ ] Editar un producto existente → los cambios se guardan y se reflejan en el catálogo de clientes.
- [ ] Eliminar un producto → desaparece de la tabla y del catálogo.
- [ ] Producto eliminado → no aparece en `/productos` ni en `/productos/:nombre`.
- [ ] Reporte de inventario bajo → click en el botón → se descarga/genera el PDF.

---

## Flujo 7 — Admin: Ventas y Pedidos

- [ ] Navegar a `/admin/ventas` → se lista todos los pedidos.
- [ ] Filtrar por estado funciona.
- [ ] Click en un pedido → se ve el detalle completo (cliente, productos, monto).
- [ ] Cambiar el estado de un pedido (ej: PENDIENTE → EN_PROCESO) → se guarda y se refleja en "Mis pedidos" del cliente.
- [ ] Cambiar estado a ENTREGADO → verificar si el stock se reduce (depende de implementación).
- [ ] Reporte de ventas por rango de fechas → genera el PDF con los datos del período.
- [ ] **Opcional**: Enviar reporte por correo → el correo llega a la dirección configurada.

---

## Flujo 8 — Admin: Usuarios y Cupones

**Usuarios:**
- [ ] Navegar a `/admin/usuarios` → se lista todos los usuarios del sistema.
- [ ] Crear usuario nuevo desde el panel → el usuario aparece en la lista.
- [ ] Activar/desactivar usuario → el estado cambia (el usuario desactivado no puede loguearse).
- [ ] Eliminar usuario → desaparece de la lista.

**Cupones:**
- [ ] Navegar a `/admin/cupones` → se lista todos los cupones.
- [ ] Crear cupón con descuento porcentual → el cupón puede validarse desde el checkout.
- [ ] Crear cupón con descuento fijo → se aplica correctamente.
- [ ] Crear cupón con fecha de expiración pasada → en el checkout se rechaza con mensaje de error.
- [ ] Crear cupón con monto mínimo → en el checkout se rechaza si el subtotal es menor.
- [ ] Crear cupón con uso máximo = 1 → después de ser usado una vez, se rechaza en el siguiente intento.
- [ ] Actualizar cupón → los cambios se aplican inmediatamente.
- [ ] Eliminar cupón → el código ya no puede validarse.

---

## Flujo 9 — Autenticación y seguridad de rutas

- [ ] Navegar a `/dashboard` sin sesión → redirige al login.
- [ ] Navegar a `/admin/productos` sin sesión → redirige al login.
- [ ] Navegar a `/admin/productos` con rol CLIENTE → redirige (acceso denegado).
- [ ] Navegar a `/mis-pedidos` sin sesión → redirige al login.
- [ ] **BUG CHECK (B1)**: Abrir DevTools → Network. Esperar que el `accessToken` expire (~15 min). Navegar al catálogo o hacer una acción que requiera API. Verificar si:
  - La app muestra un error 401 en silencio (bug presente), o
  - La app intenta refrescar el token automáticamente (fetchAuth.js, comportamiento correcto).
  - **Actualmente la mayoría de servicios usan `fetch()` directo → bug B1 es probable.**
- [ ] Logout borra `accessToken`, `refreshToken` y `clienteId` de `localStorage`.
- [ ] Después de logout, navegar atrás con el navegador → la app redirige al login (no hay acceso con token inválido).
- [ ] Acceder a `/login` o `/` ya logueado → redirige directamente al catálogo o dashboard según el rol.

---

## Flujo 10 — Responsive (mobile)

Probar en resolución ≤ 768px (o con DevTools en modo mobile):

- [ ] Navbar del catálogo se adapta y el carrito es accesible.
- [ ] Grilla de productos cambia a 1 columna en mobile pequeño (≤480px).
- [ ] Formulario de login/registro es legible y los inputs no hacen zoom en iOS.
- [ ] Checkout: el grid de dos columnas se apila verticalmente.
- [ ] Panel de carrito ocupa el ancho completo.
- [ ] Admin panel: el sidebar se oculta y aparece el botón hamburger.
- [ ] Click en hamburger → sidebar se abre con overlay.
- [ ] Click fuera del sidebar → se cierra.
- [ ] Todos los botones de acción tienen al menos 44px de altura (touch target).
- [ ] Mis pedidos y Perfil se ven correctamente sin overflow horizontal.

---

## Bugs conocidos a verificar

| # | Descripción | Severidad | ¿Confirmado? |
|---|---|---|---|
| B1 | Servicios usan `fetch()` directo, no `fetchAuth.js`. Token expirado → 401 silencioso, sin auto-refresh. | Media | [ ] |
| B2 | `Checkout.jsx`: después de `crearPedido()` el carrito NO se elimina (ni localStorage ni servidor). | Media | [ ] |
| B3 | ~~Ruta `/checkout/resultado` inexistente~~ — **RESUELTO**: la ruta existe en `App.jsx`. | — | [x] |
