import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { obtenerClientePorUsuarioId, actualizarCliente } from "../../services/clienteService";
import { clearSession, getUsuarioId } from "../../services/tokenService";
import { useToast } from "../../context/ToastContext";
import "../../styles/Perfil.css";

export default function Perfil() {
  const navigate = useNavigate();
  const toast = useToast();
  const usuarioId = getUsuarioId();

  const [cliente, setCliente] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [editando, setEditando] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [form, setForm] = useState({ nombre: "", ciudad: "", pais: "" });

  useEffect(() => {
    if (!usuarioId) { navigate("/login"); return; }
    cargarPerfil();
  }, []);

  const cargarPerfil = async () => {
    try {
      const data = await obtenerClientePorUsuarioId(usuarioId);
      setCliente(data);
      setForm({ nombre: data.nombre, ciudad: data.ciudad, pais: data.pais });
    } catch {
      toast("No se pudo cargar el perfil", "error");
    } finally {
      setCargando(false);
    }
  };

  const handleGuardar = async (e) => {
    e.preventDefault();
    if (!form.nombre.trim() || !form.ciudad.trim() || !form.pais.trim()) {
      toast("Todos los campos son obligatorios", "error");
      return;
    }
    setGuardando(true);
    try {
      await actualizarCliente(cliente.nit, {
        nombre: form.nombre,
        nit: cliente.nit,
        ciudad: form.ciudad,
        pais: form.pais,
        usuarioId: cliente.usuarioId,
      });
      setCliente((prev) => ({ ...prev, ...form }));
      setEditando(false);
      toast("Perfil actualizado correctamente", "success");
    } catch (err) {
      toast(err.message || "Error al guardar", "error");
    } finally {
      setGuardando(false);
    }
  };

  const handleLogout = () => {
    clearSession();
    localStorage.removeItem("olympo_carrito");
    navigate("/productos");
  };

  const iniciales = (nombre) =>
    nombre ? nombre.split(" ").map((w) => w[0]).slice(0, 2).join("").toUpperCase() : "?";

  if (cargando) return <div className="perfil-page"><p className="perfil-loading">Cargando perfil...</p></div>;

  return (
    <div className="perfil-page">
      <div className="perfil-container">
        <div className="perfil-header">
          <button onClick={() => navigate("/productos")}>← Volver</button>
          <h1>Mi perfil</h1>
        </div>

        <div className="perfil-avatar">
          <div className="avatar-circulo">{iniciales(cliente?.nombre)}</div>
          <p>Cliente #{cliente?.id}</p>
        </div>

        {/* Datos personales */}
        <div className="perfil-card">
          <h3>Datos personales</h3>
          <form onSubmit={handleGuardar}>
            <div className="perfil-campo">
              <label>Nombre</label>
              <input
                value={form.nombre}
                onChange={(e) => setForm({ ...form, nombre: e.target.value })}
                disabled={!editando}
              />
            </div>
            <div className="perfil-campo">
              <label>NIT / Identificación</label>
              <input value={cliente?.nit || ""} disabled />
            </div>
            <div className="perfil-campo">
              <label>Ciudad</label>
              <input
                value={form.ciudad}
                onChange={(e) => setForm({ ...form, ciudad: e.target.value })}
                disabled={!editando}
              />
            </div>
            <div className="perfil-campo">
              <label>País</label>
              <input
                value={form.pais}
                onChange={(e) => setForm({ ...form, pais: e.target.value })}
                disabled={!editando}
              />
            </div>

            {editando ? (
              <div className="perfil-acciones">
                <button type="submit" className="btn-editar-perfil" disabled={guardando}>
                  {guardando ? "Guardando..." : "Guardar cambios"}
                </button>
                <button
                  type="button"
                  className="btn-cancelar-perfil"
                  onClick={() => {
                    setEditando(false);
                    setForm({ nombre: cliente.nombre, ciudad: cliente.ciudad, pais: cliente.pais });
                  }}
                >
                  Cancelar
                </button>
              </div>
            ) : (
              <div className="perfil-acciones">
                <button type="button" className="btn-editar-perfil" onClick={() => setEditando(true)}>
                  Editar datos
                </button>
              </div>
            )}
          </form>
        </div>

        {/* Accesos rápidos */}
        <div className="perfil-card">
          <h3>Mi cuenta</h3>
          <div className="perfil-links">
            <button className="perfil-link-btn" onClick={() => navigate("/mis-pedidos")}>
              Mis pedidos <span>→</span>
            </button>
            <button className="perfil-link-btn" onClick={() => navigate("/productos")}>
              Ir al catálogo <span>→</span>
            </button>
          </div>
        </div>

        <button className="perfil-logout" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </div>
    </div>
  );
}
