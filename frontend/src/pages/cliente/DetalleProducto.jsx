import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { obtenerProductos } from "../../services/productoService";
import "../../styles/DetalleProducto.css";

const agruparVariantes = (productos) => {
  const grupo = {
    nombre: productos[0]?.nombre,
    descripcion: productos[0]?.descripcion,
    categoria: productos[0]?.categoria,
    marca: productos[0]?.marca,
    variantes: productos.map((p) => ({
      id: p.id,
      productoId: p.id,
      sabor: p.sabor ?? null,
      tamano: p.tamano ?? null,
      precio: p.precio,
      cantidad: p.cantidad,
      imagenGeneral: p.imagenGeneral,
      imagenNutricional: p.imagenNutricional,
    })),
  };
  return grupo;
};

export default function DetalleProducto() {
  const { nombre } = useParams();
  const navigate = useNavigate();
  const nombreDecoded = decodeURIComponent(nombre);

  const [grupo, setGrupo] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [saborSel, setSaborSel] = useState(null);
  const [tamanoSel, setTamanoSel] = useState(null);
  const [varianteActiva, setVarianteActiva] = useState(null);
  const [cantidad, setCantidad] = useState(1);
  const [imagenZoom, setImagenZoom] = useState(null);
  const [toastVisible, setToastVisible] = useState(false);

  useEffect(() => { cargar(); }, [nombre]);

  const cargar = async () => {
    setCargando(true);
    try {
      const todos = await obtenerProductos();
      const delGrupo = todos.filter(
        (p) => p.nombre?.toLowerCase() === nombreDecoded.toLowerCase() && p.cantidad > 0
      );
      if (delGrupo.length === 0) { navigate("/productos"); return; }
      const g = agruparVariantes(delGrupo);
      setGrupo(g);

      const sabores = [...new Set(g.variantes.map((v) => v.sabor).filter(Boolean))];
      const tamanos = [...new Set(g.variantes.map((v) => v.tamano).filter(Boolean))];
      const s = sabores[0] ?? null;
      const t = tamanos[0] ?? null;
      setSaborSel(s);
      setTamanoSel(t);
      setVarianteActiva(resolverVariante(g, s, t) ?? g.variantes[0]);
    } catch {
      navigate("/productos");
    } finally {
      setCargando(false);
    }
  };

  const resolverVariante = (g, sabor, tamano) =>
    g.variantes.find((v) => (!sabor || v.sabor === sabor) && (!tamano || v.tamano === tamano)) ?? null;

  const elegirSabor = (s) => {
    setSaborSel(s);
    const v = resolverVariante(grupo, s, tamanoSel);
    setVarianteActiva(v);
    setCantidad(1);
  };

  const elegirTamano = (t) => {
    setTamanoSel(t);
    const v = resolverVariante(grupo, saborSel, t);
    setVarianteActiva(v);
    setCantidad(1);
  };

  const agregarAlCarrito = () => {
    if (!varianteActiva || cantidad === 0) return;
    try {
      const carrito = JSON.parse(localStorage.getItem("olympo_carrito") ?? "{}");
      const id = String(varianteActiva.id);
      carrito[id] = (carrito[id] ?? 0) + cantidad;
      localStorage.setItem("olympo_carrito", JSON.stringify(carrito));
      setToastVisible(true);
      setTimeout(() => setToastVisible(false), 2000);
    } catch {/* ignore */}
  };

  const sabores = grupo ? [...new Set(grupo.variantes.map((v) => v.sabor).filter(Boolean))] : [];
  const tamanos = grupo ? [...new Set(grupo.variantes.map((v) => v.tamano).filter(Boolean))] : [];

  if (cargando) return <div className="detalle-page"><p className="detalle-cargando">Cargando producto...</p></div>;
  if (!grupo) return null;

  return (
    <div className="detalle-page">
      <div className="detalle-header">
        <button onClick={() => navigate("/productos")}>← Volver al catálogo</button>
        <div className="detalle-breadcrumb">
          Inicio / {grupo.categoria?.nombre} / <span>{grupo.nombre}</span>
        </div>
      </div>

      <div className="detalle-grid">
        {/* Imágenes */}
        <div>
          <div
            className="detalle-imagen-principal"
            onClick={() =>
              varianteActiva?.imagenGeneral &&
              setImagenZoom(`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${varianteActiva.imagenGeneral}`)
            }
          >
            {varianteActiva?.imagenGeneral ? (
              <img
                src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${varianteActiva.imagenGeneral}`}
                alt={grupo.nombre}
                onError={(e) => { e.target.style.display = "none"; }}
              />
            ) : (
              <div className="detalle-imagen-placeholder">📦</div>
            )}
          </div>

          {varianteActiva?.imagenNutricional && (
            <>
              <p className="detalle-nutricional-label">Información Nutricional</p>
              <div
                className="detalle-nutricional"
                onClick={() =>
                  setImagenZoom(`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/nutricionales/${varianteActiva.imagenNutricional}`)
                }
              >
                <img
                  src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/nutricionales/${varianteActiva.imagenNutricional}`}
                  alt="Info nutricional"
                  onError={(e) => { e.target.style.display = "none"; }}
                />
              </div>
            </>
          )}
        </div>

        {/* Info */}
        <div>
          <p className="detalle-categoria-marca">
            {grupo.categoria?.nombre ?? "Sin categoría"} · {grupo.marca?.nombre ?? "Sin marca"}
          </p>
          <h1 className="detalle-nombre">{grupo.nombre}</h1>
          {grupo.descripcion && <p className="detalle-descripcion">{grupo.descripcion}</p>}

          {sabores.length > 0 && (
            <div className="detalle-seccion">
              <div className="detalle-seccion-label">🍬 Sabor</div>
              <div className="detalle-variantes">
                {sabores.map((s) => (
                  <button
                    key={s}
                    className={`btn-variante ${saborSel === s ? "activo" : ""}`}
                    onClick={() => elegirSabor(s)}
                  >
                    {s}
                  </button>
                ))}
              </div>
            </div>
          )}

          {tamanos.length > 0 && (
            <div className="detalle-seccion">
              <div className="detalle-seccion-label">📦 Tamaño / Presentación</div>
              <div className="detalle-variantes">
                {tamanos.map((t) => (
                  <button
                    key={t}
                    className={`btn-variante ${tamanoSel === t ? "activo" : ""}`}
                    onClick={() => elegirTamano(t)}
                  >
                    {t}
                  </button>
                ))}
              </div>
            </div>
          )}

          {varianteActiva ? (
            <>
              <div className="detalle-precio">${varianteActiva.precio?.toLocaleString()}</div>
              <div className={varianteActiva.cantidad > 10 ? "detalle-stock-ok" : "detalle-stock-low"}>
                {varianteActiva.cantidad > 10
                  ? `✓ Stock disponible: ${varianteActiva.cantidad} unidades`
                  : `⚠ ¡Solo ${varianteActiva.cantidad} unidades!`}
              </div>

              <div className="detalle-cantidad-label">Cantidad</div>
              <div className="detalle-cantidad-ctrl">
                <button className="btn-cant" onClick={() => setCantidad((p) => Math.max(1, p - 1))} disabled={cantidad <= 1}>−</button>
                <span className="detalle-cantidad-valor">{cantidad}</span>
                <button className="btn-cant" onClick={() => setCantidad((p) => Math.min(varianteActiva.cantidad, p + 1))} disabled={cantidad >= varianteActiva.cantidad}>+</button>
              </div>

              <button className="btn-agregar-detalle" onClick={agregarAlCarrito}>
                🛒 Agregar al carrito · ${(varianteActiva.precio * cantidad).toLocaleString()}
              </button>
            </>
          ) : (
            <div className="detalle-no-variante">⚠️ Combinación no disponible. Elige otra opción.</div>
          )}

          <button className="btn-volver-catalogo-detalle" onClick={() => navigate("/productos")}>
            ← Volver al catálogo
          </button>
        </div>
      </div>

      {/* Zoom */}
      {imagenZoom && (
        <div className="detalle-zoom-overlay" onClick={() => setImagenZoom(null)}>
          <img className="detalle-zoom-img" src={imagenZoom} alt="Ampliada" onClick={(e) => e.stopPropagation()} />
        </div>
      )}

      {toastVisible && <div className="detalle-toast">✓ Producto añadido al carrito</div>}
    </div>
  );
}
