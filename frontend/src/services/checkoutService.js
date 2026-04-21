const API_PEDIDOS = `${import.meta.env.VITE_API_TIENDA}/api/pedidos`;
const getToken = () => localStorage.getItem("accessToken");

export const crearPedido = async (carritoId, clienteId, direccionEnvio, metodoPago, notas) => {
  const response = await fetch(`${API_PEDIDOS}/carrito/${carritoId}/cliente/${clienteId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify({ direccionEnvio, metodoPago, notas }),
  });
  if (!response.ok) {
    const err = await response.text();
    throw new Error(err || "Error al crear el pedido");
  }
  return response.text();
};