import { useEffect, useState, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  actualizarProductoConImagenesPorId, actualizarProductoPorId,
  crearProductoConImagenes, obtenerProductos,
} from "../../services/productoService";
import { obtenerCategorias } from "../../services/categoriaService";
import { obtenerMarcas } from "../../services/marcaService";
import { obtenerSaboresActivos } from "../../services/saborService";
import { obtenerTamanosActivos } from "../../services/tamanoService";
import { isTokenValid } from "../../services/tokenService";
import "../../styles/EditarProducto.css";

const IMAGEN_MAX_SIZE = 5 * 1024 * 1024;
const API_ARCHIVOS = import.meta.env.VITE_API_ARCHIVOS;

function PreviewImagen({ preview, tipo, label, onChange, onEliminar }) {
  return (
    <div className="form-group">
      <label>{label}</label>
      {!preview ? (
        <>
          <input
            type="file" accept="image/*" id={`imagen-${tipo}`}
            style={{ display: "none" }}
            onChange={(e) => onChange(e, tipo)}
          />
          <label htmlFor={`imagen-${tipo}`} className={`btn-subir-imagen ${tipo}`}>
            {tipo === "general" ? "📷 Imagen General" : "🏷️ Etiqueta Nutricional"}
          </label>
        </>
      ) : (
        <div className="preview-wrapper">
          <img src={preview} alt={`Preview ${tipo}`} />
          <button type="button" className="btn-eliminar-imagen" onClick={() => onEliminar(tipo)}>×</button>
        </div>
      )}
    </div>
  );
}

const estadoInicial = {
  nombre: "", descripcion: "", precio: "", costo: "",
  cantidad: "", tamano: "", sabor: "",
  categoria: { id: "", nombre: "" },
  marca: { id: "", nombre: "" },
};

