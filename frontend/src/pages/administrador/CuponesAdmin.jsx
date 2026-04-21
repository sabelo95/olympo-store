import { useEffect, useState } from "react";
import {
  listarCupones,
  crearCupon,
  actualizarCupon,
  eliminarCupon,
} from "../../services/cuponService";
import "../../styles/CuponesAdmin.css";

const fmt = (n) =>
  n != null
    ? new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", minimumFractionDigits: 0 }).format(n)
    : "—";

const FORM_VACIO = {
  codigo: "",
  descuentoPorcentaje: "",
  descuentoFijo: "",
  activo: true,
  fechaExpiracion: "",
  usoMaximo: "",
  montoMinimo: "",
};

export default function CuponesAdmin() {
  const [cupones, setCupones] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [form, setForm] = useState(FORM_VACIO);
  const [editando, setEditando] = useState(null); // código del cupón en edición
  const [guardando, setGuardando] = useState(false);
  const [mensaje, setMensaje] = useState({ texto: "", tipo: "" });

  useEffect(() => { cargar(); }, []);

  const mostrarMensaje = (texto, tipo) => {
    setMensaje({ texto, tipo });
    setTimeout(() => setMensaje({ texto: "", tipo: "" }), 4000);
  };

  const cargar = async () => {
    setCargando(true);
    try {
      const data = await listarCupones();
      setCupones(data);
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setCargando(false);
    }
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const validarForm = () => {
    if (!form.codigo.trim()) return "El código es obligatorio.";
    if (!form.descuentoPorcentaje && !form.descuentoFijo)
      return "Debes definir descuento porcentual o fijo.";
    if (form.descuentoPorcentaje && form.descuentoFijo)
      return "Usa solo un tipo de descuento: porcentual o fijo.";
    if (form.descuentoPorcentaje && (Number(form.descuentoPorcentaje) < 1 || Number(form.descuentoPorcentaje) > 100))
      return "El porcentaje debe ser entre 1 y 100.";
    return null;
  };

  const buildPayload = () => ({
    codigo: form.codigo.trim().toUpperCase(),
    descuentoPorcentaje: form.descuentoPorcentaje ? Number(form.descuentoPorcentaje) : null,
    descuentoFijo: form.descuentoFijo ? Number(form.descuentoFijo) : null,
    activo: form.activo,
    fechaExpiracion: form.fechaExpiracion || null,
    usoMaximo: form.usoMaximo ? Number(form.usoMaximo) : null,
    montoMinimo: form.montoMinimo ? Number(form.montoMinimo) : null,
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const error = validarForm();
    if (error) { mostrarMensaje(error, "error"); return; }

    setGuardando(true);
    try {
      if (editando) {
        const actualizado = await actualizarCupon(editando, buildPayload());
        setCupones((prev) => prev.map((c) => (c.codigo === editando ? actualizado : c)));
        mostrarMensaje("Cupón actualizado exitosamente", "success");
      } else {
        const nuevo = await crearCupon(buildPayload());
        setCupones((prev) => [...prev, nuevo]);
        mostrarMensaje("Cupón creado exitosamente", "success");
      }
      cancelar();
    } catch (err) {
      mostrarMensaje(err.message, "error");
    } finally {
      setGuardando(false);
    }
  };

  const handleEditar = (cupon) => {
    setEditando(cupon.codigo);
    setForm({
      codigo: cupon.codigo,
      descuentoPorcentaje: cupon.descuentoPorcentaje ?? "",
      descuentoFijo: cupon.descuentoFijo ?? "",
      activo: cupon.activo,
      fechaExpiracion: cupon.fechaExpiracion ?? "",
      usoMaximo: cupon.usoMaximo ?? "",
      montoMinimo: cupon.montoMinimo ?? "",
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleEliminar = async (cupon) => {
    if (!window.confirm(`¿Eliminar el cupón "${cupon.codigo}"?`)) return;
    try {
      await eliminarCupon(cupon.codigo);
      setCupones((prev) => prev.filter((c) => c.codigo !== cupon.codigo));
      mostrarMensaje("Cupón eliminado exitosamente", "success");
    } catch (err) {
      mostrarMensaje(err.message, "error");
    }
  };

  const cancelar = () => {
    setEditando(null);
    setForm(FORM_VACIO);
  };

  const estaVencido = (fecha) => fecha && new Date(fecha) < new Date();
  const usosAgotados = (c) => c.usoMaximo != null && c.usoActual >= c.usoMaximo;

  return (
    <div className="cupones-container">
      <h2>Gestión de Cupones</h2>
      <p className="cupones-subtitulo">Crea y administra los cupones de descuento de la tienda.</p>

      {mensaje.texto && <div className={`mensaje ${mensaje.tipo}`}>{mensaje.texto}</div>}

      {/* Formulario */}
      <div className="cupon-form-card">
        <h3>{editando ? `Editando cupón: ${editando}` : "Nuevo cupón"}</h3>

        <form onSubmit={handleSubmit}>
          <div className="cupon-form-grid">

            <div className="form-group">
              <label>Código *</label>
              <input
                name="codigo"
                value={form.codigo}
                onChange={handleChange}
                placeholder="Ej: VERANO20"
                disabled={!!editando}
                style={{ textTransform: "uppercase" }}
              />
              {editando && <p className="hint">El código no se puede cambiar</p>}
            </div>

            <div className="form-group">
              <label>Descuento porcentual (%)</label>
              <input
                type="number"
                name="descuentoPorcentaje"
                value={form.descuentoPorcentaje}
                onChange={handleChange}
                placeholder="Ej: 15  →  15%"
                min="1"
                max="100"
                disabled={!!form.descuentoFijo}
              />
              <p className="hint">Deja vacío si usas descuento fijo</p>
            </div>

            <div className="form-group">
              <label>Descuento fijo (COP)</label>
              <input
                type="number"
                name="descuentoFijo"
                value={form.descuentoFijo}
                onChange={handleChange}
                placeholder="Ej: 5000  →  $5.000"
                min="0"
                disabled={!!form.descuentoPorcentaje}
              />
              <p className="hint">Deja vacío si usas descuento porcentual</p>
            </div>

            <div className="form-group">
              <label>Monto mínimo de compra (COP)</label>
              <input
                type="number"
                name="montoMinimo"
                value={form.montoMinimo}
                onChange={handleChange}
                placeholder="Ej: 50000  →  $50.000 (opcional)"
                min="0"
              />
            </div>

            <div className="form-group">
              <label>Fecha de expiración</label>
              <input
                type="date"
                name="fechaExpiracion"
                value={form.fechaExpiracion}
                onChange={handleChange}
                min={new Date().toISOString().split("T")[0]}
              />
              <p className="hint">Opcional — sin fecha no expira</p>
            </div>

            <div className="form-group">
              <label>Usos máximos</label>
              <input
                type="number"
                name="usoMaximo"
                value={form.usoMaximo}
                onChange={handleChange}
                placeholder="Ej: 100 (opcional, sin límite si vacío)"
                min="1"
              />
            </div>

            <div className="form-check">
              <input
                type="checkbox"
                id="activo"
                name="activo"
                checked={form.activo}
                onChange={handleChange}
              />
              <label htmlFor="activo">Cupón activo</label>
            </div>

          </div>

          <div className="cupon-form-acciones">
            <button type="submit" className="btn-primary" disabled={guardando}>
              {guardando ? "Guardando..." : editando ? "Guardar cambios" : "Crear cupón"}
            </button>
            {editando && (
              <button type="button" className="btn-secondary" onClick={cancelar}>
                Cancelar
              </button>
            )}
          </div>
        </form>
      </div>

      {/* Tabla */}
      {cargando ? (
        <p className="sin-cupones">Cargando cupones...</p>
      ) : (
        <>
          <p className="cupones-conteo">
            {cupones.length} cupón{cupones.length !== 1 ? "es" : ""} registrado{cupones.length !== 1 ? "s" : ""}
          </p>
          <div className="cupones-tabla-wrapper">
            <table className="cupones-tabla">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Descuento</th>
                  <th>Monto mín.</th>
                  <th>Usos</th>
                  <th>Expira</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {cupones.length === 0 ? (
                  <tr><td colSpan={7} className="sin-cupones">No hay cupones creados</td></tr>
                ) : (
                  cupones.map((cupon) => {
                    const vencido = estaVencido(cupon.fechaExpiracion);
                    const agotado = usosAgotados(cupon);
                    return (
                      <tr key={cupon.codigo}>
                        <td><span className="codigo-cell">{cupon.codigo}</span></td>

                        <td className="descuento-cell">
                          {cupon.descuentoPorcentaje
                            ? `${cupon.descuentoPorcentaje}%`
                            : cupon.descuentoFijo
                            ? fmt(cupon.descuentoFijo)
                            : "—"}
                        </td>

                        <td>{cupon.montoMinimo ? fmt(cupon.montoMinimo) : "Sin mínimo"}</td>

                        <td className={`usos-cell ${agotado ? "usos-agotados" : ""}`}>
                          {cupon.usoMaximo != null
                            ? `${cupon.usoActual} / ${cupon.usoMaximo}${agotado ? " ⚠ agotado" : ""}`
                            : `${cupon.usoActual ?? 0} / ilimitado`}
                        </td>

                        <td className={`fecha-cell ${vencido ? "fecha-vencida" : ""}`}>
                          {cupon.fechaExpiracion
                            ? `${new Date(cupon.fechaExpiracion).toLocaleDateString("es-CO")}${vencido ? " ⚠ vencido" : ""}`
                            : "Sin vencimiento"}
                        </td>

                        <td>
                          <span className={`badge-activo ${cupon.activo && !vencido && !agotado ? "si" : "no"}`}>
                            {cupon.activo && !vencido && !agotado ? "Activo" : "Inactivo"}
                          </span>
                        </td>

                        <td>
                          <div className="acciones-cell">
                            <button className="btn-editar" onClick={() => handleEditar(cupon)}>
                              ✏️ Editar
                            </button>
                            <button className="btn-eliminar" onClick={() => handleEliminar(cupon)}>
                              🗑️ Eliminar
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}
