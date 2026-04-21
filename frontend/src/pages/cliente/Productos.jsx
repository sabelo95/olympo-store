import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { obtenerProductos } from "../../services/productoService";
import {
  obtenerCarritoActivo,
  crearCarrito,
  actualizarCarrito,
  construirPayload,
} from "../../services/carritoService";
import { getRol, getDecodedToken, isTokenValid, clearSession } from "../../services/tokenService";
import CarritoPanel from "./CarritoPanel";
import logo from "../../assets/LOGO.jpeg";
import "../../styles/ProductosCliente.css";

const agruparPorNombre = (productos) => {
  const mapa = new Map();
  productos.forEach((p) => {
    if (!mapa.has(p.nombre)) {
      mapa.set(p.nombre, {
        nombre: p.nombre,
        descripcion: p.descripcion,
        categoria: p.categoria,
        marca: p.marca,
        imagenGeneral: p.imagenGeneral,
        variantes: [],
        precioMin: p.precio,
        precioMax: p.precio,
        cantidadTotal: 0,
      });
    }
    const grupo = mapa.get(p.nombre);
    grupo.variantes.push({
      id: p.id || `${p.nombre}-${p.sabor}-${p.tamano}`,
      productoId: p.id,
      sabor: p.sabor || null,
      tamano: p.tamano || null,
      precio: p.precio,
      cantidad: p.cantidad,
      imagenGeneral: p.imagenGeneral,
      imagenNutricional: p.imagenNutricional,
    });
    grupo.cantidadTotal += p.cantidad;
    grupo.precioMin = Math.min(grupo.precioMin, p.precio);
    grupo.precioMax = Math.max(grupo.precioMax, p.precio);
  });
  return Array.from(mapa.values());
};

const Badge = ({ text, bg, color }) => (
  <span className="badge" style={{ backgroundColor: bg, color }}>{text}</span>
);

