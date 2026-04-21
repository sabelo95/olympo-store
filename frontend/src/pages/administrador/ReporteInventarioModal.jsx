import { useState } from "react";
import { generarReporteInventarioBajo } from "../../services/reporteService";
import "../../styles/ReporteInventarioModal.css";

export default function ReporteInventarioModal({ onClose }) {
  const [limite, setLimite] = useState(10);
  const [cargando, setCargando] = useState(false);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  const handleGenerar = async () => {
    if (limite < 1) {
      setMensaje({ texto: "El límite debe ser mayor a 0", tipo: "error" });
      return;
    }

    setCargando(true);
    setMensaje({ texto: "", tipo: "" });

    try {
      const pdfBlob = await generarReporteInventarioBajo(limite);

      // Crear URL temporal para descargar el PDF
      const url = window.URL.createObjectURL(pdfBlob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `reporte_inventario_bajo_${limite}.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);

      setMensaje({
        texto: "Reporte generado y enviado a tu correo ✅",
        tipo: "success",
      });

      setTimeout(() => {
        onClose();
      }, 2000);
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
          <h3>📊 Generar Reporte de Inventario Bajo</h3>
          <button className="modal-close" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="modal-body">
          <p className="modal-description">
            Este reporte mostrará todos los productos con stock menor o igual al
            límite especificado.
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

          {mensaje.texto && (
            <div className={`mensaje-modal ${mensaje.tipo}`}>
              {mensaje.texto}
            </div>
          )}
        </div>

        <div className="modal-footer">
          <button
            className="btn-cancelar"
            onClick={onClose}
            disabled={cargando}
          >
            Cancelar
          </button>
          <button
            className="btn-generar"
            onClick={handleGenerar}
            disabled={cargando}
          >
            {cargando ? "Generando..." : "📥 Generar Reporte"}
          </button>
        </div>
      </div>
    </div>
  );
}