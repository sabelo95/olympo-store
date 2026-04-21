import { useState } from "react";
import { registrar } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/LOGO.jpeg";
import "../../styles/register.css";

function RegisterForm() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nombre: "", correo: "", contrasena: "", confirmar: "",
    nit: "", ciudad: "", pais: "Colombia",
  });
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);
  const [exitoso, setExitoso] = useState(false);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (form.contrasena !== form.confirmar) {
      setError("Las contraseñas no coinciden");
      return;
    }

    setCargando(true);
    try {
      await registrar(form);
      setExitoso(true);
      setTimeout(() => navigate("/productos"), 1500);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="register-page">
      {/* Navbar */}
      <nav className="register-navbar">
        <a className="register-navbar-brand" onClick={() => navigate("/productos")} style={{ cursor: "pointer" }}>
          <img src={logo} alt="Olympo Store" className="register-navbar-logo" />
          <span className="register-navbar-nombre">Olympo Store</span>
        </a>
        <button className="register-navbar-back" onClick={() => navigate("/productos")}>
          ← Volver a la tienda
        </button>
      </nav>

      {/* Formulario */}
      <div className="register-body">
        <div className="register-card">
          <div className="register-card-header">
            <img src={logo} alt="Olympo Store" className="register-card-logo" />
            <h1>Crear cuenta</h1>
            <p>Guarda tus datos para agilizar tus próximas compras</p>
          </div>

          {exitoso ? (
            <div className="register-exito">
              ✅ ¡Cuenta creada exitosamente! Redirigiendo...
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              <div className="register-field">
                <label>Nombre completo</label>
                <input
                  name="nombre" placeholder="Juan Pérez"
                  value={form.nombre} onChange={handleChange} required
                />
              </div>

              <div className="register-field">
                <label>Correo electrónico</label>
                <input
                  name="correo" type="email" placeholder="correo@ejemplo.com"
                  value={form.correo} onChange={handleChange} required
                />
              </div>

              <div className="register-grid">
                <div className="register-field">
                  <label>Contraseña</label>
                  <input
                    name="contrasena" type="password" placeholder="••••••••"
                    value={form.contrasena} onChange={handleChange} required
                  />
                </div>
                <div className="register-field">
                  <label>Confirmar contraseña</label>
                  <input
                    name="confirmar" type="password" placeholder="••••••••"
                    value={form.confirmar} onChange={handleChange} required
                  />
                </div>
              </div>

              <div className="register-field">
                <label>NIT / Cédula</label>
                <input
                  name="nit" placeholder="123456789"
                  value={form.nit} onChange={handleChange} required
                />
              </div>

              <div className="register-grid">
                <div className="register-field">
                  <label>Ciudad</label>
                  <input
                    name="ciudad" placeholder="Medellín"
                    value={form.ciudad} onChange={handleChange} required
                  />
                </div>
                <div className="register-field">
                  <label>País</label>
                  <input
                    name="pais" placeholder="Colombia"
                    value={form.pais} onChange={handleChange} required
                  />
                </div>
              </div>

              {error && (
                <div className="register-error">⚠️ {error}</div>
              )}

              <button type="submit" className="register-btn" disabled={cargando}>
                {cargando ? "Creando cuenta..." : "Crear cuenta"}
              </button>

              <p className="register-footer">
                ¿Ya tienes cuenta?{" "}
                <span onClick={() => navigate("/login")}>Iniciar sesión</span>
              </p>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}

export default RegisterForm;
