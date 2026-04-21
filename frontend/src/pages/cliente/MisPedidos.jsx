import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { obtenerPedidosPorCliente } from "../../services/pedidoService";
import { useToast } from "../../context/ToastContext";
import "../../styles/MisPedidos.css";

const fmt = (n) =>
  new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", minimumFractionDigits: 0 }).format(n);

const ESTADOS_LABEL = {
  PENDIENTE: "Pendiente",
  CONFIRMADO: "Confirmado",
  PREPARANDO: "Preparando",
  ENVIADO: "Enviado",
  ENTREGADO: "Entregado",
  CANCELADO: "Cancelado",
};

export default function MisPedidos() {
  const navigate = useNavigate();
  const toast = useToast();
  const clienteId = localStorage.getItem("clienteId");

  const [pedidos, setPedidos] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    if (!clienteId) { navigate("/login"); return; }
    cargarPedidos();
  }, []);

  const cargarPedidos = async () => {
    try {
      const data = await obtenerPedidosPorCliente(clienteId);
      // Más recientes primero
      setPedidos([...data].sort((a, b) => b.id - a.id));
    } catch {
      toast("No se pudieron cargar los pedidos", "error");
    } finally {
      setCargando(false);
    }
  };

  const formatearFecha = (fecha) => {
    if (!fecha) return "";
    return new Date(fecha).toLocaleDateString("es-CO", {
      day: "2-digit", month: "short", year: "numeric",
    });
  };

  if (cargando) return <div className="mispedidos-page"><p className="mispedidos-loading">Cargando pedidos...</p></div>;

  return (
    <div className="mispedidos-page">
      <div className="mispedidos-container">
        <div className="mispedidos-header">
          <button onClick={() => navigate("/perfil")}>← Volver</button>
          <h1>Mis pedidos</h1>
        </div>

        {pedidos.length === 0 ? (
          <div className="sin-pedidos">
            <p>Aún no tienes pedidos realizados.</p>
            <button className="btn-ir-tienda" onClick={() => navigate("/productos")}>
              Ir al catálogo
            </button>
          </div>
        ) : (
          pedidos.map((pedido) => (
            <div key={pedido.id} className="pedido-card">
              <div className="pedido-card-header">
                <div className="pedido-id-fecha">
                  <span className="pedido-id">Pedido #{pedido.id}</span>
                  <span className="pedido-fecha">{formatearFecha(pedido.fechaPedido ?? pedido.fecha)}</span>
                </div>
                <span className={`pedido-estado estado-${pedido.estado}`}>
                  {ESTADOS_LABEL[pedido.estado] ?? pedido.estado}
                </span>
              </div>

              {pedido.productos?.length > 0 && (
                <div className="pedido-lineas">
                  {pedido.productos.map((p, i) => (
                    <div key={i} className="pedido-linea">
                      <div>
                        <div className="pedido-linea-nombre">{p.nombre ?? p.productoNombre ?? "Producto"}</div>
                        <div className="pedido-linea-detalle">
                          {[p.sabor, p.tamano].filter(Boolean).join(" · ")} × {p.cantidad}
                        </div>
                      </div>
                      <div className="pedido-linea-precio">
                        {p.precio ? fmt(p.precio * p.cantidad) : ""}
                      </div>
                    </div>
                  ))}
                </div>
              )}

              <div className="pedido-footer">
                <div className="pedido-direccion">📍 {pedido.direccionEnvio}</div>
                <div>
                  <div className="pedido-total">{pedido.total ? fmt(pedido.total) : ""}</div>
                  <div className="pedido-metodo">{pedido.metodoPago?.replace("_", " ")}</div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
