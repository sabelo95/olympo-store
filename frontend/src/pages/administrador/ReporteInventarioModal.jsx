import { useState } from "react";
import {
  generarReporteInventarioBajo,
  enviarReportePorCorreo,
  descargarBlob,
} from "../../services/reporteService";
import { getDecodedToken } from "../../services/tokenService";
import "../../styles/ReporteInventarioModal.css";

export default function ReporteInventarioModal({ onClose }) {
  const [limite, setLimite] = useState(10);
  const [cargando, setCargando] = useState(false);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  const correoAdmin = getDecodedToken()?.sub ?? "";
  const nombreArchivo = `reporte_inventario_bajo_${limite}.pdf`;

  const handleDescargar = async () => {
    if (limite < 1) {
      setMensaje({ texto: "El límite debe ser mayor a 0", tipo: "error" });
      return;
    }
    setCargando(true);
    setMensaje({ texto: "", tipo: "" });
    try {
      const blob = await generarReporteInventarioBajo(limite);
      descargarBlob(blob, nombreArchivo);
      setMensaje({ texto: "PDF descargado correctamente.", tipo: "success" });
    } catch (error) {
      setMensaje({ texto: error.message, tipo: "error" });
    } finally {
      setCargando(false);
    }
  };

  const handleEnviar = async () => {
    if (limite < 1) {
      setMensaje({ texto: "El límite debe ser mayor a 0", tipo: "error" });
      return;
    }
    setCargando(true);
    setMensaje({ texto: "", tipo: "" });
    try {
      const blob = await generarReporteInventarioBajo(limite);
      await enviarReportePorCorreo(
        blob,
        nombreArchivo,
        correoAdmin,
        `Reporte de inventario bajo: productos con stock ≤ ${limite} unidades.`
      );
      setMensaje({ texto: `Reporte enviado a ${correoAdmin} ✅`, tipo: "success" });
      setTimeout(onClose, 2000);
    } catch (error) {
      setMensaje({ texto: error.message, tipo: "error" });
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Reporte de Inventario Bajo</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>

        <div className="modal-body">
          <p className="modal-description">
            Productos con stock menor o igual al límite especificado.
          </p>

          <div className="form-group">
            <label htmlFor="limite">Límite de stock:</label>
            <input
              id="limite"
              type="number"
              min="1"
              value={limite}
              onChange={(e) => setLimite(Number(e.target.value))}
              className="input-limite"
              disabled={cargando}
            />
            <small>Productos con stock ≤ {limite} unidades</small>
          </div>

          {correoAdmin && (
            <p style={{ fontSize: "0.82rem", color: "#888", margin: "8px 0 0" }}>
              Correo destino: <strong>{correoAdmin}</strong>
            </p>
          )}

          {mensaje.texto && (
            <div className={`mensaje-modal ${mensaje.tipo}`}>{mensaje.texto}</div>
          )}
        </div>

        <div className="modal-footer">
          <button className="btn-cancelar" onClick={onClose} disabled={cargando}>
            Cancelar
          </button>
          <button className="btn-generar" onClick={handleDescargar} disabled={cargando}>
            {cargando ? "Generando..." : "Descargar PDF"}
          </button>
          <button className="btn-generar" onClick={handleEnviar} disabled={cargando || !correoAdmin}
            style={{ background: "#2b6cb0" }}>
            {cargando ? "Enviando..." : "Enviar al correo"}
          </button>
        </div>
      </div>
    </div>
  );
}
