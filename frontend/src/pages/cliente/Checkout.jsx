import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { crearPedido } from "../../services/checkoutService";
import { validarCupon } from "../../services/cuponService";
import { useWompi } from "../../components/hooks/useWompi";
import "../../styles/Checkout.css";

const fmt = (n) => new Intl.NumberFormat("es-CO", {
  style: "currency", currency: "COP", minimumFractionDigits: 0,
}).format(n);

const METODOS = [
  { id: "CONTRA_ENTREGA", nombre: "Pago contra entrega",    desc: "Paga en efectivo cuando recibas tu pedido",          icono: "💵", badge: "disponible" },
  { id: "TRANSFERENCIA",  nombre: "Transferencia bancaria", desc: "Transfiere a nuestra cuenta y envía el comprobante", icono: "🏦", badge: "disponible" },
  { id: "WOMPI",          nombre: "Wompi",                  desc: "Paga con tarjeta débito/crédito o PSE",              icono: "⚡", badge: "disponible" },
  { id: "MERCADO_PAGO",   nombre: "Mercado Pago",           desc: "Paga con tarjeta, PSE o saldo Mercado Pago",         icono: "💳", badge: "pronto"     },
];

const COLOMBIA = {
  "Antioquia": {
    "Oriente Antioqueño": ["Abejorral","Alejandría","Argelia","Carmen de Viboral","Cocorná","Concepción","El Peñol","El Retiro","El Santuario","Granada","Guarne","Guatapé","La Ceja","La Unión","Marinilla","Nariño","Rionegro","San Carlos","San Francisco","San Luis","San Rafael","San Vicente Ferrer","Sonsón"],
    "Valle de Aburrá":    ["Medellín","Bello","Itagüí","Envigado","Sabaneta","La Estrella","Copacabana","Girardota","Barbosa","Caldas"],
    "Otros municipios":   ["Apartadó","Turbo","Caucasia","Santa Fe de Antioquia","Yarumal","Andes","Jericó","Ciudad Bolívar"],
  },
  "Cundinamarca": {
    "Sabana Centro":    ["Bogotá D.C.","Soacha","Facatativá","Zipaquirá","Chía","Mosquera","Madrid","Funza","Cajicá","Sopó","La Calera"],
    "Otros municipios": ["Fusagasugá","Girardot","Villeta","Ubaté"],
  },
  "Valle del Cauca":      { "Todos": ["Cali","Buenaventura","Palmira","Tuluá","Buga","Cartago","Jamundí","Florida"] },
  "Atlántico":            { "Todos": ["Barranquilla","Soledad","Malambo","Sabanalarga","Baranoa","Puerto Colombia"] },
  "Santander":            { "Todos": ["Bucaramanga","Floridablanca","Girón","Piedecuesta","Barrancabermeja","Socorro"] },
  "Bolívar":              { "Todos": ["Cartagena","Magangué","El Carmen de Bolívar","Mompós"] },
  "Nariño":               { "Todos": ["Pasto","Tumaco","Ipiales","La Unión"] },
  "Risaralda":            { "Todos": ["Pereira","Dosquebradas","Santa Rosa de Cabal","La Virginia"] },
  "Caldas":               { "Todos": ["Manizales","La Dorada","Chinchiná","Riosucio","Villamaría"] },
  "Quindío":              { "Todos": ["Armenia","Calarcá","Montenegro","Quimbaya"] },
  "Tolima":               { "Todos": ["Ibagué","Espinal","Melgar","Honda","Chaparral"] },
  "Huila":                { "Todos": ["Neiva","Pitalito","Garzón","La Plata"] },
  "Meta":                 { "Todos": ["Villavicencio","Acacías","Granada","Puerto López"] },
  "Cauca":                { "Todos": ["Popayán","Santander de Quilichao","Puerto Tejada"] },
  "Córdoba":              { "Todos": ["Montería","Cereté","Sahagún","Lorica","Planeta Rica"] },
  "Magdalena":            { "Todos": ["Santa Marta","Ciénaga","Fundación","El Banco"] },
  "Cesar":                { "Todos": ["Valledupar","Aguachica","Codazzi"] },
  "Norte de Santander":   { "Todos": ["Cúcuta","Ocaña","Pamplona","Villa del Rosario","Los Patios"] },
  "Boyacá":               { "Todos": ["Tunja","Duitama","Sogamoso","Chiquinquirá","Paipa"] },
  "La Guajira":           { "Todos": ["Riohacha","Maicao","Uribia","Manaure"] },
  "Casanare":             { "Todos": ["Yopal","Aguazul","Villanueva","Tauramena"] },
  "Sucre":                { "Todos": ["Sincelejo","Corozal","Sampués"] },
  "Caquetá":              { "Todos": ["Florencia","San Vicente del Caguán"] },
  "Arauca":               { "Todos": ["Arauca","Saravena","Tame"] },
  "Putumayo":             { "Todos": ["Mocoa","Puerto Asís","Orito"] },
  "Chocó":                { "Todos": ["Quibdó","Istmina"] },
  "San Andrés":           { "Todos": ["San Andrés","Providencia"] },
  "Amazonas":             { "Todos": ["Leticia","Puerto Nariño"] },
  "Guainía":              { "Todos": ["Inírida"] },
  "Guaviare":             { "Todos": ["San José del Guaviare"] },
  "Vaupés":               { "Todos": ["Mitú"] },
  "Vichada":              { "Todos": ["Puerto Carreño"] },
};

