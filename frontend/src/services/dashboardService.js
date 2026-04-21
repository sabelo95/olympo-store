const API_URL = `${import.meta.env.VITE_API_TIENDA}/api/dashboard`;
const getToken = () => localStorage.getItem("accessToken");

const headers = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export const obtenerResumen = async () => {
  const res = await fetch(`${API_URL}/resumen`, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener resumen");
  return res.json();
};

export const obtenerPedidosRecientes = async (limite = 10) => {
  const res = await fetch(`${API_URL}/pedidos-recientes?limite=${limite}`, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener pedidos recientes");
  return res.json();
};

export const obtenerTopProductos = async (limite = 5) => {
  const res = await fetch(`${API_URL}/top-productos?limite=${limite}`, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener top productos");
  return res.json();
};

export const obtenerVentasPorPeriodo = async (fechaInicio, fechaFin) => {
  const res = await fetch(
    `${API_URL}/ventas-por-periodo?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`,
    { headers: headers() }
  );
  if (!res.ok) throw new Error("Error al obtener ventas por período");
  return res.json();
};
