import { useEffect, useState, useCallback } from "react";
import {
  obtenerTodosPedidos,
  obtenerPedidoPorId,
  actualizarEstadoPedido,
} from "../../services/pedidoService";
import "../../styles/Ventas.css";

const ESTADOS = ["CREADO", "EN_PROCESO", "COMPLETADO", "CANCELADO"];

const formatCOP = (valor) =>
  new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0,
  }).format(valor ?? 0);

const formatFecha = (fecha) => {
  if (!fecha) return "-";
  return new Date(fecha).toLocaleDateString("es-CO", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const badgeClase = (estado) => {
  const mapa = {
    CREADO:     "badge-CREADO",
    EN_PROCESO: "badge-EN_PROCESO",
    COMPLETADO: "badge-COMPLETADO",
    CANCELADO:  "badge-CANCELADO",
  };
  return mapa[estado?.toUpperCase()] ?? "badge-default";
};

// ── Modal detalle ────────────────────────────────────────────────────────────
function ModalDetalle({ pedidoId, onClose }) {
  const [pedido, setPedido] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    obtenerPedidoPorId(pedidoId)
      .then(setPedido)
      .finally(() => setLoading(false));
  }, [pedidoId]);

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-detalle" onClick={(e) => e.stopPropagation()}>
        <h2>
          Pedido #{pedidoId}
          <button className="modal-close" onClick={onClose}>✕</button>
        </h2>

        {loading && <p style={{ color: "#888", textAlign: "center" }}>Cargando...</p>}

        {!loading && pedido && (
          <>
            <div className="modal-field">
              <span className="modal-field-label">Cliente ID</span>
              <span className="modal-field-value">{pedido.clienteId ?? "-"}</span>
            </div>
            <div className="modal-field">
              <span className="modal-field-label">Fecha</span>
              <span className="modal-field-value">{formatFecha(pedido.fechaPedido)}</span>
            </div>
            <div className="modal-field">
              <span className="modal-field-label">Estado</span>
              <span className={`estado-badge ${badgeClase(pedido.estado)}`}>{pedido.estado}</span>
            </div>
            <div className="modal-field">
              <span className="modal-field-label">Dirección</span>
              <span className="modal-field-value">{pedido.direccionEnvio ?? "-"}</span>
            </div>
            <div className="modal-field">
              <span className="modal-field-label">Método de pago</span>
              <span className="modal-field-value">{pedido.metodoPago ?? "-"}</span>
            </div>
            <div className="modal-field">
              <span className="modal-field-label">Total</span>
              <span className="modal-field-value">{formatCOP(pedido.total)}</span>
            </div>

            {pedido.items?.length > 0 && (
              <>
                <div className="modal-items-title">Productos</div>
                {pedido.items.map((item, i) => (
                  <div key={i} className="modal-item-row">
                    <span>{item.nombreProducto ?? `Producto ${item.productoId}`}</span>
                    <span>x{item.cantidad} — {formatCOP(item.precioUnitario)}</span>
                  </div>
                ))}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

// ── Fila de la tabla ─────────────────────────────────────────────────────────
function FilaPedido({ pedido, onVerDetalle, onEstadoCambiado }) {
  const [estadoLocal, setEstadoLocal] = useState(pedido.estado ?? "CREADO");
  const [guardando, setGuardando] = useState(false);
  const estadoOriginal = pedido.estado ?? "CREADO";
  const cambio = estadoLocal !== estadoOriginal;

  const guardar = async () => {
    setGuardando(true);
    try {
      await actualizarEstadoPedido(pedido.id, estadoLocal);
      onEstadoCambiado(pedido.id, estadoLocal, true);
    } catch (err) {
      onEstadoCambiado(pedido.id, estadoLocal, false, err.message);
      setEstadoLocal(estadoOriginal);
    } finally {
      setGuardando(false);
    }
  };

  return (
    <tr>
      <td>#{pedido.id}</td>
      <td>{pedido.clienteId ?? "-"}</td>
      <td>{formatFecha(pedido.fechaPedido)}</td>
      <td>{formatCOP(pedido.total)}</td>
      <td>
        <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
          <select
            className="estado-select"
            value={estadoLocal}
            onChange={(e) => setEstadoLocal(e.target.value)}
            disabled={guardando}
          >
            {ESTADOS.map((e) => (
              <option key={e} value={e}>{e.replace("_", " ")}</option>
            ))}
          </select>
          {cambio && (
            <button className="btn-guardar-estado" onClick={guardar} disabled={guardando}>
              {guardando ? "..." : "Guardar"}
            </button>
          )}
          {!cambio && (
            <span className={`estado-badge ${badgeClase(estadoLocal)}`}>{estadoLocal.replace("_", " ")}</span>
          )}
        </div>
      </td>
      <td>
        <button className="btn-ver-detalle" onClick={() => onVerDetalle(pedido.id)}>
          Ver
        </button>
      </td>
    </tr>
  );
}

// ── Página principal ──────────────────────────────────────────────────────────
function Ventas() {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [busqueda, setBusqueda] = useState("");
  const [filtroEstado, setFiltroEstado] = useState("TODOS");
  const [modalId, setModalId] = useState(null);
  const [toast, setToast] = useState(null);

  useEffect(() => {
    obtenerTodosPedidos()
      .then(setPedidos)
      .catch(() => setError("No se pudieron cargar los pedidos."))
      .finally(() => setLoading(false));
  }, []);

  const mostrarToast = (msg, tipo = "ok") => {
    setToast({ msg, tipo });
    setTimeout(() => setToast(null), 3000);
  };

  const handleEstadoCambiado = useCallback((id, nuevoEstado, ok, errMsg) => {
    if (ok) {
      setPedidos((prev) =>
        prev.map((p) => (p.id === id ? { ...p, estado: nuevoEstado } : p))
      );
      mostrarToast(`Pedido #${id} actualizado a ${nuevoEstado.replace("_", " ")}`);
    } else {
      mostrarToast(errMsg ?? "Error al actualizar", "error");
    }
  }, []);

  const pedidosFiltrados = pedidos.filter((p) => {
    const coincideEstado = filtroEstado === "TODOS" || p.estado === filtroEstado;
    const coincideBusqueda =
      busqueda === "" ||
      String(p.id).includes(busqueda) ||
      String(p.clienteId ?? "").toLowerCase().includes(busqueda.toLowerCase());
    return coincideEstado && coincideBusqueda;
  });

  return (
    <div className="ventas">
      <h1>Gestión de Ventas</h1>

      {/* Filtros */}
      <div className="ventas-filtros">
        <input
          type="text"
          placeholder="Buscar por ID o cliente..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button
          className={`filtro-btn ${filtroEstado === "TODOS" ? "activo" : ""}`}
          onClick={() => setFiltroEstado("TODOS")}
        >
          Todos
        </button>
        {ESTADOS.map((e) => (
          <button
            key={e}
            className={`filtro-btn ${filtroEstado === e ? "activo" : ""}`}
            onClick={() => setFiltroEstado(e)}
          >
            {e.replace("_", " ")}
          </button>
        ))}
      </div>

      {/* Tabla */}
      <div className="ventas-card">
        {loading && <p className="ventas-msg">Cargando pedidos...</p>}
        {error && <p className="ventas-msg ventas-error">{error}</p>}
        {!loading && !error && pedidosFiltrados.length === 0 && (
          <p className="ventas-msg">No hay pedidos con ese filtro.</p>
        )}
        {!loading && !error && pedidosFiltrados.length > 0 && (
          <div className="ventas-table-wrap">
            <table className="ventas-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Cliente</th>
                  <th>Fecha</th>
                  <th>Total</th>
                  <th>Estado</th>
                  <th>Detalle</th>
                </tr>
              </thead>
              <tbody>
                {pedidosFiltrados.map((p) => (
                  <FilaPedido
                    key={p.id}
                    pedido={p}
                    onVerDetalle={setModalId}
                    onEstadoCambiado={handleEstadoCambiado}
                  />
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal detalle */}
      {modalId && (
        <ModalDetalle pedidoId={modalId} onClose={() => setModalId(null)} />
      )}

      {/* Toast */}
      {toast && (
        <div className={`ventas-toast ${toast.tipo}`}>{toast.msg}</div>
      )}
    </div>
  );
}

export default Ventas;
