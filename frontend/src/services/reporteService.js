const API_REPORTE        = `${import.meta.env.VITE_API_TIENDA}/api/reportes`;
const API_INVENTARIO     = `${import.meta.env.VITE_API_ARCHIVOS}/reporte`;
const API_NOTIFICACIONES = `${import.meta.env.VITE_API_TIENDA}/notificaciones`;
const getToken = () => localStorage.getItem("accessToken");

// ── Inventario bajo stock ────────────────────────────────────────────────────
export const generarReporteInventarioBajo = async (limite) => {
  const res = await fetch(`${API_INVENTARIO}/inventario-bajo?limite=${limite}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
  if (!res.ok) throw new Error("Error al generar el reporte de inventario");
  return res.blob();
};

// ── Reporte de ventas por período ────────────────────────────────────────────
export const generarReporteVentas = async (fechaInicio, fechaFin) => {
  const res = await fetch(
    `${API_REPORTE}/ventas-semanal?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`,
    { headers: { Authorization: `Bearer ${getToken()}` } }
  );
  if (!res.ok) throw new Error("Error al generar el reporte de ventas");
  return res.blob();
};

// ── Enviar cualquier PDF al correo via notificaciones-service ─────────────────
export const enviarReportePorCorreo = async (pdfBlob, nombreArchivo, destinatario, mensaje) => {
  const formData = new FormData();
  formData.append("tipo", "EMAIL");
  formData.append("destinatario", destinatario);
  formData.append("mensaje", mensaje);
  formData.append("archivo", pdfBlob, nombreArchivo);

  const res = await fetch(`${API_NOTIFICACIONES}/con-adjunto`, {
    method: "POST",
    body: formData,
  });
  if (!res.ok) throw new Error("Error al enviar el reporte por correo");
};

// ── Descargar blob como archivo ───────────────────────────────────────────────
export const descargarBlob = (blob, nombreArchivo) => {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = nombreArchivo;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
};
