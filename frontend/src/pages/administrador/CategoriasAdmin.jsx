import { useEffect, useState } from "react";
import {
  obtenerCategorias,
  crearCategoria,
  actualizarCategoria,
  eliminarCategoria,
} from "../../services/categoriaService";
import  "../../styles/CategoriasAdmin.css";

export default function CategoriasAdmin() {
  const [categorias, setCategorias] = useState([]);
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [categoriaEditando, setCategoriaEditando] = useState(null);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => {
    cargarCategorias();
  }, []);

  const cargarCategorias = async () => {
    try {
      const data = await obtenerCategorias();
      setCategorias(data);
    } catch (error) {
      mostrarMensaje("Error al cargar categorías", "error");
    }
  };

  const mostrarMensaje = (texto, tipo) => {
    setMensaje({ texto, tipo });
    setTimeout(() => setMensaje({ texto: "", tipo: "" }), 3000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!nombre.trim()) {
      mostrarMensaje("El nombre no puede estar vacío", "error");
      return;
    }

    try {
      if (categoriaEditando) {
        await actualizarCategoria(categoriaEditando.id, { nombre, descripcion });
        mostrarMensaje("Categoría actualizada exitosamente", "success");
        setCategoriaEditando(null);
      } else {
        await crearCategoria({ nombre, descripcion });
        mostrarMensaje("Categoría creada exitosamente", "success");
      }
      setNombre("");
      setDescripcion("");
      cargarCategorias();
    } catch (error) {
      mostrarMensaje(error.message, "error");
    }
  };

  const handleEditar = (categoria) => {
    setCategoriaEditando(categoria);
    setNombre(categoria.nombre);
    setDescripcion(categoria.descripcion || "");
  };

  const handleCancelar = () => {
    setCategoriaEditando(null);
    setNombre("");
    setDescripcion("");
  };

  const handleEliminar = async (categoria) => {
    if (!window.confirm(`¿Eliminar la categoría "${categoria.nombre}"?`)) {
      return;
    }

    try {
      await eliminarCategoria(categoria.id);
      mostrarMensaje("Categoría eliminada exitosamente", "success");
      cargarCategorias();
    } catch (error) {
      mostrarMensaje(error.message, "error");
    }
  };

  return (
    <div className="categorias-admin-container">
      <h2>Gestión de Categorías</h2>

      {mensaje.texto && (
        <div className={`mensaje ${mensaje.tipo}`}>{mensaje.texto}</div>
      )}

      <form onSubmit={handleSubmit} className="categoria-form">
        <input
          type="text"
          placeholder="Nombre de la categoría"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          className="input-categoria"
        />
        <textarea
          placeholder="Descripción (opcional)"
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
          className="input-categoria"
          rows="3"
        />
        <div className="form-buttons">
          <button type="submit" className="btn-primary">
            {categoriaEditando ? "Actualizar" : "Crear"}
          </button>
          {categoriaEditando && (
            <button type="button" onClick={handleCancelar} className="btn-secondary">
              Cancelar
            </button>
          )}
        </div>
      </form>

      <div className="categorias-lista">
        <h3>Categorías Registradas ({categorias.length})</h3>
        {categorias.length === 0 ? (
          <p className="sin-categorias">No hay categorías registradas</p>
        ) : (
          <ul>
            {categorias.map((c) => (
              <li key={c.id} className="categoria-item">
                <div className="categoria-info">
                  <span className="categoria-nombre">{c.nombre}</span>
                  {c.descripcion && (
                    <span className="categoria-descripcion">{c.descripcion}</span>
                  )}
                </div>
                <div className="categoria-acciones">
                  <button onClick={() => handleEditar(c)} className="btn-editar">
                    ✏️ Editar
                  </button>
                  <button onClick={() => handleEliminar(c)} className="btn-eliminar">
                    🗑️ Eliminar
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}