const API_URL = `${import.meta.env.VITE_API_TIENDA}/api/cupones`;
const getToken = () => localStorage.getItem("accessToken");

const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

// ── PÚBLICO ──────────────────────────────────────────────────────────────────

export const validarCupon = async (codigo, total) => {
  const res = await fetch(
    `${API_URL}/validar/${encodeURIComponent(codigo)}?total=${total}`,
    { headers: authHeaders() }
  );
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Cupón inválido o expirado");
  }
  return res.json();
};

// ── ADMINISTRADOR ─────────────────────────────────────────────────────────────

export const listarCupones = async () => {
  const res = await fetch(API_URL, { headers: authHeaders() });
  if (!res.ok) throw new Error("Error al obtener los cupones");
  return res.json();
};

export const obtenerCupon = async (codigo) => {
  const res = await fetch(`${API_URL}/${encodeURIComponent(codigo)}`, {
    headers: authHeaders(),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Cupón no encontrado");
  }
  return res.json();
};

export const crearCupon = async (cupon) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(cupon),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al crear el cupón");
  }
  return res.json();
};

export const actualizarCupon = async (codigo, cupon) => {
  const res = await fetch(`${API_URL}/${encodeURIComponent(codigo)}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(cupon),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al actualizar el cupón");
  }
  return res.json();
};

export const eliminarCupon = async (codigo) => {
  const res = await fetch(`${API_URL}/${encodeURIComponent(codigo)}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al eliminar el cupón");
  }
  return res.text();
};
