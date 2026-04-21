import { Navigate } from "react-router-dom";
import { getRol } from "../../services/tokenService";

function ProtectedRoute({ children, allowedRoles }) {
  const rol = getRol();

  // No logueado
  if (!rol) {
    return <Navigate to="/admin/login" replace />;
  }

  // Rol no autorizado
  if (!allowedRoles.includes(rol)) {
    return <Navigate to="/productos" replace />;
  }

  // 👇 ESTO ES CLAVE
  return children;
}

export default ProtectedRoute;
