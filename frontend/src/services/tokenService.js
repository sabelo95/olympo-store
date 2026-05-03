import { jwtDecode } from "jwt-decode";

export const saveTokens = (accessToken, refreshToken) => {
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);
};

export const getDecodedToken = () => {
  const token = localStorage.getItem("accessToken");
  if (!token) return null;
  try {
    return jwtDecode(token);
  } catch {
    return null;
  }
};

export const isTokenValid = () => {
  const decoded = getDecodedToken();
  if (!decoded) return false;
  // exp está en segundos, Date.now() en milisegundos
  return decoded.exp * 1000 > Date.now();
};

export const clearSession = async () => {
  const refreshToken = localStorage.getItem("refreshToken");
  if (refreshToken) {
    try {
      await fetch(`${import.meta.env.VITE_API_AUTH}/auth/logout`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ refreshToken }),
      });
    } catch {
      // ignorar error de red en logout
    }
  }
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("clienteId");
  localStorage.removeItem("olympo_carrito");
};

export const getRol = () => {
  const decoded = getDecodedToken();
  return decoded?.rol;
};

export const getUsuarioId = () => {
  const decoded = getDecodedToken();
  return decoded?.UsuarioId ?? null;
};

export const isAdmin = () => getRol() === "ADMINISTRADOR";
