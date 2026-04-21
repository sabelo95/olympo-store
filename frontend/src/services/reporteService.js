const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/reporte`;

export const generarReporteInventarioBajo = async (limite) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/inventario-bajo?limite=${limite}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    throw new Error("Error al generar el reporte");
  }

  // Retornar el blob del PDF
  return res.blob();
};