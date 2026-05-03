import { useEffect, useState, useMemo, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { obtenerProductos, eliminarProducto, actualizarProductoConImagenesPorId } from "../../services/productoService";
import ReporteInventarioModal from "./ReporteInventarioModal";
import "../../styles/ProductosAdmin.css";

const CAMPOS_FILTRO = ["nombre", "tamano", "sabor", "precio", "costo", "cantidad", "categoria", "marca"];
const PRODUCTOS_POR_PAGINA = 10;

const filtroInicial = {
  nombre: "", tamano: "", sabor: "", precio: "",
  costo: "", cantidad: "", categoria: "", marca: "",
};

function ProductosAdmin() {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
  const [productosFiltrados, setProductosFiltrados] = useState([]);
  const [filtros, setFiltros] = useState(filtroInicial);
  const [paginaActual, setPaginaActual] = useState(1);
  const [mostrarReporte, setMostrarReporte] = useState(false);
  const [subiendoImagen, setSubiendoImagen] = useState(null); // id del producto subiendo
  const fileInputRef = useRef(null);
  const productoActivoRef = useRef(null);

  useEffect(() => { cargarProductos(); }, []);
  useEffect(() => { aplicarFiltros(); }, [productos, filtros]);

  const cargarProductos = async () => {
    const data = await obtenerProductos();
    setProductos(data);
    setProductosFiltrados(data);
  };

  const aplicarFiltros = () => {
    let res = [...productos];
    if (filtros.nombre)    res = res.filter(p => p.nombre?.toLowerCase().includes(filtros.nombre.toLowerCase()));
    if (filtros.tamano)    res = res.filter(p => p.tamano?.toLowerCase().includes(filtros.tamano.toLowerCase()));
    if (filtros.sabor)     res = res.filter(p => p.sabor?.toLowerCase().includes(filtros.sabor.toLowerCase()));
    if (filtros.precio)    res = res.filter(p => p.precio?.toString().includes(filtros.precio));
    if (filtros.costo)     res = res.filter(p => p.costo?.toString().includes(filtros.costo));
    if (filtros.cantidad)  res = res.filter(p => p.cantidad?.toString().includes(filtros.cantidad));
    if (filtros.categoria) res = res.filter(p => p.categoria?.nombre?.toLowerCase().includes(filtros.categoria.toLowerCase()));
    if (filtros.marca)     res = res.filter(p => p.marca?.nombre?.toLowerCase().includes(filtros.marca.toLowerCase()));
    res.sort((a, b) => a.nombre.localeCompare(b.nombre, "es"));
    setProductosFiltrados(res);
    setPaginaActual(1);
  };

  const handleFiltroChange = (e) => {
    const { name, value } = e.target;
    setFiltros((prev) => ({ ...prev, [name]: value }));
  };

  const limpiarFiltros = () => setFiltros(filtroInicial);

  const handleClickImagen = (p) => {
    productoActivoRef.current = p;
    fileInputRef.current.value = "";
    fileInputRef.current.click();
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    const p = productoActivoRef.current;
    if (!file || !p) return;
    setSubiendoImagen(p.id);
    try {
      const actualizado = await actualizarProductoConImagenesPorId(
        p.id,
        {
          nombre: p.nombre, descripcion: p.descripcion || p.nombre,
          cantidad: p.cantidad, precio: p.precio, costo: p.costo,
          tamano: p.tamano || "", sabor: p.sabor || "",
          categoria: { id: p.categoria.id, nombre: p.categoria.nombre },
          marca: { id: p.marca.id, nombre: p.marca.nombre },
        },
        file, null,
      );
      setProductos((prev) =>
        prev.map((prod) => prod.id === p.id ? { ...prod, imagenGeneral: actualizado.imagenGeneral } : prod)
      );
    } catch (err) {
      alert("Error al subir imagen: " + err.message);
    } finally {
      setSubiendoImagen(null);
    }
  };

  const handleEliminar = async (id, nombre) => {
    if (!confirm(`¿Eliminar ${nombre}?`)) return;
    try {
      await eliminarProducto(id);
      cargarProductos();
    } catch (err) {
      alert(err.message);
    }
  };

  const resumenSabores = useMemo(() => {
    const mapa = new Map();
    productos.forEach((p) => {
      const key = p.sabor || "Sin sabor";
      if (!mapa.has(key)) mapa.set(key, { variantes: 0, stock: 0 });
      const e = mapa.get(key);
      e.variantes += 1;
      e.stock += p.cantidad || 0;
    });
    return [...mapa.entries()]
      .sort((a, b) => b[1].stock - a[1].stock)
      .map(([nombre, data]) => ({ nombre, ...data }));
  }, [productos]);

  const indiceUltimo   = paginaActual * PRODUCTOS_POR_PAGINA;
  const indicePrimero  = indiceUltimo - PRODUCTOS_POR_PAGINA;
  const productosActuales = productosFiltrados.slice(indicePrimero, indiceUltimo);
  const totalPaginas   = Math.ceil(productosFiltrados.length / PRODUCTOS_POR_PAGINA);

  return (
    <div className="productos-admin">
      {/* Header */}
      <div className="productos-admin-header">
        <h1>Gestión de Productos</h1>
        <div className="header-botones">
          <button className="btn-reporte" onClick={() => setMostrarReporte(true)}>
            📊 Generar Reporte
          </button>
          <button className="btn-agregar" onClick={() => navigate("/admin/productos/agregar")}>
            + Agregar Producto
          </button>
        </div>
      </div>

      {/* Resumen por sabor */}
      {resumenSabores.length > 0 && (
        <div className="resumen-sabores">
          <h3 className="resumen-sabores-titulo">Stock por sabor</h3>
          <div className="resumen-sabores-grid">
            {resumenSabores.map(({ nombre, variantes, stock }) => (
              <div key={nombre} className="sabor-card">
                <span className="sabor-nombre">{nombre}</span>
                <span className="sabor-stock">{stock.toLocaleString()} uds.</span>
                <span className="sabor-variantes">{variantes} {variantes === 1 ? "variante" : "variantes"}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Info */}
      <div className="tabla-info">
        <span>
          Mostrando {productosActuales.length} de {productosFiltrados.length} productos
          {productosFiltrados.length !== productos.length && ` (${productos.length} total)`}
        </span>
        <button className="btn-limpiar" onClick={limpiarFiltros}>Limpiar Filtros</button>
      </div>

      {/* Tabla */}
      <div className="tabla-wrapper">
        <table className="tabla-productos">
          <thead>
            <tr>
              <th>Imagen</th>
              {["Nombre","Tamaño","Sabor","Precio","Costo","Cantidad","Categoría","Marca","Acciones"].map((h) => (
                <th key={h}>{h}</th>
              ))}
            </tr>
            <tr className="fila-filtros">
              <th />
              {CAMPOS_FILTRO.map((campo) => (
                <th key={campo}>
                  <input className="filtro-input" type="text" name={campo}
                    placeholder="Filtrar..." value={filtros[campo]}
                    onChange={handleFiltroChange} />
                </th>
              ))}
              <th />
            </tr>
          </thead>
          <tbody>
            {productosActuales.map((p) => (
              <tr key={p.id}>
                <td className="td-imagen-producto">
                  <div
                    className={`imagen-upload-trigger ${!p.imagenGeneral ? "sin-imagen" : ""}`}
                    onClick={() => handleClickImagen(p)}
                    title="Clic para subir imagen"
                  >
                    {subiendoImagen === p.id ? (
                      <span className="imagen-subiendo">...</span>
                    ) : p.imagenGeneral ? (
                      <img
                        src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${p.imagenGeneral}`}
                        alt={p.nombre}
                        className="producto-thumb"
                        onError={(e) => { e.target.style.display = "none"; }}
                      />
                    ) : (
                      <span className="badge-sin-imagen">+ imagen</span>
                    )}
                  </div>
                </td>
                <td>{p.nombre}</td>
                <td>{p.tamano || "N/A"}</td>
                <td>{p.sabor || "N/A"}</td>
                <td>${p.precio?.toLocaleString() || "0"}</td>
                <td>${p.costo?.toLocaleString() || "0"}</td>
                <td style={{ textAlign: "center" }}>{p.cantidad}</td>
                <td>{p.categoria?.nombre || "N/A"}</td>
                <td>{p.marca?.nombre || "N/A"}</td>
                <td>
                  <button className="btn-editar"
                    onClick={() => navigate(`/admin/productos/editar/id/${p.id}`)}>
                    Editar
                  </button>
                  <button className="btn-eliminar" onClick={() => handleEliminar(p.id, p.nombre)}>
                    Eliminar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Paginación */}
      {totalPaginas > 1 && (
        <div className="paginacion">
          <button className="btn-pagina-nav"
            onClick={() => setPaginaActual((p) => p - 1)}
            disabled={paginaActual === 1}>
            Anterior
          </button>
          {[...Array(totalPaginas)].map((_, i) => (
            <button key={i} onClick={() => setPaginaActual(i + 1)}
              className={`btn-pagina-num ${paginaActual === i + 1 ? "activo" : ""}`}>
              {i + 1}
            </button>
          ))}
          <button className="btn-pagina-nav"
            onClick={() => setPaginaActual((p) => p + 1)}
            disabled={paginaActual === totalPaginas}>
            Siguiente
          </button>
          <span className="paginacion-info">Página {paginaActual} de {totalPaginas}</span>
        </div>
      )}

      {mostrarReporte && <ReporteInventarioModal onClose={() => setMostrarReporte(false)} />}

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        style={{ display: "none" }}
        onChange={handleFileChange}
      />
    </div>
  );
}

export default ProductosAdmin;