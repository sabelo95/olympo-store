const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/tamanos`;

export const obtenerTamanos = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Error al obtener tamaños");
  return res.json();
};

export const obtenerTamanosActivos = async () => {
  const res = await fetch(`${API_URL}/activos`);
  if (!res.ok) throw new Error("Error al obtener tamaños activos");
  return res.json();
};

export const obtenerTamanoPorId = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Error al obtener tamaño");
  return res.json();
};

export const crearTamano = async (tamano) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(tamano),
  });

  if (!res.ok) throw new Error("Error al crear tamaño");
  return res.json();
};

export const actualizarTamano = async (id, tamano) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(tamano),
  });

  if (!res.ok) throw new Error("Error al actualizar tamaño");
  return res.json();
};

export const eliminarTamano = async (id) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Error al eliminar tamaño");
  
  return res.status === 204 ? null : res.json();
};

export const eliminarTamanoPermanente = async (id) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}/hard`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Error al eliminar tamaño permanentemente");
  
  return res.status === 204 ? null : res.json();
};