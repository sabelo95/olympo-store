import { useEffect, useState } from "react";
import { obtenerMarcas, crearMarca, actualizarMarca, eliminarMarca } from "../../services/marcaService";
import  "../../styles/MarcasAdmin.css";


export default function MarcasAdmin() {
  const [marcas, setMarcas] = useState([]);
  const [nombre, setNombre] = useState("");
  const [marcaEditando, setMarcaEditando] = useState(null);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => {
    cargarMarcas();
  }, []);

  const cargarMarcas = async () => {
    try {
      const data = await obtenerMarcas();
      setMarcas(data);
    } catch (error) {
      mostrarMensaje("Error al cargar marcas", "error");
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
      if (marcaEditando) {
        // Actualizar marca existente
        await actualizarMarca(marcaEditando.nombre, { nombre });
        mostrarMensaje("Marca actualizada exitosamente", "success");
        setMarcaEditando(null);
      } else {
        // Crear nueva marca
        await crearMarca({ nombre });
        mostrarMensaje("Marca creada exitosamente", "success");
      }
      setNombre("");
      cargarMarcas();
    } catch (error) {
      const errorMsg = error.response?.data || "Error al procesar la marca";
      mostrarMensaje(errorMsg, "error");
    }
  };

  const handleEditar = (marca) => {
    setMarcaEditando(marca);
    setNombre(marca.nombre);
  };

  const handleCancelar = () => {
    setMarcaEditando(null);
    setNombre("");
  };

  const handleEliminar = async (marca) => {
    if (!window.confirm(`¿Estás seguro de eliminar la marca "${marca.nombre}"?`)) {
      return;
    }

    try {
      await eliminarMarca(marca.nombre);
      mostrarMensaje("Marca eliminada exitosamente", "success");
      cargarMarcas();
    } catch (error) {
      const errorMsg = error.response?.data || "Error al eliminar la marca";
      mostrarMensaje(errorMsg, "error");
    }
  };

  return (
    <div className="marcas-admin-container">
      <h2>Gestión de Marcas</h2>

      {mensaje.texto && (
        <div className={`mensaje ${mensaje.tipo}`}>
          {mensaje.texto}
        </div>
      )}

      <form onSubmit={handleSubmit} className="marca-form">
        <input
          type="text"
          placeholder="Nombre de la marca"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          className="input-marca"
        />
        <div className="form-buttons">
          <button type="submit" className="btn-primary">
            {marcaEditando ? "Actualizar" : "Crear"}
          </button>
          {marcaEditando && (
            <button type="button" onClick={handleCancelar} className="btn-secondary">
              Cancelar
            </button>
          )}
        </div>
      </form>

      <div className="marcas-lista">
        <h3>Marcas Registradas ({marcas.length})</h3>
        {marcas.length === 0 ? (
          <p className="sin-marcas">No hay marcas registradas</p>
        ) : (
          <ul>
            {marcas.map((m) => (
              <li key={m.id} className="marca-item">
                <span className="marca-nombre">{m.nombre}</span>
                <div className="marca-acciones">
                  <button onClick={() => handleEditar(m)} className="btn-editar">
                    ✏️ Editar
                  </button>
                  <button onClick={() => handleEliminar(m)} className="btn-eliminar">
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