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
          backgroundColor: "#fff3cd", color: "#856404", padding: "12px 24px",
          borderRadius: "8px", fontSize: "14px", fontWeight: "500",
          boxShadow: "0 2px 8px rgba(0,0,0,0.15)", zIndex: 1000,
        }}>
          {mensaje}
        </div>
      )}
      <LoginCard />
    </div>
  );
};

export default Login;
