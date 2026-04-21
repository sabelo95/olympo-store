import LoginForm from "./LoginForm";
import logo from "../../assets/LOGO.jpeg";
import { useNavigate } from "react-router-dom";

const LoginCard = () => {
  const navigate = useNavigate();

  return (
    <>
      {/* Navbar */}
      <nav className="login-navbar">
        <div className="login-navbar-brand" onClick={() => navigate("/productos")}>
          <img src={logo} alt="Olympo Store" className="login-navbar-logo-nav" />
          <span className="login-navbar-nombre">Olympo Store</span>
        </div>
        <button className="login-navbar-back" onClick={() => navigate("/productos")}>
          ← Volver a la tienda
        </button>
      </nav>

      {/* Tarjeta */}
      <div className="login-body">
        <div className="login-card">
          <div className="login-card-header">
            <img src={logo} alt="Olympo Store" className="login-logo" />
            <h1>Iniciar sesión</h1>
            <p>Inicia sesión para tener tus datos de envío guardados</p>
          </div>

          <LoginForm />

          <div className="login-divider">o</div>

          <p className="login-footer">
            ¿No tienes cuenta?{" "}
            <span onClick={() => navigate("/registro")}>Regístrate gratis</span>
          </p>

          <button className="login-footer-skip" onClick={() => navigate("/productos")}>
            Continuar sin cuenta →
          </button>
        </div>
      </div>
    </>
  );
};

export default LoginCard;
