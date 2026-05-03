import { useEffect, useState } from "react";
import {
  obtenerUsuarios,
  toggleActivoUsuario,
  eliminarUsuario,
  crearUsuario,
} from "../../services/usuarioService";
import "../../styles/UsuariosAdmin.css";

const FORM_VACIO = { nombre: "", correo: "", contrasena: "", rol: "CLIENTE" };

export default function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [filtrados, setFiltrados] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [busqueda, setBusqueda] = useState("");
  const [filtroRol, setFiltroRol] = useState("");
  const [filtroActivo, setFiltroActivo] = useState("");
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });
  const [modalAbierto, setModalAbierto] = useState(false);
  const [form, setForm] = useState(FORM_VACIO);
  const [guardando, setGuardando] = useState(false);

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
      mostrarMensaje("Error al cargar usuarios.", "error");
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

  const handleCrear = async (e) => {
    e.preventDefault();
    if (!form.nombre.trim() || !form.correo.trim() || !form.contrasena.trim()) {
      mostrarMensaje("Completa todos los campos", "error");
      return;
    }
    setGuardando(true);
    try {
      const nuevo = await crearUsuario(form);
      setUsuarios((prev) => [...prev, nuevo]);
      mostrarMensaje(`Usuario "${nuevo.nombre}" creado exitosamente`, "success");
      setModalAbierto(false);
      setForm(FORM_VACIO);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="usuarios-admin-container">
      <div className="usuarios-header">
        <div>
          <h2>Gestión de Usuarios</h2>
          <p className="usuarios-subtitulo">Administra las cuentas de acceso al sistema.</p>
        </div>
        <button className="btn-crear-usuario" onClick={() => { setForm(FORM_VACIO); setModalAbierto(true); }}>
          + Crear Usuario
        </button>
      </div>

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

      {modalAbierto && (
        <div className="modal-overlay" onClick={() => setModalAbierto(false)}>
          <div className="modal-crear-usuario" onClick={(e) => e.stopPropagation()}>
            <div className="modal-crear-header">
              <h3>Crear Usuario</h3>
              <button className="modal-cerrar" onClick={() => setModalAbierto(false)}>✕</button>
            </div>
            <form className="modal-crear-form" onSubmit={handleCrear}>
              <label>
                Nombre
                <input
                  type="text"
                  placeholder="Nombre completo"
                  value={form.nombre}
                  onChange={(e) => setForm({ ...form, nombre: e.target.value })}
                  autoFocus
                />
              </label>
              <label>
                Correo
                <input
                  type="email"
                  placeholder="correo@ejemplo.com"
                  value={form.correo}
                  onChange={(e) => setForm({ ...form, correo: e.target.value })}
                />
              </label>
              <label>
                Contraseña
                <input
                  type="password"
                  placeholder="Contraseña"
                  value={form.contrasena}
                  onChange={(e) => setForm({ ...form, contrasena: e.target.value })}
                />
              </label>
              <label>
                Rol
                <select
                  value={form.rol}
                  onChange={(e) => setForm({ ...form, rol: e.target.value })}
                >
                  <option value="CLIENTE">Cliente</option>
                  <option value="ADMINISTRADOR">Administrador</option>
                </select>
              </label>
              <div className="modal-crear-acciones">
                <button type="button" className="btn-cancelar-modal" onClick={() => setModalAbierto(false)}>
                  Cancelar
                </button>
                <button type="submit" className="btn-guardar-modal" disabled={guardando}>
                  {guardando ? "Creando..." : "Crear Usuario"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
