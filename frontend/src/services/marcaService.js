const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/marcas`;

export const obtenerMarcas = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Error al obtener marcas");
  return res.json();
};

export const obtenerMarcaPorNombre = async (nombre) => {
  const res = await fetch(`${API_URL}/${nombre}`);
  if (!res.ok) throw new Error("Error al obtener marca");
  return res.json();
};

export const crearMarca = async (marca) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(marca),
  });

  if (!res.ok) throw new Error("Error al crear marca");
  return res.json();
};

export const actualizarMarca = async (nombre, marca) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${nombre}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(marca),
  });

  if (!res.ok) throw new Error("Error al actualizar marca");
  return res.json();
};

export const eliminarMarca = async (nombre) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${nombre}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Error al eliminar marca");
  
  // El endpoint DELETE retorna 204 No Content, no hay JSON
  return res.status === 204 ? null : res.json();
};