function ProductosCliente() {
  const navigate = useNavigate();
  const [grupos, setGrupos] = useState([]);
  const [filtrados, setFiltrados] = useState([]);
  // Carrito persistido en localStorage para sobrevivir navegación (ej: ir a login y volver)
  const [carrito, setCarrito] = useState(() => {
    try {
      const guardado = localStorage.getItem("olympo_carrito");
      return guardado ? JSON.parse(guardado) : {};
    } catch { return {}; }
  });
  const [carritoIdGuardado, setCarritoIdGuardado] = useState(null);
  const [guardando, setGuardando] = useState(false);
  const [grupoDetalle, setGrupoDetalle] = useState(null);
  const [varianteActiva, setVarianteActiva] = useState(null);
  const [saborSel, setSaborSel] = useState(null);
  const [tamanoSel, setTamanoSel] = useState(null);
  const [imagenZoom, setImagenZoom] = useState(null);
  const [feedbackId, setFeedbackId] = useState(null);
  const [carritoAbierto, setCarritoAbierto] = useState(false);
  const [cantidadTemporal, setCantidadTemporal] = useState(0);
  // null si es invitado (sin sesión activa)
  const clienteId = Number(localStorage.getItem("clienteId")) || null;
  const estaLogueado = !!getDecodedToken();
  const esAdmin = getRol() === "ADMINISTRADOR";
  const debounceRef = useRef(null);
  const lineasCarritoRef = useRef([]);
  const [filtros, setFiltros] = useState({
    busqueda: "", categoria: "", marca: "", precioMin: "", precioMax: "",
  });
  const [paginaActual, setPaginaActual] = useState(1);
  const POR_PAGINA = 12;

  useEffect(() => {
    // Si hay datos de sesión pero el token expiró, limpiar para evitar 401
    if (localStorage.getItem("accessToken") && !isTokenValid()) {
      clearSession();
      window.location.reload();
    }
    cargarProductos();
  }, []);
  useEffect(() => { aplicarFiltros(); }, [grupos, filtros]);

  // Persistir carrito en localStorage en cada cambio
  useEffect(() => {
    localStorage.setItem("olympo_carrito", JSON.stringify(carrito));
  }, [carrito]);

  useEffect(() => {
    // Solo sincronizar con servidor si el usuario está logueado con clienteId
    if (!clienteId) return;
    if (Object.keys(carrito).length === 0) return;
    if (debounceRef.current) clearTimeout(debounceRef.current);

    debounceRef.current = setTimeout(async () => {
      const lineas = lineasCarritoRef.current;
      if (lineas.length === 0) return;
      setGuardando(true);
      try {
        if (carritoIdGuardado) {
          await actualizarCarrito(carritoIdGuardado, clienteId, construirPayload(clienteId, lineas, true));
        } else {
          const res = await crearCarrito(construirPayload(clienteId, lineas, true));
          setCarritoIdGuardado(res.id);
        }
      } catch (e) {
        console.error("Error guardando carrito:", e);
      } finally {
        setGuardando(false);
      }
    }, 2000);

    return () => { if (debounceRef.current) clearTimeout(debounceRef.current); };
  }, [carrito]);

  const restaurarCarrito = async (gruposData) => {
    if (!clienteId) return;
    try {
      const carritoGuardado = await obtenerCarritoActivo(clienteId);
      if (!carritoGuardado || !carritoGuardado.productos?.length) return;

      // Construir carrito desde el servidor
      const carritoServidor = {};
      carritoGuardado.productos.forEach((p) => {
        for (const g of gruposData) {
          const v = g.variantes.find((v) => v.productoId === p.productoId);
          if (v) { carritoServidor[String(v.id)] = p.cantidad; break; }
        }
      });

      // Fusionar: lo local tiene prioridad (el usuario lo acaba de agregar)
      setCarrito((prev) => ({ ...carritoServidor, ...prev }));
      setCarritoIdGuardado(carritoGuardado.id);
    } catch (_) {}
  };

  const cargarProductos = async () => {
    try {
      const data = await obtenerProductos();
      const agrupados = agruparPorNombre(data.filter((p) => p.cantidad > 0));
      setGrupos(agrupados);
      setFiltrados(agrupados);
      await restaurarCarrito(agrupados);
    } catch (err) {
      console.error("Error cargando productos:", err);
    }
  };

  const aplicarFiltros = () => {
    let res = [...grupos];
    if (filtros.busqueda)
      res = res.filter((g) =>
        g.nombre?.toLowerCase().includes(filtros.busqueda.toLowerCase()) ||
        g.descripcion?.toLowerCase().includes(filtros.busqueda.toLowerCase())
      );
    if (filtros.categoria)
      res = res.filter((g) => g.categoria?.nombre?.toLowerCase().includes(filtros.categoria.toLowerCase()));
    if (filtros.marca)
      res = res.filter((g) => g.marca?.nombre?.toLowerCase().includes(filtros.marca.toLowerCase()));
    if (filtros.precioMin)
      res = res.filter((g) => g.precioMin >= parseFloat(filtros.precioMin));
    if (filtros.precioMax)
      res = res.filter((g) => g.precioMin <= parseFloat(filtros.precioMax));
    setFiltrados(res);
    setPaginaActual(1);
  };

  const handleFiltro = (e) => setFiltros({ ...filtros, [e.target.name]: e.target.value });
  const limpiarFiltros = () => setFiltros({ busqueda: "", categoria: "", marca: "", precioMin: "", precioMax: "" });

  const cantidadEnCarrito = (varianteId) => carrito[String(varianteId)] || 0;

  const agregar = (variante) => {
    const id = String(variante.id);
    const actual = cantidadEnCarrito(id);
    if (actual < variante.cantidad)
      setCarrito((prev) => ({ ...prev, [id]: actual + 1 }));
    else alert("No hay más stock disponible");
  };

  const agregarDesdetarjeta = (grupo, e) => {
    e.stopPropagation();
    const variante = grupo.variantes[0];
    if (!variante) return;
    agregar(variante);
    setFeedbackId(grupo.nombre);
    setTimeout(() => setFeedbackId(null), 1400);
  };

  const actualizarCarritoDesdePanel = (varianteId, cantidad) => {
    setCarrito((prev) => {
      const nuevo = { ...prev };
      if (cantidad === 0) delete nuevo[String(varianteId)];
      else nuevo[String(varianteId)] = cantidad;
      return nuevo;
    });
  };

  const totalItemsCarrito = Object.values(carrito).reduce((s, c) => s + c, 0);

  const lineasCarrito = Object.entries(carrito)
    .filter(([, cantidad]) => cantidad > 0)
    .map(([varianteId, cantidad]) => {
      let variante = null, grupo = null;
      for (const g of grupos) {
        const v = g.variantes.find((v) => String(v.id) === varianteId);
        if (v) { variante = v; grupo = g; break; }
      }
      if (!variante || !grupo) return null;
      return {
        varianteId, productoId: variante.productoId,
        nombre: grupo.nombre, sabor: variante.sabor,
        tamano: variante.tamano, precio: variante.precio,
        imagenGeneral: variante.imagenGeneral, cantidad,
      };
    })
    .filter(Boolean);

  lineasCarritoRef.current = lineasCarrito;

  const saboresUnicos = (g) => [...new Set(g.variantes.map((v) => v.sabor).filter(Boolean))];
  const tamanosUnicos = (g) => [...new Set(g.variantes.map((v) => v.tamano).filter(Boolean))];
  const resolverVariante = (g, sabor, tamano) =>
    g.variantes.find((v) => (!sabor || v.sabor === sabor) && (!tamano || v.tamano === tamano)) || null;

  const abrirDetalle = (grupo) => {
    const sabor = saboresUnicos(grupo)[0] || null;
    const tamano = tamanosUnicos(grupo)[0] || null;
    setSaborSel(sabor); setTamanoSel(tamano); setGrupoDetalle(grupo);
    setVarianteActiva(resolverVariante(grupo, sabor, tamano) || grupo.variantes[0]);
    setCantidadTemporal(0);
  };

  const cerrarDetalle = () => {
    setGrupoDetalle(null); setVarianteActiva(null);
    setSaborSel(null); setTamanoSel(null);
    setImagenZoom(null); setCantidadTemporal(0);
  };

  const elegirSabor = (sabor) => {
    setSaborSel(sabor);
    setVarianteActiva(resolverVariante(grupoDetalle, sabor, tamanoSel));
    setCantidadTemporal(0);
  };

  const elegirTamano = (tamano) => {
    setTamanoSel(tamano);
    setVarianteActiva(resolverVariante(grupoDetalle, saborSel, tamano));
    setCantidadTemporal(0);
  };

  const totalPaginas = Math.ceil(filtrados.length / POR_PAGINA);
  const paginados = filtrados.slice((paginaActual - 1) * POR_PAGINA, paginaActual * POR_PAGINA);
  const irPagina = (n) => { setPaginaActual(n); window.scrollTo({ top: 0, behavior: "smooth" }); };

  const categorias = [...new Set(grupos.map((g) => g.categoria?.nombre).filter(Boolean))];
  const marcas     = [...new Set(grupos.map((g) => g.marca?.nombre).filter(Boolean))];
  const labelPrecio = (g) =>
    g.precioMin === g.precioMax
      ? `$${g.precioMin?.toLocaleString()}`
      : `Desde $${g.precioMin?.toLocaleString()}`;

  const cerrarSesion = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("clienteId");
    window.location.reload();
  };

  return (
    <div className="catalogo">
      {/* Navbar */}
      <nav className="tienda-navbar">
        <div className="tienda-navbar-brand">
          <img src={logo} alt="Olympo Store" className="tienda-navbar-logo" />
          <span className="tienda-navbar-nombre">Olympo Store</span>
        </div>
        <div className="tienda-navbar-acciones">
          {esAdmin ? (
            <>
              <a href="/dashboard" className="btn-navbar-panel">Panel Admin</a>
              <button className="btn-navbar-logout" onClick={cerrarSesion}>Cerrar sesión</button>
            </>
          ) : estaLogueado ? (
            <>
              <button className="btn-navbar-login" onClick={() => navigate("/perfil")}>Mi perfil</button>
              <button className="btn-navbar-login" onClick={() => navigate("/mis-pedidos")}>Mis pedidos</button>
              <button className="btn-navbar-logout" onClick={cerrarSesion}>Cerrar sesión</button>
            </>
          ) : (
            <>
              <button className="btn-navbar-login" onClick={() => navigate("/login")}>Iniciar sesión</button>
              <button className="btn-navbar-registro" onClick={() => navigate("/registro")}>Registrarse</button>
            </>
          )}
        </div>
      </nav>

      <h1>Catálogo de Productos</h1>

      {/* Destacados */}
      {grupos.length > 0 && (
        <div className="destacados-seccion">
          <h2 className="destacados-titulo">⭐ Más populares</h2>
          <div className="destacados-grid">
            {[...grupos]
              .sort((a, b) => b.cantidadTotal - a.cantidadTotal)
              .slice(0, 4)
              .map((grupo) => (
                <div
                  key={grupo.nombre}
                  className="destacado-card"
                  onClick={() => navigate(`/producto/${encodeURIComponent(grupo.nombre)}`)}
                >
                  {grupo.imagenGeneral ? (
                    <img
                      src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${grupo.imagenGeneral}`}
                      alt={grupo.nombre}
                      onError={(e) => { e.target.style.display = "none"; }}
                    />
                  ) : (
                    <span className="destacado-placeholder">📦</span>
                  )}
                  <div className="destacado-info">
                    <p className="destacado-nombre">{grupo.nombre}</p>
                    <p className="destacado-precio">Desde ${grupo.precioMin?.toLocaleString()}</p>
                  </div>
                </div>
              ))}
          </div>
        </div>
      )}

      {/* Filtros */}
      <div className="filtros">
        <div className="filtros-grid">
          <div>
            <label>Buscar</label>
            <input type="text" name="busqueda" placeholder="Buscar productos..."
              value={filtros.busqueda} onChange={handleFiltro} />
          </div>
          <div>
            <label>Categoría</label>
            <select name="categoria" value={filtros.categoria} onChange={handleFiltro}>
              <option value="">Todas</option>
              {categorias.map((c) => <option key={c} value={c}>{c}</option>)}
            </select>
          </div>
          <div>
            <label>Marca</label>
            <select name="marca" value={filtros.marca} onChange={handleFiltro}>
              <option value="">Todas</option>
              {marcas.map((m) => <option key={m} value={m}>{m}</option>)}
            </select>
          </div>
          <div>
            <label>Precio Mín.</label>
            <input type="number" name="precioMin" placeholder="$0"
              value={filtros.precioMin} onChange={handleFiltro} />
          </div>
          <div>
            <label>Precio Máx.</label>
            <input type="number" name="precioMax" placeholder="$999999"
              value={filtros.precioMax} onChange={handleFiltro} />
          </div>
          <div className="filtros-align-bottom">
            <button className="btn-limpiar" onClick={limpiarFiltros}>Limpiar Filtros</button>
          </div>
        </div>
        <div className="filtros-footer">
          Mostrando {filtrados.length} producto{filtrados.length !== 1 ? "s" : ""}
        </div>
      </div>

      {/* Grid */}
      <div className="productos-grid">
        {paginados.map((grupo) => {
          const sabores  = saboresUnicos(grupo);
          const tamanos  = tamanosUnicos(grupo);
          const enCarrito = grupo.variantes.reduce((s, v) => s + cantidadEnCarrito(v.id), 0);
          const esFeedback = feedbackId === grupo.nombre;

          return (
            <div key={grupo.nombre} className="tarjeta">
              <div className="tarjeta-imagen">
                {grupo.imagenGeneral
                  ? <img src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${grupo.imagenGeneral}`}
                      alt={grupo.nombre} onError={(e) => { e.target.style.display = "none"; }} />
                  : <span className="emoji-placeholder">📦</span>
                }
                {grupo.variantes.length > 1 && (
                  <div className="badge-variantes">{grupo.variantes.length} variantes</div>
                )}
                {enCarrito > 0 && (
                  <div className="badge-carrito">🛒 {enCarrito} en carrito</div>
                )}
              </div>

              <div className="tarjeta-body">
                <h3
                  className="tarjeta-nombre tarjeta-nombre-link"
                  onClick={(e) => { e.stopPropagation(); navigate(`/producto/${encodeURIComponent(grupo.nombre)}`); }}
                >
                  {grupo.nombre}
                </h3>
                <div className="tarjeta-meta">
                  {grupo.categoria?.nombre || "Sin categoría"} · {grupo.marca?.nombre || "Sin marca"}
                </div>
                {(sabores.length > 0 || tamanos.length > 0) && (
                  <div className="tarjeta-badges">
                    {sabores.slice(0, 3).map((s) => <Badge key={s} text={s} bg="#fff3cd" color="#856404" />)}
                    {sabores.length > 3 && <Badge text={`+${sabores.length - 3} más`} bg="#fff3cd" color="#856404" />}
                    {tamanos.slice(0, 3).map((t) => <Badge key={t} text={t} bg="#d1ecf1" color="#0c5460" />)}
                    {tamanos.length > 3 && <Badge text={`+${tamanos.length - 3} más`} bg="#d1ecf1" color="#0c5460" />}
                  </div>
                )}
                <div className="tarjeta-precio">{labelPrecio(grupo)}</div>
                <div className={grupo.cantidadTotal > 10 ? "tarjeta-stock-ok" : "tarjeta-stock-low"}>
                  {grupo.cantidadTotal > 10
                    ? `Stock: ${grupo.cantidadTotal} uds.`
                    : `¡Solo ${grupo.cantidadTotal} unidades!`}
                </div>
                <div className="tarjeta-botones">
                  <button className={`btn-agregar ${esFeedback ? "feedback" : ""}`}
                    onClick={(e) => agregarDesdetarjeta(grupo, e)}>
                    {esFeedback ? "✓ Añadido" : "🛒 Agregar"}
                  </button>
                  <button className="btn-detalle" onClick={() => abrirDetalle(grupo)}>
                    Ver detalles
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {filtrados.length === 0 && (
        <div className="sin-resultados">
          <div className="icono">🔍</div>
          <h3>No se encontraron productos</h3>
          <p>Intenta ajustar los filtros de búsqueda</p>
        </div>
      )}

      {/* Paginación */}
      {totalPaginas > 1 && (
        <div className="paginacion">
          <button className={`btn-pagina-nav ${paginaActual === 1 ? "inactivo" : "activo"}`}
            onClick={() => irPagina(paginaActual - 1)} disabled={paginaActual === 1}>
            ← Anterior
          </button>
          {[...Array(totalPaginas)].map((_, i) => {
            const show = i === 0 || i === totalPaginas - 1 || Math.abs(i + 1 - paginaActual) <= 1;
            if (!show) { if (i === 1 || i === totalPaginas - 2) return <span key={i}>…</span>; return null; }
            return (
              <button key={i} onClick={() => irPagina(i + 1)}
                className={`btn-pagina ${paginaActual === i + 1 ? "activo" : ""}`}>
                {i + 1}
              </button>
            );
          })}
          <button className={`btn-pagina-nav ${paginaActual === totalPaginas ? "inactivo" : "activo"}`}
            onClick={() => irPagina(paginaActual + 1)} disabled={paginaActual === totalPaginas}>
            Siguiente →
          </button>
          <span className="paginacion-info">Página {paginaActual} de {totalPaginas}</span>
        </div>
      )}

      {/* Modal detalle */}
      {grupoDetalle && (
        <div className="modal-overlay" onClick={cerrarDetalle}>
          <div className="modal-contenido" onClick={(e) => e.stopPropagation()}>
            <button className="modal-cerrar" onClick={cerrarDetalle}>×</button>
            <div className="modal-grid">
              <div>
                <div className="modal-imagen-principal"
                  style={{ cursor: varianteActiva?.imagenGeneral ? "zoom-in" : "default" }}
                  onClick={() => varianteActiva?.imagenGeneral &&
                    setImagenZoom(`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${varianteActiva.imagenGeneral}`)}>
                  {varianteActiva?.imagenGeneral
                    ? <><img src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/generales/${varianteActiva.imagenGeneral}`}
                          alt={grupoDetalle.nombre} onError={(e) => { e.target.style.display = "none"; }} />
                        <div className="zoom-hint">🔍 Click para ampliar</div></>
                    : <span className="modal-placeholder">📦</span>
                  }
                </div>
                {varianteActiva?.imagenNutricional && (
                  <div>
                    <h4 className="modal-nutricional-label">Información Nutricional</h4>
                    <div className="modal-nutricional"
                      onClick={() => setImagenZoom(`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/nutricionales/${varianteActiva.imagenNutricional}`)}>
                      <img src={`${import.meta.env.VITE_API_ARCHIVOS}/uploads/productos/nutricionales/${varianteActiva.imagenNutricional}`}
                        alt="Info nutricional" onError={(e) => { e.target.style.display = "none"; }} />
                      <div className="zoom-hint">🔍 Click para ampliar</div>
                    </div>
                  </div>
                )}
              </div>

              <div>
                <h2 className="modal-nombre">{grupoDetalle.nombre}</h2>
                <div className="modal-meta">
                  {grupoDetalle.categoria?.nombre || "Sin categoría"} · {grupoDetalle.marca?.nombre || "Sin marca"}
                </div>
                {grupoDetalle.descripcion && <p className="modal-desc">{grupoDetalle.descripcion}</p>}

                {saboresUnicos(grupoDetalle).length > 0 && (
                  <div className="modal-seccion">
                    <div className="modal-seccion-label">🍬 Sabor</div>
                    <div className="modal-variantes">
                      {saboresUnicos(grupoDetalle).map((s) => (
                        <button key={s}
                          className={`btn-variante-sabor ${saborSel === s ? "activo" : ""}`}
                          onClick={() => elegirSabor(s)}>
                          {s}
                        </button>
                      ))}
                    </div>
                  </div>
                )}

                {tamanosUnicos(grupoDetalle).length > 0 && (
                  <div className="modal-seccion">
                    <div className="modal-seccion-label">📦 Tamaño / Presentación</div>
                    <div className="modal-variantes">
                      {tamanosUnicos(grupoDetalle).map((t) => (
                        <button key={t}
                          className={`btn-variante-tamano ${tamanoSel === t ? "activo" : ""}`}
                          onClick={() => elegirTamano(t)}>
                          {t}
                        </button>
                      ))}
                    </div>
                  </div>
                )}

                {varianteActiva ? (
                  <>
                    <div className="modal-precio">${varianteActiva.precio?.toLocaleString()}</div>
                    <div className={varianteActiva.cantidad > 10 ? "modal-stock-ok" : "modal-stock-low"}>
                      {varianteActiva.cantidad > 10
                        ? `Stock disponible: ${varianteActiva.cantidad} unidades`
                        : `¡Últimas ${varianteActiva.cantidad} unidades!`}
                    </div>

                    <div className="cantidad-label">Cantidad:</div>
                    <div className="cantidad-ctrl">
                      <button className="btn-cantidad restar"
                        onClick={() => setCantidadTemporal((p) => Math.max(0, p - 1))}
                        disabled={cantidadTemporal === 0}>−</button>
                      <span className="cantidad-valor">{cantidadTemporal}</span>
                      <button className="btn-cantidad sumar"
                        onClick={() => setCantidadTemporal((p) => Math.min(varianteActiva.cantidad, p + 1))}
                        disabled={cantidadTemporal >= varianteActiva.cantidad}>+</button>
                    </div>

                    <button className="btn-agregar-carrito"
                      disabled={cantidadTemporal === 0}
                      onClick={() => {
                        if (cantidadTemporal === 0) return;
                        setCarrito((prev) => ({
                          ...prev,
                          [String(varianteActiva.id)]: (prev[String(varianteActiva.id)] || 0) + cantidadTemporal,
                        }));
                        cerrarDetalle();
                        setCarritoAbierto(true);
                      }}>
                      🛒 Agregar al carrito
                    </button>

                    {cantidadTemporal > 0 && (
                      <div className="modal-subtotal">
                        {cantidadTemporal} ud. — Subtotal: ${(varianteActiva.precio * cantidadTemporal).toLocaleString()}
                      </div>
                    )}
                  </>
                ) : (
                  <div className="modal-no-variante">⚠️ Combinación no disponible. Prueba otra opción.</div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Zoom */}
      {imagenZoom && (
        <div className="zoom-overlay" onClick={() => setImagenZoom(null)}>
          <button className="zoom-cerrar" onClick={() => setImagenZoom(null)}>×</button>
          <img className="zoom-img" src={imagenZoom} alt="Ampliada"
            onClick={(e) => e.stopPropagation()} />
          <div className="zoom-info">Click en cualquier lugar para cerrar</div>
        </div>
      )}

      {/* Botón flotante */}
      <button className="btn-flotante" onClick={() => setCarritoAbierto(true)}>
        🛒
        {guardando && <span className="btn-flotante-guardando" title="Guardando..." />}
        {totalItemsCarrito > 0 && !guardando && (
          <span className="btn-flotante-badge">
            {totalItemsCarrito > 99 ? "99+" : totalItemsCarrito}
          </span>
        )}
      </button>

      {/* Panel carrito */}
      {carritoAbierto && (
        <CarritoPanel
          clienteId={clienteId}
          items={lineasCarrito}
          carritoIdInicial={carritoIdGuardado}
          onClose={() => setCarritoAbierto(false)}
          onCambiarCarrito={actualizarCarritoDesdePanel}
          onVaciar={() => {
            setCarrito({});
            setCarritoIdGuardado(null);
            setCarritoAbierto(false);
            localStorage.removeItem("olympo_carrito");
          }}
        />
      )}
    </div>
  );
}

export default ProductosCliente;