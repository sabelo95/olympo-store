const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/sabores`;

export const obtenerSabores = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Error al obtener sabores");
  return res.json();
};

export const obtenerSaboresActivos = async () => {
  const res = await fetch(`${API_URL}/activos`);
  if (!res.ok) throw new Error("Error al obtener sabores activos");
  return res.json();
};

export const obtenerSaborPorId = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Error al obtener sabor");
  return res.json();
};

export const crearSabor = async (sabor) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(sabor),
  });

  if (!res.ok) throw new Error("Error al crear sabor");
  return res.json();
};

export const actualizarSabor = async (id, sabor) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(sabor),
  });

  if (!res.ok) throw new Error("Error al actualizar sabor");
  return res.json();
};

export const eliminarSabor = async (id) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Error al eliminar sabor");
  
  return res.status === 204 ? null : res.json();
};

export const eliminarSaborPermanente = async (id) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}/hard`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Error al eliminar sabor permanentemente");
  
  return res.status === 204 ? null : res.json();
};