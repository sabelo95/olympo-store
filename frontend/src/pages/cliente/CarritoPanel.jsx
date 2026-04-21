import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  crearCarrito,
  construirPayload,
  eliminarCarrito,
  fmt,
} from "../../services/carritoService";
import "../../styles/CarritoPanel.css";

export default function CarritoPanel({
  clienteId,
  items = [],
  onClose,
  onVaciar,
  onCambiarCarrito,
  carritoIdInicial = null,
}) {
  const navigate = useNavigate();
  const [lineas, setLineas] = useState([]);
  const [carritoId, setCarritoId] = useState(carritoIdInicial);
  const [cargando, setCargando] = useState(false);
  const [estado, setEstado] = useState(null);
  const [confirmando, setConfirmando] = useState(false);

  useEffect(() => {
    setLineas(items.filter((i) => i.cantidad > 0).map((i) => ({ ...i })));
  }, []);

  useEffect(() => {
    if (carritoIdInicial !== null) setCarritoId(carritoIdInicial);
  }, [carritoIdInicial]);

  const subtotal = lineas.reduce((s, l) => s + l.precio * l.cantidad, 0);
  const envio    = subtotal > 150000 ? 0 : 8900;
  const total    = subtotal + envio;
  const totalItems = lineas.reduce((s, l) => s + l.cantidad, 0);

  const cambiarCantidad = (varianteId, delta) => {
    setLineas((prev) => {
      const linea = prev.find((l) => l.varianteId === varianteId);
      const nuevaCantidad = Math.max(0, (linea?.cantidad || 0) + delta);
      onCambiarCarrito?.(varianteId, nuevaCantidad);
      return prev
        .map((l) => l.varianteId === varianteId ? { ...l, cantidad: nuevaCantidad } : l)
        .filter((l) => l.cantidad > 0);
    });
  };

  const quitarLinea = (varianteId) => {
    setLineas((prev) => prev.filter((l) => l.varianteId !== varianteId));
    onCambiarCarrito?.(varianteId, 0);
  };

  const irACheckout = async () => {
    // Si no hay sesión activa, redirigir al login
    if (!clienteId) {
      navigate("/login", { state: { from: "/productos", mensaje: "Inicia sesión para completar tu compra" } });
      return;
    }
    setCargando(true);
    setEstado(null);
    try {
      let idCarrito = carritoId;
      if (!idCarrito) {
        const res = await crearCarrito(construirPayload(clienteId, lineas, false));
        idCarrito = res.id;
        setCarritoId(idCarrito);
      }
      navigate("/checkout", { state: { lineas, carritoId: idCarrito, clienteId } });
    } catch (e) {
      setEstado({ tipo: "error", msg: `Error al procesar: ${e.message}` });
    } finally {
      setCargando(false);
    }
  };

  const vaciarCarrito = async () => {
    if (carritoId) {
      try { await eliminarCarrito(carritoId); } catch (_) {}
      setCarritoId(null);
    }
    setLineas([]);
    onVaciar?.();
  };

  return (
    <>
      <div className="carrito-overlay" onClick={onClose} />

      <aside className="carrito-panel">
        {/* Cabecera */}
        <header className="carrito-header">
          <div className="carrito-header-info">
            <span className="carrito-header-icono">🛒</span>
            <div>
              <h2>Carrito de compras</h2>
              <span className="carrito-header-count">
                {totalItems} {totalItems === 1 ? "producto" : "productos"}
              </span>
            </div>
          </div>
          <button className="carrito-btn-cerrar" onClick={onClose}>×</button>
        </header>

        {/* Lista */}
        <div className="carrito-lista">
          {lineas.length === 0 ? (
            <div className="carrito-vacio">
              <span className="icono">🛍️</span>
              <p className="titulo">Tu carrito está vacío</p>
              <p className="subtitulo">Agrega productos desde el catálogo</p>
            </div>
          ) : (
            <ul className="carrito-items">
              {lineas.map((linea) => (
                <li key={linea.varianteId} className="carrito-item">
                  <div className="carrito-item-img">
                    {linea.imagenGeneral ? (
                      <img
                        src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${linea.imagenGeneral}`}
                        alt={linea.nombre}
                        onError={(e) => { e.target.style.display = "none"; }}
                      />
                    ) : (
                      <span className="placeholder">📦</span>
                    )}
                  </div>

                  <div className="carrito-item-info">
                    <p className="carrito-item-nombre">{linea.nombre}</p>
                    {(linea.sabor || linea.tamano) && (
                      <p className="carrito-item-variante">
                        {[linea.sabor, linea.tamano].filter(Boolean).join(" · ")}
                      </p>
                    )}
                    <div className="carrito-item-ctrl">
                      <button className="btn-ctrl" onClick={() => cambiarCantidad(linea.varianteId, -1)}>−</button>
                      <span className="carrito-item-cantidad">{linea.cantidad}</span>
                      <button className="btn-ctrl" onClick={() => cambiarCantidad(linea.varianteId, +1)}>+</button>
                    </div>
                  </div>

                  <div className="carrito-item-precios">
                    <span className="carrito-item-total">{fmt(linea.precio * linea.cantidad)}</span>
                    <span className="carrito-item-unitario">{fmt(linea.precio)} c/u</span>
                    <button className="btn-quitar" onClick={() => quitarLinea(linea.varianteId)} title="Eliminar">
                      🗑️
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Footer */}
        {lineas.length > 0 && (
          <footer className="carrito-footer">
            <div className="carrito-resumen">
              <Row label="Subtotal" value={fmt(subtotal)} />
              <Row
                label={envio === 0 ? "Envío (gratis desde $150.000)" : "Envío estándar"}
                value={envio === 0 ? "Gratis 🎉" : fmt(envio)}
                gratis={envio === 0}
              />
              <hr className="carrito-resumen-divider" />
              <Row label="TOTAL" value={fmt(total)} bold />
            </div>

            {estado && (
              <div className={`carrito-alerta ${estado.tipo}`}>
                {estado.tipo === "ok" ? "✅ " : "⚠️ "}{estado.msg}
              </div>
            )}

            <div className="carrito-acciones">
              {!confirmando ? (
                <>
                  <button className="btn-confirmar-compra"
                    onClick={() => setConfirmando(true)} disabled={cargando}>
                    Confirmar compra →
                  </button>
                  <button className="btn-vaciar" onClick={vaciarCarrito} disabled={cargando}>
                    🗑️ Vaciar carrito
                  </button>
                </>
              ) : (
                <div className="carrito-confirmacion">
                  <p>¿Continuar con el pago por {fmt(total)}?</p>
                  <div className="carrito-confirmacion-botones">
                    <button className="btn-si-continuar"
                      onClick={irACheckout} disabled={cargando}>
                      {cargando ? "Procesando…" : "Sí, continuar"}
                    </button>
                    <button className="btn-cancelar" onClick={() => setConfirmando(false)}>
                      Cancelar
                    </button>
                  </div>
                </div>
              )}
            </div>
          </footer>
        )}
      </aside>
    </>
  );
}

function Row({ label, value, bold, gratis }) {
  return (
    <div className="carrito-row">
      <span className={`carrito-row-label ${bold ? "bold" : ""}`}>{label}</span>
      <span className={`carrito-row-value ${bold ? "bold" : ""} ${gratis ? "gratis" : ""}`}>
        {value}
      </span>
    </div>
  );
}