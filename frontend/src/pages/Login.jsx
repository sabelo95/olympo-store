import LoginCard from "../components/login/LoginCard";
import { useLocation } from "react-router-dom";
import "../styles/login.css";

const Login = () => {
  const { state } = useLocation();
  const mensaje = state?.mensaje;

  return (
    <div className="login-page">
      {mensaje && (
        <div style={{
          position: "fixed", top: "20px", left: "50%", transform: "translateX(-50%)",
          background: "rgba(245,166,35,0.12)", color: "var(--olympo-gold)",
          border: "1px solid rgba(245,166,35,0.3)",
          padding: "12px 24px", borderRadius: "var(--r-md)",
          fontSize: "14px", fontWeight: "500", zIndex: 1000,
          backdropFilter: "blur(8px)",
        }}>
          {mensaje}
        </div>
      )}
      <LoginCard />
    </div>
  );
};

export default Login;