function EditarProducto() {
  const { nombre: nombreParam, id: idParam } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState(estadoInicial);
  const [categorias, setCategorias] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [sabores, setSabores] = useState([]);
  const [tamanos, setTamanos] = useState([]);
  const [error, setError] = useState("");
  const [cargando, setCargando] = useState(false);

  const [imagenGeneralFile, setImagenGeneralFile] = useState(null);
  const [imagenNutricionalFile, setImagenNutricionalFile] = useState(null);
  const [previewGeneral, setPreviewGeneral] = useState(null);
  const [previewNutricional, setPreviewNutricional] = useState(null);
  const [eliminarGeneral, setEliminarGeneral] = useState(false);
  const [eliminarNutricional, setEliminarNutricional] = useState(false);

  // Variants state
  const [todasVariantes, setTodasVariantes] = useState([]);
  const [cantidadesVariantes, setCantidadesVariantes] = useState({});
  const [guardandoVariante, setGuardandoVariante] = useState(null);
  const [mostrarFormVariante, setMostrarFormVariante] = useState(false);
  const [nuevaVariante, setNuevaVariante] = useState({ sabor: "", cantidad: 0, tamano: "" });
  const [agregandoVariante, setAgregandoVariante] = useState(false);

  useEffect(() => {
    Promise.all([
      obtenerProductos(),
      obtenerCategorias(),
      obtenerMarcas(),
      obtenerSaboresActivos(),
      obtenerTamanosActivos(),
    ])
      .then(([todos, cats, marcs, sabs, tams]) => {
        const prod = idParam
          ? todos.find((p) => String(p.id) === String(idParam))
          : todos.find((p) => p.nombre === decodeURIComponent(nombreParam));

        if (!prod) {
          setError("Producto no encontrado.");
          return;
        }

        setProducto(prod);
        setCategorias(cats);
        setMarcas(marcs);
        setSabores(sabs);
        setTamanos(tams);

        const variantes = todos.filter((p) => p.nombre === prod.nombre);
        setTodasVariantes(variantes);
        const qtys = {};
        variantes.forEach((v) => { qtys[v.id] = v.cantidad; });
        setCantidadesVariantes(qtys);

        if (prod.imagenGeneral)
          setPreviewGeneral(`${API_ARCHIVOS}/uploads/productos/generales/${prod.imagenGeneral}`);
        if (prod.imagenNutricional)
          setPreviewNutricional(`${API_ARCHIVOS}/uploads/productos/nutricionales/${prod.imagenNutricional}`);
      })
      .catch((err) => setError(`Error al cargar datos: ${err.message}`));
  }, [idParam, nombreParam]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProducto((prev) => ({ ...prev, [name]: value }));
  };

  const handleCategoriaChange = (e) => {
    const cat = categorias.find((c) => c.id === parseInt(e.target.value));
    if (cat) setProducto((prev) => ({ ...prev, categoria: { id: cat.id, nombre: cat.nombre } }));
  };

  const handleMarcaChange = (e) => {
    const marca = marcas.find((m) => m.id === parseInt(e.target.value));
    if (marca) setProducto((prev) => ({ ...prev, marca: { id: marca.id, nombre: marca.nombre } }));
  };

  const handleImagenChange = (e, tipo) => {
    const file = e.target.files[0];
    if (!file) return;
    if (!file.type.startsWith("image/")) return setError("El archivo debe ser una imagen");
    if (file.size > IMAGEN_MAX_SIZE) return setError("La imagen no debe superar 5MB");
    setError("");
    const reader = new FileReader();
    reader.onloadend = () => {
      if (tipo === "general") {
        setImagenGeneralFile(file); setPreviewGeneral(reader.result); setEliminarGeneral(false);
      } else {
        setImagenNutricionalFile(file); setPreviewNutricional(reader.result); setEliminarNutricional(false);
      }
    };
    reader.readAsDataURL(file);
  };

  const eliminarImagen = (tipo) => {
    if (tipo === "general") {
      setImagenGeneralFile(null); setPreviewGeneral(null); setEliminarGeneral(true);
    } else {
      setImagenNutricionalFile(null); setPreviewNutricional(null); setEliminarNutricional(true);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isTokenValid()) {
      navigate("/admin/login");
      return;
    }
    setError("");
    setCargando(true);
    try {
      await actualizarProductoConImagenesPorId(
        producto.id,
        {
          nombre: producto.nombre, descripcion: producto.descripcion,
          cantidad: parseInt(producto.cantidad), precio: parseFloat(producto.precio),
          costo: parseFloat(producto.costo), tamano: producto.tamano, sabor: producto.sabor,
          categoria: { id: producto.categoria.id, nombre: producto.categoria.nombre },
          marca: { id: producto.marca.id, nombre: producto.marca.nombre },
        },
        imagenGeneralFile, imagenNutricionalFile, eliminarGeneral, eliminarNutricional,
      );
      navigate("/admin/productos");
    } catch (err) {
      if (err.message?.includes("401")) {
        navigate("/admin/login");
      } else {
        setError(err.message || "Error al actualizar el producto.");
      }
    } finally {
      setCargando(false);
    }
  };

  // --- Variants handlers ---

  const guardarCantidadVariante = async (variante) => {
    setGuardandoVariante(variante.id);
    try {
      await actualizarProductoPorId(variante.id, {
        nombre: variante.nombre,
        descripcion: variante.descripcion,
        cantidad: parseInt(cantidadesVariantes[variante.id]),
        precio: variante.precio,
        costo: variante.costo,
        tamano: variante.tamano,
        sabor: variante.sabor,
        categoria: { id: variante.categoria.id, nombre: variante.categoria.nombre },
        marca: { id: variante.marca.id, nombre: variante.marca.nombre },
      });
      setTodasVariantes((prev) =>
        prev.map((v) =>
          v.id === variante.id ? { ...v, cantidad: parseInt(cantidadesVariantes[variante.id]) } : v
        )
      );
      if (variante.id === producto.id) {
        setProducto((prev) => ({ ...prev, cantidad: parseInt(cantidadesVariantes[variante.id]) }));
      }
    } catch (err) {
      if (err.message?.includes("401")) navigate("/admin/login");
      else setError(err.message || "Error al actualizar cantidad.");
    } finally {
      setGuardandoVariante(null);
    }
  };

  const saboresUsados = useMemo(
    () => new Set(todasVariantes.map((v) => v.sabor)),
    [todasVariantes]
  );

  const saboresDisponibles = sabores.filter((s) => !saboresUsados.has(s.nombre));

  const handleAgregarVariante = async () => {
    if (!nuevaVariante.sabor) return setError("Selecciona un sabor para la nueva variante.");
    const tamanoFinal = nuevaVariante.tamano || producto.tamano;
    if (!tamanoFinal) return setError("Selecciona un tamaño para la nueva variante.");
    setAgregandoVariante(true);
    setError("");
    try {
      const nuevoProd = {
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        precio: parseFloat(producto.precio),
        costo: parseFloat(producto.costo),
        tamano: tamanoFinal,
        sabor: nuevaVariante.sabor,
        cantidad: parseInt(nuevaVariante.cantidad) || 0,
        categoria: { id: producto.categoria.id, nombre: producto.categoria.nombre },
        marca: { id: producto.marca.id, nombre: producto.marca.nombre },
      };
      const created = await crearProductoConImagenes(nuevoProd, null, null);
      setTodasVariantes((prev) => [...prev, created]);
      setCantidadesVariantes((prev) => ({ ...prev, [created.id]: created.cantidad }));
      setNuevaVariante({ sabor: "", cantidad: 0, tamano: "" });
      setMostrarFormVariante(false);
    } catch (err) {
      setError(err.message || "Error al agregar variante.");
    } finally {
      setAgregandoVariante(false);
    }
  };

  const productoId = idParam ? parseInt(idParam) : producto.id;

  return (
    <div className="editar-producto">
      <h1>Editar Producto</h1>

      {error && <div className="alerta-error">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nombre *</label>
          <input name="nombre" value={producto.nombre} onChange={handleChange} required />
        </div>

        <div className="form-group">
          <label>Descripción *</label>
          <textarea name="descripcion" value={producto.descripcion} onChange={handleChange} required />
        </div>

        <div className="grid-2">
          <div className="form-group">
            <label>Tamaño *</label>
            <select name="tamano" value={producto.tamano || ""} onChange={handleChange} required>
              <option value="">Seleccione un tamaño</option>
              {tamanos.map((t) => <option key={t.id} value={t.nombre}>{t.nombre}</option>)}
              {producto.tamano && !tamanos.find(t => t.nombre === producto.tamano) && (
                <option value={producto.tamano}>{producto.tamano} (legado)</option>
              )}
            </select>
          </div>
          <div className="form-group">
            <label>Sabor *</label>
            <select name="sabor" value={producto.sabor || ""} onChange={handleChange} required>
              <option value="">Seleccione un sabor</option>
              {sabores.map((s) => <option key={s.id} value={s.nombre}>{s.nombre}</option>)}
              {producto.sabor && !sabores.find(s => s.nombre === producto.sabor) && (
                <option value={producto.sabor}>{producto.sabor} (legado)</option>
              )}
            </select>
          </div>
        </div>

        <div className="grid-3">
          <div className="form-group">
            <label>Precio *</label>
            <input name="precio" type="number" step="0.01" value={producto.precio} onChange={handleChange} required min="0.01" />
          </div>
          <div className="form-group">
            <label>Costo *</label>
            <input name="costo" type="number" step="0.01" value={producto.costo} onChange={handleChange} required min="0.01" />
          </div>
          <div className="form-group">
            <label>Stock *</label>
            <input name="cantidad" type="number" value={producto.cantidad} onChange={handleChange} required min="0" />
          </div>
        </div>

        <div className="form-group">
          <label>Categoría *</label>
          <select value={producto.categoria?.id || ""} onChange={handleCategoriaChange} required>
            <option value="">Seleccione una categoría</option>
            {categorias.map((c) => <option key={c.id} value={c.id}>{c.nombre}</option>)}
          </select>
        </div>

        <div className="form-group">
          <label>Marca *</label>
          <select value={producto.marca?.id || ""} onChange={handleMarcaChange} required>
            <option value="">Seleccione una marca</option>
            {marcas.map((m) => <option key={m.id} value={m.id}>{m.nombre}</option>)}
          </select>
        </div>

        <div className="seccion-imagenes">
          <h3>Imágenes del Producto</h3>
          <div className="grid-2">
            <PreviewImagen preview={previewGeneral} tipo="general" label="Imagen General"
              onChange={handleImagenChange} onEliminar={eliminarImagen} />
            <PreviewImagen preview={previewNutricional} tipo="nutricional" label="Etiqueta Nutricional"
              onChange={handleImagenChange} onEliminar={eliminarImagen} />
          </div>
          <p className="nota">* Haz clic en × para cambiar la imagen. Formatos: JPG, PNG. Máximo 5MB.</p>
        </div>

        <div className="form-botones">
          <button type="submit" className="btn-guardar" disabled={cargando}>
            {cargando ? "Guardando..." : "Guardar cambios"}
          </button>
          <button type="button" className="btn-cancelar" onClick={() => navigate("/admin/productos")}>
            Cancelar
          </button>
        </div>
      </form>

      {/* Variantes del producto */}
      {todasVariantes.filter(v => v.id !== productoId).length > 0 && (
        <div className="variantes-panel">
          <div className="variantes-header">
            <h3 className="variantes-titulo">Variantes de este producto</h3>
            {!mostrarFormVariante && (
              <button
                type="button"
                className="btn-nueva-variante"
                onClick={() => setMostrarFormVariante(true)}
                disabled={saboresDisponibles.length === 0}
                title={saboresDisponibles.length === 0 ? "Ya existen variantes para todos los sabores disponibles" : ""}
              >
                + Agregar variante
              </button>
            )}
          </div>

          <table className="variantes-tabla">
            <thead>
              <tr>
                <th>Sabor</th>
                <th>Tamaño</th>
                <th style={{ width: 64 }}>Imagen</th>
                <th style={{ width: 110 }}>Stock</th>
                <th style={{ width: 100 }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {todasVariantes.filter(v => v.id !== productoId).map((v) => (
                <tr key={v.id}>
                  <td>{v.sabor || "—"}</td>
                  <td>{v.tamano || "—"}</td>
                  <td className="td-variante-imagen">
                    {v.imagenGeneral
                      ? <img
                          src={`${API_ARCHIVOS}/uploads/productos/generales/${v.imagenGeneral}`}
                          alt={v.sabor || "variante"}
                          className="variante-thumb"
                          onError={(e) => { e.target.style.display = "none"; }}
                        />
                      : <span className="badge-sin-imagen">—</span>
                    }
                  </td>
                  <td>
                    <input
                      type="number" min="0"
                      className="variante-qty-input"
                      value={cantidadesVariantes[v.id] ?? v.cantidad}
                      onChange={(e) =>
                        setCantidadesVariantes((prev) => ({ ...prev, [v.id]: e.target.value }))
                      }
                    />
                  </td>
                  <td>
                    <button
                      type="button"
                      className="btn-guardar-variante"
                      onClick={() => guardarCantidadVariante(v)}
                      disabled={guardandoVariante === v.id}
                    >
                      {guardandoVariante === v.id ? "..." : "Guardar"}
                    </button>
                    <button
                      type="button"
                      className="btn-ir-variante"
                      onClick={() => navigate(`/admin/productos/editar/id/${v.id}`)}
                    >
                      Editar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {mostrarFormVariante && (
            <div className="form-nueva-variante">
              <select
                value={nuevaVariante.sabor}
                onChange={(e) => setNuevaVariante((p) => ({ ...p, sabor: e.target.value }))}
              >
                <option value="">Sabor...</option>
                {saboresDisponibles.map((s) => (
                  <option key={s.id} value={s.nombre}>{s.nombre}</option>
                ))}
              </select>
              {!producto.tamano && (
                <select
                  value={nuevaVariante.tamano}
                  onChange={(e) => setNuevaVariante((p) => ({ ...p, tamano: e.target.value }))}
                >
                  <option value="">Tamaño...</option>
                  {tamanos.map((t) => (
                    <option key={t.id} value={t.nombre}>{t.nombre}</option>
                  ))}
                </select>
              )}
              <input
                type="number" min="0" placeholder="Cantidad"
                value={nuevaVariante.cantidad}
                onChange={(e) => setNuevaVariante((p) => ({ ...p, cantidad: e.target.value }))}
                className="variante-qty-input"
              />
              <button
                type="button" className="btn-guardar-variante"
                onClick={handleAgregarVariante}
                disabled={agregandoVariante}
              >
                {agregandoVariante ? "Agregando..." : "Agregar"}
              </button>
              <button
                type="button" className="btn-cancelar-variante"
                onClick={() => { setMostrarFormVariante(false); setNuevaVariante({ sabor: "", cantidad: 0, tamano: "" }); }}
              >
                Cancelar
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default EditarProducto;
