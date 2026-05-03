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
          <span className="login-navbar-nombre" style={{ fontFamily: "var(--font-display)", fontWeight: 900, fontSize: 20, textTransform: "uppercase", letterSpacing: "0.04em" }}>OLYMPO<small style={{ display: "block", fontFamily: "var(--font-body)", fontSize: 8, letterSpacing: "0.32em", color: "var(--olympo-lime)", marginTop: 2, fontWeight: 700 }}>· STORE ·</small></span>
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
            <h1>INICIAR SESIÓN</h1>
            <p>Guarda tus datos de envío y sigue tus pedidos</p>
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
