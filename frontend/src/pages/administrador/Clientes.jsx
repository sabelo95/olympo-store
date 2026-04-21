import { useState } from "react";
import {
  obtenerClientePorNit,
  crearCliente,
  actualizarCliente,
  eliminarCliente,
} from "../../services/clienteService";
import "../../styles/ClientesAdmin.css";

const FORM_VACIO = { nombre: "", nit: "", ciudad: "", pais: "", usuarioId: "" };

export default function Clientes() {
  const [tab, setTab] = useState("buscar"); // "buscar" | "crear"
  const [nitBusqueda, setNitBusqueda] = useState("");
  const [clienteEncontrado, setClienteEncontrado] = useState(null);
  const [editando, setEditando] = useState(false);
  const [formEditar, setFormEditar] = useState(FORM_VACIO);
  const [formCrear, setFormCrear] = useState(FORM_VACIO);
  const [cargando, setCargando] = useState(false);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  const mostrarMensaje = (texto, tipo) => {
    setMensaje({ texto, tipo });
    setTimeout(() => setMensaje({ texto: "", tipo: "" }), 4000);
  };

  // --- BUSCAR ---
  const handleBuscar = async (e) => {
    e.preventDefault();
    if (!nitBusqueda.trim()) return;
    setCargando(true);
    setClienteEncontrado(null);
    setEditando(false);
    try {
      const data = await obtenerClientePorNit(nitBusqueda.trim());
      setClienteEncontrado(data);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setCargando(false);
    }
  };

  // --- EDITAR ---
  const handleIniciarEdicion = () => {
    setFormEditar({
      nombre: clienteEncontrado.nombre ?? "",
      nit: clienteEncontrado.nit ?? "",
      ciudad: clienteEncontrado.ciudad ?? "",
      pais: clienteEncontrado.pais ?? "",
      usuarioId: clienteEncontrado.usuarioId ?? "",
    });
    setEditando(true);
  };

  const handleGuardarEdicion = async (e) => {
    e.preventDefault();
    if (!formEditar.nombre.trim() || !formEditar.ciudad.trim() || !formEditar.pais.trim()) {
      mostrarMensaje("Nombre, ciudad y país son obligatorios", "error");
      return;
    }
    setCargando(true);
    try {
      await actualizarCliente(clienteEncontrado.nit, {
        nombre: formEditar.nombre,
        nit: clienteEncontrado.nit,
        ciudad: formEditar.ciudad,
        pais: formEditar.pais,
        usuarioId: Number(formEditar.usuarioId),
      });
      mostrarMensaje("Cliente actualizado exitosamente", "success");
      setEditando(false);
      // Recargar datos del cliente actualizado
      const actualizado = await obtenerClientePorNit(clienteEncontrado.nit);
      setClienteEncontrado(actualizado);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setCargando(false);
    }
  };

  const handleCancelarEdicion = () => {
    setEditando(false);
    setFormEditar(FORM_VACIO);
  };

  // --- ELIMINAR ---
  const handleEliminar = async () => {
    if (!window.confirm(`¿Eliminar al cliente "${clienteEncontrado.nombre}" (NIT: ${clienteEncontrado.nit})?`)) return;
    setCargando(true);
    try {
      await eliminarCliente(clienteEncontrado.nit);
      mostrarMensaje("Cliente eliminado exitosamente", "success");
      setClienteEncontrado(null);
      setNitBusqueda("");
      setEditando(false);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setCargando(false);
    }
  };

  // --- CREAR ---
  const handleCrear = async (e) => {
    e.preventDefault();
    const { nombre, nit, ciudad, pais, usuarioId } = formCrear;
    if (!nombre.trim() || !nit.trim() || !ciudad.trim() || !pais.trim() || !usuarioId) {
      mostrarMensaje("Todos los campos son obligatorios", "error");
      return;
    }
    setCargando(true);
    try {
      await crearCliente({ nombre, nit, ciudad, pais, usuarioId: Number(usuarioId) });
      mostrarMensaje("Cliente creado exitosamente", "success");
      setFormCrear(FORM_VACIO);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="clientes-admin-container">
      <h2>Gestión de Clientes</h2>

      {mensaje.texto && (
        <div className={`mensaje ${mensaje.tipo}`}>{mensaje.texto}</div>
      )}

      <div className="clientes-tabs">
        <button
          className={`tab-btn ${tab === "buscar" ? "active" : ""}`}
          onClick={() => setTab("buscar")}
        >
          Buscar / Editar
        </button>
        <button
          className={`tab-btn ${tab === "crear" ? "active" : ""}`}
          onClick={() => setTab("crear")}
        >
          Crear Cliente
        </button>
      </div>

      {/* ===== TAB BUSCAR ===== */}
      {tab === "buscar" && (
        <>
          <div className="buscar-section">
            <h3>Buscar por NIT</h3>
            <form onSubmit={handleBuscar} className="buscar-row">
              <input
                type="text"
                placeholder="Ingresa el NIT del cliente"
                value={nitBusqueda}
                onChange={(e) => setNitBusqueda(e.target.value)}
              />
              <button type="submit" className="btn-buscar" disabled={cargando}>
                {cargando ? "Buscando..." : "Buscar"}
              </button>
            </form>
          </div>

          {!clienteEncontrado && !cargando && (
            <p className="sin-resultado">
              Ingresa un NIT para buscar un cliente.
            </p>
          )}

          {clienteEncontrado && !editando && (
            <div className="cliente-card">
              <div className="cliente-card-header">
                <h3>{clienteEncontrado.nombre}</h3>
                <div className="cliente-card-acciones">
                  <button className="btn-editar" onClick={handleIniciarEdicion}>
                    ✏️ Editar
                  </button>
                  <button className="btn-eliminar" onClick={handleEliminar} disabled={cargando}>
                    🗑️ Eliminar
                  </button>
                </div>
              </div>
              <div className="cliente-info-grid">
                <div className="cliente-info-item">
                  <label>NIT</label>
                  <span>{clienteEncontrado.nit}</span>
                </div>
                <div className="cliente-info-item">
                  <label>ID interno</label>
                  <span>{clienteEncontrado.id}</span>
                </div>
                <div className="cliente-info-item">
                  <label>Ciudad</label>
                  <span>{clienteEncontrado.ciudad}</span>
                </div>
                <div className="cliente-info-item">
                  <label>País</label>
                  <span>{clienteEncontrado.pais}</span>
                </div>
                <div className="cliente-info-item">
                  <label>Usuario ID</label>
                  <span>{clienteEncontrado.usuarioId}</span>
                </div>
              </div>
            </div>
          )}

          {clienteEncontrado && editando && (
            <form onSubmit={handleGuardarEdicion} className="cliente-form">
              <h3>Editar cliente — NIT: {clienteEncontrado.nit}</h3>
              <div className="form-grid">
                <div className="form-group">
                  <label>Nombre</label>
                  <input
                    type="text"
                    value={formEditar.nombre}
                    onChange={(e) => setFormEditar({ ...formEditar, nombre: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label>NIT</label>
                  <input type="text" value={clienteEncontrado.nit} disabled />
                </div>
                <div className="form-group">
                  <label>Ciudad</label>
                  <input
                    type="text"
                    value={formEditar.ciudad}
                    onChange={(e) => setFormEditar({ ...formEditar, ciudad: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label>País</label>
                  <input
                    type="text"
                    value={formEditar.pais}
                    onChange={(e) => setFormEditar({ ...formEditar, pais: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label>Usuario ID</label>
                  <input
                    type="number"
                    value={formEditar.usuarioId}
                    onChange={(e) => setFormEditar({ ...formEditar, usuarioId: e.target.value })}
                  />
                </div>
              </div>
              <div className="form-buttons">
                <button type="submit" className="btn-primary" disabled={cargando}>
                  {cargando ? "Guardando..." : "Guardar cambios"}
                </button>
                <button type="button" className="btn-secondary" onClick={handleCancelarEdicion}>
                  Cancelar
                </button>
              </div>
            </form>
          )}
        </>
      )}

      {/* ===== TAB CREAR ===== */}
      {tab === "crear" && (
        <form onSubmit={handleCrear} className="cliente-form">
          <h3>Nuevo cliente</h3>
          <div className="form-grid">
            <div className="form-group">
              <label>Nombre</label>
              <input
                type="text"
                placeholder="Nombre completo"
                value={formCrear.nombre}
                onChange={(e) => setFormCrear({ ...formCrear, nombre: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>NIT</label>
              <input
                type="text"
                placeholder="Número de identificación"
                value={formCrear.nit}
                onChange={(e) => setFormCrear({ ...formCrear, nit: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>Ciudad</label>
              <input
                type="text"
                placeholder="Ciudad"
                value={formCrear.ciudad}
                onChange={(e) => setFormCrear({ ...formCrear, ciudad: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>País</label>
              <input
                type="text"
                placeholder="País"
                value={formCrear.pais}
                onChange={(e) => setFormCrear({ ...formCrear, pais: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>Usuario ID</label>
              <input
                type="number"
                placeholder="ID del usuario asociado"
                value={formCrear.usuarioId}
                onChange={(e) => setFormCrear({ ...formCrear, usuarioId: e.target.value })}
              />
            </div>
          </div>
          <div className="form-buttons">
            <button type="submit" className="btn-primary" disabled={cargando}>
              {cargando ? "Creando..." : "Crear cliente"}
            </button>
            <button
              type="button"
              className="btn-secondary"
              onClick={() => setFormCrear(FORM_VACIO)}
            >
              Limpiar
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
