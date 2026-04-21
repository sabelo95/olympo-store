import { useEffect, useState } from "react";
import {
  obtenerUsuarios,
  toggleActivoUsuario,
  eliminarUsuario,
} from "../../services/usuarioService";
import "../../styles/UsuariosAdmin.css";

export default function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [filtrados, setFiltrados] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [busqueda, setBusqueda] = useState("");
  const [filtroRol, setFiltroRol] = useState("");
  const [filtroActivo, setFiltroActivo] = useState("");
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => { cargar(); }, []);
  useEffect(() => { filtrar(); }, [usuarios, busqueda, filtroRol, filtroActivo]);

  const mostrarMensaje = (texto, tipo) => {
    setMensaje({ texto, tipo });
    setTimeout(() => setMensaje({ texto: "", tipo: "" }), 3500);
  };

  const cargar = async () => {
    setCargando(true);
    try {
      const data = await obtenerUsuarios();
      setUsuarios(data);
    } catch {
      mostrarMensaje("Error al cargar usuarios. Verifica que el endpoint /auth/usuarios exista en el backend.", "error");
    } finally {
      setCargando(false);
    }
  };

  const filtrar = () => {
    let res = [...usuarios];
    if (filtroRol) res = res.filter((u) => u.rol === filtroRol);
    if (filtroActivo !== "") res = res.filter((u) => String(u.activo) === filtroActivo);
    if (busqueda.trim()) {
      const q = busqueda.toLowerCase();
      res = res.filter(
        (u) =>
          u.nombre?.toLowerCase().includes(q) ||
          u.correo?.toLowerCase().includes(q) ||
          String(u.id).includes(q)
      );
    }
    setFiltrados(res);
  };

  const handleToggleActivo = async (usuario) => {
    const accion = usuario.activo ? "desactivar" : "activar";
    if (!window.confirm(`¿${accion.charAt(0).toUpperCase() + accion.slice(1)} a "${usuario.nombre}"?`)) return;

    try {
      const actualizado = await toggleActivoUsuario(usuario);
      setUsuarios((prev) =>
        prev.map((u) => (u.id === usuario.id ? { ...u, activo: actualizado.activo } : u))
      );
      mostrarMensaje(`Usuario "${usuario.nombre}" ${accion}do exitosamente`, "success");
    } catch (err) {
      mostrarMensaje(err.message, "error");
    }
  };

  const handleEliminar = async (usuario) => {
    if (!window.confirm(`¿Eliminar permanentemente a "${usuario.nombre}"? Esta acción no se puede deshacer.`)) return;
    try {
      await eliminarUsuario(usuario.id);
      setUsuarios((prev) => prev.filter((u) => u.id !== usuario.id));
      mostrarMensaje(`Usuario "${usuario.nombre}" eliminado`, "success");
    } catch (err) {
      mostrarMensaje(err.message, "error");
    }
  };

  return (
    <div className="usuarios-admin-container">
      <h2>Gestión de Usuarios</h2>
      <p className="usuarios-subtitulo">Administra las cuentas de acceso al sistema.</p>

      {mensaje.texto && <div className={`mensaje ${mensaje.tipo}`}>{mensaje.texto}</div>}

      <div className="usuarios-filtros">
        <input
          placeholder="Buscar por nombre, correo o ID..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <select value={filtroRol} onChange={(e) => setFiltroRol(e.target.value)}>
          <option value="">Todos los roles</option>
          <option value="CLIENTE">Cliente</option>
          <option value="ADMINISTRADOR">Administrador</option>
        </select>
        <select value={filtroActivo} onChange={(e) => setFiltroActivo(e.target.value)}>
          <option value="">Todos</option>
          <option value="true">Activos</option>
          <option value="false">Inactivos</option>
        </select>
      </div>

      {cargando ? (
        <p className="sin-usuarios">Cargando usuarios...</p>
      ) : (
        <>
          <p className="usuarios-conteo">{filtrados.length} usuario{filtrados.length !== 1 ? "s" : ""}</p>
          <div className="usuarios-tabla-wrapper">
            <table className="usuarios-tabla">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nombre</th>
                  <th>Correo</th>
                  <th>Rol</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {filtrados.length === 0 ? (
                  <tr><td colSpan={6} className="sin-usuarios">Sin resultados</td></tr>
                ) : (
                  filtrados.map((u) => (
                    <tr key={u.id}>
                      <td>{u.id}</td>
                      <td><strong>{u.nombre}</strong></td>
                      <td>{u.correo}</td>
                      <td>
                        <span className={`rol-badge rol-${u.rol}`}>{u.rol}</span>
                      </td>
                      <td>
                        <span className={`activo-badge ${u.activo ? "activo-si" : "activo-no"}`}>
                          {u.activo ? "Activo" : "Inactivo"}
                        </span>
                      </td>
                      <td>
                        <div className="acciones-celda">
                          <button
                            className={`btn-toggle-activo ${u.activo ? "btn-desactivar" : "btn-activar"}`}
                            onClick={() => handleToggleActivo(u)}
                          >
                            {u.activo ? "Desactivar" : "Activar"}
                          </button>
                          <button
                            className="btn-toggle-activo btn-eliminar-usuario"
                            onClick={() => handleEliminar(u)}
                          >
                            Eliminar
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}
