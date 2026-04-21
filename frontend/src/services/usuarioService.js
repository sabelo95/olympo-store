const API_AUTH = `${import.meta.env.VITE_API_AUTH}/auth`;
const getToken = () => localStorage.getItem("accessToken");

const headers = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export const obtenerUsuarios = async () => {
  const res = await fetch(`${API_AUTH}/usuarios`, { headers: headers() });
  if (!res.ok) throw new Error("Error al obtener usuarios");
  return res.json();
};

export const obtenerUsuarioPorId = async (id) => {
  const res = await fetch(`${API_AUTH}/obtener/${id}`, { headers: headers() });
  if (!res.ok) throw new Error("Usuario no encontrado");
  return res.json();
};

export const actualizarUsuario = async (id, datos) => {
  const res = await fetch(`${API_AUTH}/actualizar/${id}`, {
    method: "PUT",
    headers: headers(),
    body: JSON.stringify(datos),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al actualizar usuario");
  }
  return res.json();
};

export const eliminarUsuario = async (id) => {
  const res = await fetch(`${API_AUTH}/eliminar/${id}`, {
    method: "DELETE",
    headers: headers(),
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error al eliminar usuario");
  }
};

// Activa o desactiva usando el endpoint de actualización (no hay endpoints separados)
export const toggleActivoUsuario = async (usuario) => {
  return actualizarUsuario(usuario.id, {
    nombre: usuario.nombre,
    correo: usuario.correo,
    contrasena: usuario.contrasena ?? "",
    rol: usuario.rol,
    activo: !usuario.activo,
  });
};
