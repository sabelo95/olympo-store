const API_URL = `${import.meta.env.VITE_API_TIENDA}/api/clientes`;

const authHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
});

export const obtenerClientePorNit = async (nit) => {
  const res = await fetch(`${API_URL}/${nit}`, { headers: authHeaders() });
  if (res.status === 404) throw new Error("Cliente no encontrado con ese NIT");
  if (!res.ok) throw new Error("Error al obtener cliente");
  return res.json();
};

export const obtenerClientePorUsuarioId = async (usuarioId) => {
  const res = await fetch(`${API_URL}/usuario/${usuarioId}`, { headers: authHeaders() });
  if (res.status === 404) throw new Error("Cliente no encontrado para ese usuario");
  if (!res.ok) throw new Error("Error al obtener cliente");
  return res.json();
};

export const crearCliente = async (cliente) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(cliente),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al crear cliente");
  }
  return res.json();
};

export const actualizarCliente = async (nit, cliente) => {
  const res = await fetch(`${API_URL}/${nit}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(cliente),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al actualizar cliente");
  }
  return res.text();
};

export const eliminarCliente = async (nit) => {
  const res = await fetch(`${API_URL}/${nit}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al eliminar cliente");
  }
  return res.text();
};
