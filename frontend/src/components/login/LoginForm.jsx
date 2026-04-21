import { useState } from "react";
import { login } from "../../services/authService";
import { getRol } from "../../services/tokenService";
import { useNavigate, useLocation } from "react-router-dom";

function LoginForm() {
  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

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
        // Volver a la página anterior o ir al catálogo
        const from = location.state?.from || "/productos";
        navigate(from);
      }
    } catch {
      setError("Correo o contraseña incorrectos.");
    } finally {
      setCargando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="login-input">
        <input
          type="email"
          placeholder="Correo"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
          required
        />
      </div>
      <div className="login-input">
        <input
          type="password"
          placeholder="Contraseña"
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          required
        />
      </div>

      {error && (
        <div style={{
          padding: "10px 14px", backgroundColor: "#f8d7da",
          color: "#721c24", borderRadius: "6px", fontSize: "13px",
          marginBottom: "12px",
        }}>
          {error}
        </div>
      )}

      <button type="submit" className="login-button" disabled={cargando}>
        {cargando ? "Ingresando..." : "Iniciar sesión"}
      </button>
    </form>
  );
}

export default LoginForm;
