import { useEffect, useState } from "react";
import {
  obtenerResumen,
  obtenerPedidosRecientes,
  obtenerTopProductos,
  obtenerVentasPorPeriodo,
} from "../../services/dashboardService";
import "../../styles/Dashboard.css";

const formatCOP = (valor) =>
  new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 }).format(valor ?? 0);

const formatFecha = (fecha) => {
  if (!fecha) return "-";
  return new Date(fecha).toLocaleDateString("es-CO", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
};

const badgeClase = (estado) => {
  const mapa = {
    PENDIENTE: "badge-PENDIENTE",
    PROCESANDO: "badge-PROCESANDO",
    ENVIADO: "badge-ENVIADO",
    COMPLETADO: "badge-COMPLETADO",
    CANCELADO: "badge-CANCELADO",
  };
  return mapa[estado?.toUpperCase()] ?? "badge-default";
};

function Dashboard() {
  const [resumen, setResumen] = useState(null);
  const [pedidos, setPedidos] = useState([]);
  const [topProductos, setTopProductos] = useState([]);
  const [loadingResumen, setLoadingResumen] = useState(true);
  const [loadingPedidos, setLoadingPedidos] = useState(true);
  const [loadingTop, setLoadingTop] = useState(true);
  const [errorResumen, setErrorResumen] = useState(null);

  // Ventas por periodo
  const hoy = new Date();
  const primerDiaMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
  const [fechaInicio, setFechaInicio] = useState(primerDiaMes.toISOString().slice(0, 10));
  const [fechaFin, setFechaFin] = useState(hoy.toISOString().slice(0, 10));
  const [ventasPeriodo, setVentasPeriodo] = useState(null);
  const [loadingPeriodo, setLoadingPeriodo] = useState(false);
  const [errorPeriodo, setErrorPeriodo] = useState(null);

  useEffect(() => {
    obtenerResumen()
      .then(setResumen)
      .catch(() => setErrorResumen("No se pudo cargar el resumen."))
      .finally(() => setLoadingResumen(false));

    obtenerPedidosRecientes(8)
      .then(setPedidos)
      .catch(() => {})
      .finally(() => setLoadingPedidos(false));

    obtenerTopProductos(5)
      .then(setTopProductos)
      .catch(() => {})
      .finally(() => setLoadingTop(false));
  }, []);

  const consultarPeriodo = async () => {
    setLoadingPeriodo(true);
    setErrorPeriodo(null);
    try {
      const inicio = `${fechaInicio}T00:00:00`;
      const fin = `${fechaFin}T23:59:59`;
      const data = await obtenerVentasPorPeriodo(inicio, fin);
      setVentasPeriodo(data);
    } catch {
      setErrorPeriodo("No se pudo obtener los datos del período.");
    } finally {
      setLoadingPeriodo(false);
    }
  };

  const maxCantidad = topProductos.length > 0 ? Math.max(...topProductos.map((p) => p.cantidadVendida ?? 0)) : 1;

  return (
    <div className="dashboard">
      <h1>Panel de Administración</h1>

      {/* KPIs */}
      <div className="kpi-grid">
        <div className="kpi-card azul">
          <span className="kpi-label">Total pedidos</span>
          <span className="kpi-value">
            {loadingResumen ? "..." : errorResumen ? "-" : (resumen?.totalPedidos ?? 0)}
          </span>
        </div>
        <div className="kpi-card verde">
          <span className="kpi-label">Ingresos totales</span>
          <span className="kpi-value">
            {loadingResumen ? "..." : errorResumen ? "-" : formatCOP(resumen?.totalIngresos)}
          </span>
        </div>
        <div className="kpi-card morado">
          <span className="kpi-label">Total clientes</span>
          <span className="kpi-value">
            {loadingResumen ? "..." : errorResumen ? "-" : (resumen?.totalClientes ?? 0)}
          </span>
        </div>
      </div>

      {/* Fila: estados + top productos */}
      <div className="dashboard-grid">
        {/* Pedidos por estado */}
        <div className="dashboard-card">
          <h2>Pedidos por estado</h2>
          {loadingResumen && <p className="loading-text">Cargando...</p>}
          {errorResumen && <p className="error-text">{errorResumen}</p>}
          {!loadingResumen && !errorResumen && resumen?.pedidosPorEstado && (
            <div className="estado-badges">
              {Object.entries(resumen.pedidosPorEstado).map(([estado, cantidad]) => (
                <div key={estado} className="estado-row">
                  <span className="estado-nombre">{estado.charAt(0) + estado.slice(1).toLowerCase()}</span>
                  <span className={`estado-badge ${badgeClase(estado)}`}>{cantidad}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Top productos */}
        <div className="dashboard-card">
          <h2>Top productos vendidos</h2>
          {loadingTop && <p className="loading-text">Cargando...</p>}
          {!loadingTop && topProductos.length === 0 && (
            <p className="loading-text">Sin datos disponibles.</p>
          )}
          {!loadingTop && topProductos.length > 0 && (
            <div className="top-productos-list">
              {topProductos.map((p, i) => (
                <div key={p.productoId ?? i} className="top-producto-row">
                  <div className="top-producto-rank">{i + 1}</div>
                  <div className="top-producto-info">
                    <div className="top-producto-nombre">{p.nombre ?? `Producto ${p.productoId}`}</div>
                    <div className="top-producto-bar-wrap">
                      <div
                        className="top-producto-bar"
                        style={{ width: `${((p.cantidadVendida ?? 0) / maxCantidad) * 100}%` }}
                      />
                    </div>
                  </div>
                  <div className="top-producto-cantidad">{p.cantidadVendida ?? 0}</div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Pedidos recientes */}
        <div className="dashboard-card full-width">
          <h2>Pedidos recientes</h2>
          {loadingPedidos && <p className="loading-text">Cargando...</p>}
          {!loadingPedidos && pedidos.length === 0 && (
            <p className="loading-text">No hay pedidos registrados.</p>
          )}
          {!loadingPedidos && pedidos.length > 0 && (
            <div className="pedidos-table-wrap">
              <table className="pedidos-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Cliente</th>
                    <th>Fecha</th>
                    <th>Total</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {pedidos.map((p) => (
                    <tr key={p.id}>
                      <td>#{p.id}</td>
                      <td>{p.clienteId ?? "-"}</td>
                      <td>{formatFecha(p.fechaPedido)}</td>
                      <td>{formatCOP(p.total)}</td>
                      <td>
                        <span className={`estado-badge ${badgeClase(p.estado)}`}>
                          {p.estado}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Ventas por período */}
        <div className="dashboard-card full-width">
          <h2>Ventas por período</h2>
          <div className="periodo-form">
            <label>
              Desde
              <input
                type="date"
                value={fechaInicio}
                onChange={(e) => setFechaInicio(e.target.value)}
              />
            </label>
            <label>
              Hasta
              <input
                type="date"
                value={fechaFin}
                onChange={(e) => setFechaFin(e.target.value)}
              />
            </label>
            <button className="btn-consultar" onClick={consultarPeriodo} disabled={loadingPeriodo}>
              {loadingPeriodo ? "Consultando..." : "Consultar"}
            </button>
          </div>
          {errorPeriodo && <p className="error-text">{errorPeriodo}</p>}
          {ventasPeriodo && (
            <div className="periodo-result">
              <div className="periodo-kpi">
                <div className="kpi-label">Pedidos en el período</div>
                <div className="kpi-value">{ventasPeriodo.totalPedidos}</div>
              </div>
              <div className="periodo-kpi">
                <div className="kpi-label">Ingresos en el período</div>
                <div className="kpi-value">{formatCOP(ventasPeriodo.totalIngresos)}</div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
