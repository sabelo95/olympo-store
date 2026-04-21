import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { obtenerProductoPorNombre, actualizarProducto } from "../../services/productoService";
import { obtenerCategorias } from "../../services/categoriaService";
import { obtenerMarcas } from "../../services/marcaService";
import "../../styles/EditarProducto.css";

const estadoInicial = {
  nombre: "", descripcion: "", precio: "", costo: "",
  cantidad: "", tamano: "", sabor: "",
  categoria: { id: "", nombre: "" },
  marca: { id: "", nombre: "" },
};

function EditarProducto() {
  const { nombre } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState(estadoInicial);
  const [categorias, setCategorias] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([
      obtenerProductoPorNombre(nombre),
      obtenerCategorias(),
      obtenerMarcas(),
    ])
      .then(([prod, cats, marcs]) => {
        setProducto(prod);
        setCategorias(cats);
        setMarcas(marcs);
      })
      .catch(() => setError("Error al cargar los datos del producto"));
  }, [nombre]);

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await actualizarProducto(nombre, {
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        cantidad: parseInt(producto.cantidad),
        precio: parseFloat(producto.precio),
        costo: parseFloat(producto.costo),
        tamano: producto.tamano,
        sabor: producto.sabor,
        categoria: { id: producto.categoria.id, nombre: producto.categoria.nombre },
        marca: { id: producto.marca.id, nombre: producto.marca.nombre },
      });
      navigate("/admin/productos");
    } catch (err) {
      setError("Error al actualizar el producto. Verifica los datos.");
    }
  };

  return (
    <div className="editar-producto">
      <h1>Editar Producto</h1>

      {error && <div className="alerta-error">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nombre *</label>
          <input name="nombre" value={producto.nombre}
            onChange={handleChange} required />
        </div>

        <div className="form-group">
          <label>Descripción *</label>
          <textarea name="descripcion" value={producto.descripcion}
            onChange={handleChange} required />
        </div>

        <div className="grid-2">
          <div className="form-group">
            <label>Tamaño *</label>
            <input name="tamano" value={producto.tamano}
              onChange={handleChange} required placeholder="Ej: 500g, 1kg" />
          </div>
          <div className="form-group">
            <label>Sabor *</label>
            <input name="sabor" value={producto.sabor}
              onChange={handleChange} required placeholder="Ej: Chocolate, Vainilla" />
          </div>
        </div>

        <div className="grid-3">
          <div className="form-group">
            <label>Precio *</label>
            <input name="precio" type="number" step="0.01"
              value={producto.precio} onChange={handleChange} required min="0.01" />
          </div>
          <div className="form-group">
            <label>Costo *</label>
            <input name="costo" type="number" step="0.01"
              value={producto.costo} onChange={handleChange} required min="0.01" />
          </div>
          <div className="form-group">
            <label>Cantidad *</label>
            <input name="cantidad" type="number"
              value={producto.cantidad} onChange={handleChange} required min="0" />
          </div>
        </div>

        <div className="form-group">
          <label>Categoría *</label>
          <select value={producto.categoria?.id || ""}
            onChange={handleCategoriaChange} required>
            <option value="">Seleccione una categoría</option>
            {categorias.map((c) => (
              <option key={c.id} value={c.id}>{c.nombre}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Marca *</label>
          <select value={producto.marca?.id || ""}
            onChange={handleMarcaChange} required>
            <option value="">Seleccione una marca</option>
            {marcas.map((m) => (
              <option key={m.id} value={m.id}>{m.nombre}</option>
            ))}
          </select>
        </div>

        <div className="form-botones">
          <button type="submit" className="btn-guardar">Guardar cambios</button>
          <button type="button" className="btn-cancelar"
            onClick={() => navigate("/admin/productos")}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}

export default EditarProducto;