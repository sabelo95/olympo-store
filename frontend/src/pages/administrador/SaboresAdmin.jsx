import { useEffect, useState } from "react";
import { obtenerSabores, crearSabor, actualizarSabor, eliminarSabor } from "../../services/saborService";
import "../../styles/SaboresAdmin.css";

export default function SaboresAdmin() {
  const [sabores, setSabores] = useState([]);
  const [nombre, setNombre] = useState("");
  const [saborEditando, setSaborEditando] = useState(null);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => {
    cargarSabores();
  }, []);

  const cargarSabores = async () => {
    try {
      const data = await obtenerSabores();
      setSabores(data);
    } catch (error) {
      mostrarMensaje("Error al cargar sabores", "error");
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
      if (saborEditando) {
        // Actualizar sabor existente
        await actualizarSabor(saborEditando.id, { nombre });
        mostrarMensaje("Sabor actualizado exitosamente", "success");
        setSaborEditando(null);
      } else {
        // Crear nuevo sabor
        await crearSabor({ nombre });
        mostrarMensaje("Sabor creado exitosamente", "success");
      }
      setNombre("");
      cargarSabores();
    } catch (error) {
      const errorMsg = error.message || "Error al procesar el sabor";
      mostrarMensaje(errorMsg, "error");
    }
  };

  const handleEditar = (sabor) => {
    setSaborEditando(sabor);
    setNombre(sabor.nombre);
  };

  const handleCancelar = () => {
    setSaborEditando(null);
    setNombre("");
  };

  const handleEliminar = async (sabor) => {
    if (!window.confirm(`¿Estás seguro de eliminar el sabor "${sabor.nombre}"?`)) {
      return;
    }

    try {
      await eliminarSabor(sabor.id);
      mostrarMensaje("Sabor eliminado exitosamente", "success");
      cargarSabores();
    } catch (error) {
      const errorMsg = error.message || "Error al eliminar el sabor";
      mostrarMensaje(errorMsg, "error");
    }
  };

  return (
    <div className="marcas-admin-container">
      <h2>Gestión de Sabores</h2>

      {mensaje.texto && (
        <div className={`mensaje ${mensaje.tipo}`}>
          {mensaje.texto}
        </div>
      )}

      <form onSubmit={handleSubmit} className="marca-form">
        <input
          type="text"
          placeholder="Nombre del sabor (ej: Chocolate, Vainilla, Fresa)"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          className="input-marca"
        />
        <div className="form-buttons">
          <button type="submit" className="btn-primary">
            {saborEditando ? "Actualizar" : "Crear"}
          </button>
          {saborEditando && (
            <button type="button" onClick={handleCancelar} className="btn-secondary">
              Cancelar
            </button>
          )}
        </div>
      </form>

      <div className="marcas-lista">
        <h3>Sabores Registrados ({sabores.length})</h3>
        {sabores.length === 0 ? (
          <p className="sin-marcas">No hay sabores registrados</p>
        ) : (
          <ul>
            {sabores.map((s) => (
              <li key={s.id} className="marca-item">
                <span className="marca-nombre">{s.nombre}</span>
                <div className="marca-acciones">
                  <button onClick={() => handleEditar(s)} className="btn-editar">
                    ✏️ Editar
                  </button>
                  <button onClick={() => handleEliminar(s)} className="btn-eliminar">
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