const API_URL = `${import.meta.env.VITE_API_TIENDA}/api/pedidos`;
const getToken = () => localStorage.getItem("accessToken");

const headers = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export const obtenerTodosPedidos = async () => {
  const res = await fetch(API_URL, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener pedidos");
  return res.json();
};

export const obtenerPedidoPorId = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { headers: headers() });
  if (!res.ok) throw new Error("Pedido no encontrado");
  return res.json();
};

export const obtenerPedidosPorCliente = async (clienteId) => {
  const res = await fetch(`${API_URL}/cliente/${clienteId}`, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener pedidos del cliente");
  return res.json();
};

export const actualizarEstadoPedido = async (id, estado) => {
  const res = await fetch(`${API_URL}/actualizar-estado?id=${id}&estado=${estado}`, {
    method: "PUT",
    headers: headers(),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al actualizar estado");
  }
  return res.text();
};
