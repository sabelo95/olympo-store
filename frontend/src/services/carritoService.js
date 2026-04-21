const API_CARRITO = `${import.meta.env.VITE_API_TIENDA}/api/carritos`;

const getToken = () => localStorage.getItem("accessToken");
const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export const crearCarrito = (payload) =>
  fetch(API_CARRITO, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  }).then((r) => {
    if (!r.ok) return r.text().then((t) => Promise.reject(t));
    return r.json();
  });

export const actualizarCarrito = (id, clienteId, payload) =>
  fetch(`${API_CARRITO}/${id}/${clienteId}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(payload),
  }).then((r) => {
    if (!r.ok) return r.text().then((t) => Promise.reject(t));
    return r.text();
  });

export const eliminarCarrito = (id) =>
  fetch(`${API_CARRITO}/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  }).then((r) => {
    if (!r.ok) return r.text().then((t) => Promise.reject(t));
    return r.text();
  });

export const construirPayload = (clienteId, lineas, abandonado = true) => ({
  clienteId,
  abandonado,
  productos: lineas.map((l) => ({
    productoId: l.productoId,        // ← corregido
    productoNombre: l.nombre,
    sabor: l.sabor ?? null,
    tamano: l.tamano ?? null,
    cantidad: l.cantidad,
    precioUnitario: l.precio,
  })),
});

export const obtenerCarritoActivo = (clienteId) =>
  fetch(`${API_CARRITO}/cliente/${clienteId}/activo`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
  }).then((r) => {
    if (r.status === 204) return null; // sin carrito activo
    if (!r.ok) return null;
    return r.json();
  });

export const fmt = (n) =>
  n?.toLocaleString("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 });