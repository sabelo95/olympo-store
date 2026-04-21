import { getUsuarioId, getRol } from "./tokenService";

const API_URL = `${import.meta.env.VITE_API_AUTH}/auth`;
const API_CLIENTES = `${import.meta.env.VITE_API_TIENDA}/api/clientes`;

export async function login(data) {
  const response = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error("Error en login");

  const tokens = await response.json();
  localStorage.setItem("accessToken", tokens.accessToken);
  localStorage.setItem("refreshToken", tokens.refreshToken);

  const usuarioId = getUsuarioId();
  const rol = getRol();

  if (usuarioId && rol !== "ADMINISTRADOR") {
    const resCliente = await fetch(`${API_CLIENTES}/usuario/${usuarioId}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${tokens.accessToken}`,
      },
    });
    if (!resCliente.ok) {
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      throw new Error("Tu cuenta no tiene un perfil de cliente. Por favor regístrate.");
    }
    const cliente = await resCliente.json();

    // Si el clienteId cambia (otro usuario), limpiar el carrito local del anterior
    const clienteIdAnterior = localStorage.getItem("clienteId");
    if (clienteIdAnterior && String(clienteIdAnterior) !== String(cliente.id)) {
      localStorage.removeItem("olympo_carrito");
    }

    localStorage.setItem("clienteId", cliente.id);
  }

  return tokens;
}

export async function registrar({ nombre, correo, contrasena, nit, ciudad, pais }) {
  // Limpiar sesión previa Y carrito local (nuevo usuario = carrito limpio)
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("clienteId");
  localStorage.removeItem("olympo_carrito");

  const resUsuario = await fetch(`${API_URL}/crear`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, correo, contrasena, rol: "CLIENTE", activo: true }),
  });
  if (!resUsuario.ok) {
    const err = await resUsuario.text();
    throw new Error(err || "Error al crear usuario");
  }
  const usuario = await resUsuario.json();

  const resCliente = await fetch(API_CLIENTES, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, nit, ciudad, pais, usuarioId: usuario.id }),
  });
  if (!resCliente.ok) {
    const err = await resCliente.text();
    throw new Error(err || "Error al crear cliente");
  }

  const cliente = await resCliente.json();

  // Login automático con las credenciales recién creadas
  await login({ correo, contrasena });

  return cliente;
}
