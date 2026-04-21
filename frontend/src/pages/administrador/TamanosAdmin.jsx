import { useEffect, useState } from "react";
import { obtenerTamanos, crearTamano, actualizarTamano, eliminarTamano } from "../../services/tamanoService";
import "../../styles/TamanosAdmin.css";

export default function TamanosAdmin() {
  const [tamanos, setTamanos] = useState([]);
  const [nombre, setNombre] = useState("");
  const [tamanoEditando, setTamanoEditando] = useState(null);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => {
    cargarTamanos();
  }, []);

  const cargarTamanos = async () => {
    try {
      const data = await obtenerTamanos();
      setTamanos(data);
    } catch (error) {
      mostrarMensaje("Error al cargar tamaños", "error");
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
      if (tamanoEditando) {
        // Actualizar tamaño existente
        await actualizarTamano(tamanoEditando.id, { nombre });
        mostrarMensaje("Tamaño actualizado exitosamente", "success");
        setTamanoEditando(null);
      } else {
        // Crear nuevo tamaño
        await crearTamano({ nombre });
        mostrarMensaje("Tamaño creado exitosamente", "success");
      }
      setNombre("");
      cargarTamanos();
    } catch (error) {
      const errorMsg = error.message || "Error al procesar el tamaño";
      mostrarMensaje(errorMsg, "error");
    }
  };

  const handleEditar = (tamano) => {
    setTamanoEditando(tamano);
    setNombre(tamano.nombre);
  };

  const handleCancelar = () => {
    setTamanoEditando(null);
    setNombre("");
  };

  const handleEliminar = async (tamano) => {
    if (!window.confirm(`¿Estás seguro de eliminar el tamaño "${tamano.nombre}"?`)) {
      return;
    }

    try {
      await eliminarTamano(tamano.id);
      mostrarMensaje("Tamaño eliminado exitosamente", "success");
      cargarTamanos();
    } catch (error) {
      const errorMsg = error.message || "Error al eliminar el tamaño";
      mostrarMensaje(errorMsg, "error");
    }
  };

  return (
    <div className="marcas-admin-container">
      <h2>Gestión de Tamaños</h2>

      {mensaje.texto && (
        <div className={`mensaje ${mensaje.tipo}`}>
          {mensaje.texto}
        </div>
      )}

      <form onSubmit={handleSubmit} className="marca-form">
        <input
          type="text"
          placeholder="Nombre del tamaño (ej: 2lb, 5lb, 500g)"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          className="input-marca"
        />
        <div className="form-buttons">
          <button type="submit" className="btn-primary">
            {tamanoEditando ? "Actualizar" : "Crear"}
          </button>
          {tamanoEditando && (
            <button type="button" onClick={handleCancelar} className="btn-secondary">
              Cancelar
            </button>
          )}
        </div>
      </form>

      <div className="marcas-lista">
        <h3>Tamaños Registrados ({tamanos.length})</h3>
        {tamanos.length === 0 ? (
          <p className="sin-marcas">No hay tamaños registrados</p>
        ) : (
          <ul>
            {tamanos.map((t) => (
              <li key={t.id} className="marca-item">
                <span className="marca-nombre">{t.nombre}</span>
                <div className="marca-acciones">
                  <button onClick={() => handleEditar(t)} className="btn-editar">
                    ✏️ Editar
                  </button>
                  <button onClick={() => handleEliminar(t)} className="btn-eliminar">
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