export default function Checkout() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { lineas = [], carritoId, clienteId } = state || {};
  const { iniciarPagoWompi, cargandoWompi, errorWompi } = useWompi();

  const [direccion,   setDireccion]   = useState("");
  const [departamento, setDepartamento] = useState("");
  const [subregion,   setSubregion]   = useState("");
  const [ciudad,      setCiudad]      = useState("");
  const [notas,       setNotas]       = useState("");
  const [metodoPago,  setMetodoPago]  = useState(null);
  const [cargando,    setCargando]    = useState(false);
  const [error,       setError]       = useState(null);
  const [pedidoExitoso, setPedidoExitoso] = useState(false);
  const [pedidoId,    setPedidoId]    = useState(null);
  const [codigoCupon,  setCodigoCupon]  = useState("");
  const [cupon,        setCupon]        = useState(null);
  const [cuponCargando, setCuponCargando] = useState(false);
  const [cuponError,   setCuponError]   = useState(null);

  const subtotal  = lineas.reduce((s, l) => s + l.precio * l.cantidad, 0);
  const envio     = subtotal > 150000 ? 0 : 8900;
  const descuento = cupon
    ? cupon.descuentoPorcentaje
      ? Math.round(subtotal * cupon.descuentoPorcentaje / 100)
      : (cupon.descuentoFijo ?? 0)
    : 0;
  const total = subtotal + envio - descuento;

  const aplicarCupon = async () => {
    if (!codigoCupon.trim()) return;
    setCuponCargando(true);
    setCuponError(null);
    setCupon(null);
    try {
      const data = await validarCupon(codigoCupon.trim(), subtotal);
      setCupon(data);
    } catch (err) {
      setCuponError(err.message);
    } finally {
      setCuponCargando(false);
    }
  };

  const quitarCupon = () => { setCupon(null); setCodigoCupon(""); setCuponError(null); };

  const subregiones        = departamento ? Object.keys(COLOMBIA[departamento] || {}) : [];
  const tieneSubregiones   = subregiones.length > 1 || (subregiones.length === 1 && subregiones[0] !== "Todos");
  const ciudadesDisponibles = departamento && subregion
    ? COLOMBIA[departamento][subregion] || []
    : departamento && subregiones.length === 1
      ? COLOMBIA[departamento][subregiones[0]] || []
      : [];

  const handleDepartamentoChange = (e) => {
    setDepartamento(e.target.value);
    setSubregion("");
    setCiudad("");
    const subs = Object.keys(COLOMBIA[e.target.value] || {});
    if (subs.length === 1 && subs[0] === "Todos") setSubregion("Todos");
  };

  const handleSubregionChange = (e) => { setSubregion(e.target.value); setCiudad(""); };

  const puedeConfirmar =
    direccion.trim().length > 5 &&
    departamento !== "" && ciudad !== "" &&
    metodoPago !== null &&
    METODOS.find((m) => m.id === metodoPago)?.badge === "disponible";

  const confirmar = async () => {
    if (!puedeConfirmar) return;
    setCargando(true);
    setError(null);
    try {
      const direccionCompleta = `${direccion.trim()}, ${ciudad}, ${departamento}, Colombia`;
      const resultado = await crearPedido(carritoId, clienteId, direccionCompleta, metodoPago, notas.trim());
      const match = resultado.match(/ID:\s*(\d+)/);
      const idPedido = match ? match[1] : `REF-${Date.now()}`;
      setPedidoId(idPedido);

      if (metodoPago === "WOMPI") {
        setCargando(false);
        await iniciarPagoWompi({
          referencia:       `PEDIDO${idPedido}${Date.now()}`,
          montoEnCentavos:  Math.round(total * 100),
          redirectUrl:      `${import.meta.env.VITE_APP_URL}/checkout/resultado`,
          onExito:          () => setPedidoExitoso(true),
        });
      } else {
        setPedidoExitoso(true);
      }
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  };

  if (pedidoExitoso) {
    return (
      <div className="checkout-page">
        <div className="checkout-exito">
          <div className="icono">🎉</div>
          <h2>¡Pedido confirmado!</h2>
          <p>Tu pedido {pedidoId ? `#${pedidoId}` : ""} fue creado exitosamente. Te notificaremos cuando esté en camino.</p>
          <button className="btn-volver-catalogo" onClick={() => navigate("/productos")}>Volver al catálogo</button>
        </div>
      </div>
    );
  }

  if (!carritoId || lineas.length === 0) {
    return (
      <div className="checkout-page">
        <div className="checkout-exito">
          <div className="icono">🛒</div>
          <h2>No hay productos</h2>
          <p>Tu carrito está vacío. Agrega productos antes de continuar.</p>
          <button className="btn-volver-catalogo" onClick={() => navigate("/productos")}>Ir al catálogo</button>
        </div>
      </div>
    );
  }

  const paso1        = departamento !== "" && ciudad !== "" && direccion.trim().length > 5;
  const paso2        = metodoPago !== null;
  const estasCargando = cargando || cargandoWompi;

  return (
    <div className="checkout-page">
      <div className="checkout-header">
        <button onClick={() => navigate(-1)}>← Volver</button>
        <h1>Finalizar compra</h1>
      </div>

      <div className="checkout-grid">
        {/* Columna izquierda */}
        <div>
          {/* Dirección */}
          <div className="checkout-section">
            <h2>
              📍 Dirección de envío
              {paso1 && <span className="seccion-completa">✓ Completo</span>}
            </h2>

            <div className="form-group">
              <label>País</label>
              <input className="checkout-select" type="text" value="Colombia 🇨🇴" disabled />
            </div>

            <div className="selectores-grid">
              <div className="form-group">
                <label>Departamento *</label>
                <select className="checkout-select" value={departamento} onChange={handleDepartamentoChange}>
                  <option value="">Selecciona un departamento</option>
                  {Object.keys(COLOMBIA).sort().map((dep) => (
                    <option key={dep} value={dep}>{dep}</option>
                  ))}
                </select>
              </div>

              {tieneSubregiones && (
                <div className="form-group">
                  <label>Subregión *</label>
                  <select className="checkout-select" value={subregion}
                    onChange={handleSubregionChange} disabled={!departamento}>
                    <option value="">{departamento ? "Selecciona una subregión" : "Primero elige un departamento"}</option>
                    {subregiones.map((s) => <option key={s} value={s}>{s}</option>)}
                  </select>
                </div>
              )}

              <div className="form-group">
                <label>Ciudad / Municipio *</label>
                <select className="checkout-select" value={ciudad}
                  onChange={(e) => setCiudad(e.target.value)}
                  disabled={!departamento || (tieneSubregiones && !subregion)}>
                  <option value="">
                    {!departamento ? "Primero elige un departamento"
                      : tieneSubregiones && !subregion ? "Primero elige una subregión"
                      : "Selecciona una ciudad"}
                  </option>
                  {ciudadesDisponibles.sort().map((c) => (
                    <option key={c} value={c}>{c}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="form-group">
              <label>Dirección completa *</label>
              <input type="text" placeholder="Ej: Calle 123 #45-67, Apto 301, Barrio El Centro"
                value={direccion} onChange={(e) => setDireccion(e.target.value)} />
            </div>

            <div className="form-group">
              <label>Notas del pedido (opcional)</label>
              <textarea placeholder="Ej: Dejar con el portero, llamar antes de entregar..."
                value={notas} onChange={(e) => setNotas(e.target.value)} />
            </div>
          </div>

          {/* Método de pago */}
          <div className="checkout-section">
            <h2>
              💳 Método de pago
              {paso2 && <span className="seccion-completa">✓ Seleccionado</span>}
            </h2>
            <div className="metodos-pago">
              {METODOS.map((m) => (
                <button key={m.id}
                  className={`metodo-card ${metodoPago === m.id ? "activo" : ""}`}
                  onClick={() => m.badge === "disponible" && setMetodoPago(m.id)}
                  style={{ opacity: m.badge === "pronto" ? 0.6 : 1 }}>
                  <span className="metodo-icono">{m.icono}</span>
                  <div className="metodo-info">
                    <p className="metodo-nombre">{m.nombre}</p>
                    <p className="metodo-desc">{m.desc}</p>
                  </div>
                  <span className={`metodo-badge ${m.badge}`}>
                    {m.badge === "disponible" ? "Disponible" : "Próximamente"}
                  </span>
                </button>
              ))}
            </div>

            {metodoPago === "TRANSFERENCIA" && (
              <div className="metodo-info-transferencia">
                🏦 <strong>Datos bancarios:</strong> Bancolombia · Cuenta de Ahorros · Nro. 123-456789-00 · A nombre de Olympo Store S.A.S.
                <br />Envía el comprobante a <strong>pagos@olympo.com</strong>
              </div>
            )}
            {metodoPago === "WOMPI" && (
              <div className="metodo-info-wompi">
                ⚡ Se abrirá el widget de pago de Wompi al confirmar el pedido.
              </div>
            )}
          </div>
        </div>

        {/* Resumen */}
        <div>
          <div className="checkout-section">
            <h2>🛍️ Resumen del pedido</h2>
            <div className="resumen-lineas">
              {lineas.map((l) => (
                <div key={l.varianteId} className="resumen-linea">
                  <div className="resumen-img">
                    {l.imagenGeneral
                      ? <img src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${l.imagenGeneral}`}
                          alt={l.nombre} onError={(e) => { e.target.style.display = "none"; }} />
                      : <span>📦</span>
                    }
                  </div>
                  <div className="resumen-linea-info">
                    <div className="resumen-linea-nombre">{l.nombre}</div>
                    <div className="resumen-linea-variante">
                      {[l.sabor, l.tamano].filter(Boolean).join(" · ")} × {l.cantidad}
                    </div>
                  </div>
                  <div className="resumen-linea-precio">{fmt(l.precio * l.cantidad)}</div>
                </div>
              ))}
            </div>

            <hr className="resumen-divider" />

            <div className="resumen-row"><span>Subtotal</span><span>{fmt(subtotal)}</span></div>
            <div className={`resumen-row ${envio === 0 ? "gratis" : ""}`}>
              <span>{envio === 0 ? "Envío (gratis 🎉)" : "Envío estándar"}</span>
              <span>{envio === 0 ? "Gratis" : fmt(envio)}</span>
            </div>

            {/* Cupón de descuento */}
            {!cupon ? (
              <div className="cupon-row">
                <input
                  type="text"
                  placeholder="Código de cupón"
                  value={codigoCupon}
                  onChange={(e) => { setCodigoCupon(e.target.value); setCuponError(null); }}
                  className="cupon-input"
                  onKeyDown={(e) => e.key === "Enter" && aplicarCupon()}
                />
                <button className="cupon-btn" onClick={aplicarCupon} disabled={cuponCargando}>
                  {cuponCargando ? "..." : "Aplicar"}
                </button>
                {cuponError && <p className="cupon-error">{cuponError}</p>}
              </div>
            ) : (
              <div className="resumen-row descuento">
                <span>🎟 Cupón {cupon.codigo}</span>
                <span>
                  -{cupon.descuentoPorcentaje ? `${cupon.descuentoPorcentaje}%` : fmt(cupon.descuentoFijo)}
                  <button className="cupon-quitar" onClick={quitarCupon}>✕</button>
                </span>
              </div>
            )}
            {descuento > 0 && (
              <div className="resumen-row descuento">
                <span>Ahorro total</span><span>-{fmt(descuento)}</span>
              </div>
            )}

            <div className="resumen-row total"><span>Total</span><span>{fmt(total)}</span></div>

            {(!departamento || !ciudad) && (
              <div className="checkout-advertencia">⚠️ Selecciona departamento y ciudad</div>
            )}
            {departamento && ciudad && direccion.trim().length <= 5 && (
              <div className="checkout-advertencia">⚠️ Ingresa una dirección válida</div>
            )}
            {departamento && ciudad && direccion.trim().length > 5 && !metodoPago && (
              <div className="checkout-advertencia">⚠️ Selecciona un método de pago</div>
            )}

            {(error || errorWompi) && (
              <div className="checkout-error">⚠️ {error || errorWompi}</div>
            )}

            <button className="btn-confirmar"
              disabled={!puedeConfirmar || estasCargando}
              onClick={confirmar}>
              {estasCargando
                ? "Procesando..."
                : metodoPago === "WOMPI"
                  ? `Pagar con Wompi · ${fmt(total)}`
                  : `Confirmar pedido · ${fmt(total)}`}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}