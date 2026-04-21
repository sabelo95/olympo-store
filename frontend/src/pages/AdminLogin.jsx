import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";
import { getRol } from "../services/tokenService";
import logo from "../assets/LOGO.jpeg";
import "../styles/adminLogin.css";

export default function AdminLogin() {
  const navigate = useNavigate();
  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
      await login({ correo, contrasena });
      const rol = getRol();
      if (rol === "ADMINISTRADOR") {
        navigate("/dashboard");
      } else {
        setError("Acceso denegado. Esta área es solo para administradores.");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("clienteId");
      }
    } catch {
      setError("Correo o contraseña incorrectos.");
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="admin-login-page">
      <div className="admin-login-card">
        <img src={logo} alt="Olympo Store" className="admin-login-logo" />
        <h1>Panel Administrador</h1>
        <p>Acceso restringido al equipo de Olympo Store</p>

        <form onSubmit={handleSubmit}>
          <div className="admin-login-input">
            <input
              type="email"
              placeholder="Correo administrador"
              value={correo}
              onChange={(e) => setCorreo(e.target.value)}
              required
            />
          </div>
          <div className="admin-login-input">
            <input
              type="password"
              placeholder="Contraseña"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              required
            />
          </div>

          {error && <div className="admin-login-error">{error}</div>}

          <button type="submit" className="admin-login-btn" disabled={cargando}>
            {cargando ? "Ingresando..." : "Ingresar al panel"}
          </button>
        </form>

        <a href="/productos" className="admin-login-back">← Volver a la tienda</a>
      </div>
    </div>
  );
}
