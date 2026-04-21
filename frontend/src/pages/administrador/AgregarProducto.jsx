import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { crearProductoConImagenes } from "../../services/productoService";
import { obtenerCategorias } from "../../services/categoriaService";
import { obtenerMarcas } from "../../services/marcaService";
import "../../styles/AgregarProducto.css";

const IMAGEN_MAX_SIZE = 5 * 1024 * 1024; // 5MB

const estadoInicial = {
  nombre: "", descripcion: "", cantidad: 0,
  precio: 0, costo: 0, tamano: "", sabor: "",
  categoriaId: "", marcaId: "",
};

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
          <label htmlFor={`imagen-${tipo}`}
            className={`btn-subir-imagen ${tipo}`}>
            {tipo === "general" ? "📷 Imagen General" : "🏷️ Etiqueta Nutricional"}
          </label>
        </>
      ) : (
        <div className="preview-wrapper">
          <img src={preview} alt={`Preview ${tipo}`} />
          <button type="button" className="btn-eliminar-imagen"
            onClick={() => onEliminar(tipo)}>×</button>
        </div>
      )}
    </div>
  );
}

function AgregarProducto() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState("");
  const [formData, setFormData] = useState(estadoInicial);
  const [imagenGeneral, setImagenGeneral] = useState(null);
  const [imagenNutricional, setImagenNutricional] = useState(null);
  const [previewGeneral, setPreviewGeneral] = useState(null);
  const [previewNutricional, setPreviewNutricional] = useState(null);

  useEffect(() => {
    Promise.all([obtenerCategorias(), obtenerMarcas()])
      .then(([cats, marcas]) => { setCategorias(cats); setMarcas(marcas); })
      .catch(() => setError("Error al cargar categorías y marcas"));
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleImagenChange = (e, tipo) => {
    const file = e.target.files[0];
    if (!file) return;
    if (!file.type.startsWith("image/")) return setError("El archivo debe ser una imagen");
    if (file.size > IMAGEN_MAX_SIZE) return setError("La imagen no debe superar 5MB");

    setError("");
    const reader = new FileReader();
    reader.onloadend = () => {
      if (tipo === "general") { setImagenGeneral(file); setPreviewGeneral(reader.result); }
      else { setImagenNutricional(file); setPreviewNutricional(reader.result); }
    };
    reader.readAsDataURL(file);
  };

  const eliminarImagen = (tipo) => {
    if (tipo === "general") { setImagenGeneral(null); setPreviewGeneral(null); }
    else { setImagenNutricional(null); setPreviewNutricional(null); }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setCargando(true);
    try {
      const producto = {
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        cantidad: parseInt(formData.cantidad),
        precio: parseFloat(formData.precio),
        costo: parseFloat(formData.costo),
        tamano: formData.tamano,
        sabor: formData.sabor,
        categoria: categorias.find((c) => c.id === parseInt(formData.categoriaId)),
        marca: marcas.find((m) => m.id === parseInt(formData.marcaId)),
      };
      await crearProductoConImagenes(producto, imagenGeneral, imagenNutricional);
      navigate("/admin/productos");
    } catch (err) {
      setError(err.message || "Error al crear el producto");
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="agregar-producto">
      <h1>Agregar Nuevo Producto</h1>

      {error && <div className="alerta-error">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nombre *</label>
          <input type="text" name="nombre" value={formData.nombre}
            onChange={handleInputChange} required maxLength={100} />
        </div>

        <div className="form-group">
          <label>Descripción *</label>
          <textarea name="descripcion" value={formData.descripcion}
            onChange={handleInputChange} required maxLength={500} rows={4} />
        </div>

        <div className="grid-2">
          <div className="form-group">
            <label>Tamaño *</label>
            <input type="text" name="tamano" value={formData.tamano}
              onChange={handleInputChange} required placeholder="Ej: 500g, 1kg, 2lb" />
          </div>
          <div className="form-group">
            <label>Sabor *</label>
            <input type="text" name="sabor" value={formData.sabor}
              onChange={handleInputChange} required placeholder="Ej: Chocolate, Vainilla" />
          </div>
        </div>

        <div className="grid-3">
          <div className="form-group">
            <label>Cantidad *</label>
            <input type="number" name="cantidad" value={formData.cantidad}
              onChange={handleInputChange} required min="0" />
          </div>
          <div className="form-group">
            <label>Precio *</label>
            <input type="number" name="precio" value={formData.precio}
              onChange={handleInputChange} required min="0.01" step="0.01" />
          </div>
          <div className="form-group">
            <label>Costo *</label>
            <input type="number" name="costo" value={formData.costo}
              onChange={handleInputChange} required min="0.01" step="0.01" />
          </div>
        </div>

        <div className="grid-2">
          <div className="form-group">
            <label>Categoría *</label>
            <select name="categoriaId" value={formData.categoriaId}
              onChange={handleInputChange} required>
              <option value="">Seleccione una categoría</option>
              {categorias.map((c) => (
                <option key={c.id} value={c.id}>{c.nombre}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Marca *</label>
            <select name="marcaId" value={formData.marcaId}
              onChange={handleInputChange} required>
              <option value="">Seleccione una marca</option>
              {marcas.map((m) => (
                <option key={m.id} value={m.id}>{m.nombre}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="seccion-imagenes">
          <h3>Imágenes del Producto</h3>
          <div className="grid-2">
            <PreviewImagen preview={previewGeneral} tipo="general"
              label="Imagen General" onChange={handleImagenChange} onEliminar={eliminarImagen} />
            <PreviewImagen preview={previewNutricional} tipo="nutricional"
              label="Etiqueta Nutricional" onChange={handleImagenChange} onEliminar={eliminarImagen} />
          </div>
          <p className="nota">* Formatos: JPG, PNG, GIF. Máximo 5MB por imagen.</p>
        </div>

        <div className="form-botones">
          <button type="button" className="btn-cancelar"
            onClick={() => navigate("/admin/productos")}>
            Cancelar
          </button>
          <button type="submit" className="btn-guardar" disabled={cargando}>
            {cargando ? "Guardando..." : "Guardar Producto"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default AgregarProducto;