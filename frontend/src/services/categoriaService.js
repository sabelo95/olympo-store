const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/categorias`;

// GET público
export const obtenerCategorias = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Error al obtener categorías");
  return res.json();
};

export const obtenerCategoriaPorId = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Error al obtener categoría");
  return res.json();
};

// POST (ADMIN)
export const crearCategoria = async (categoria) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(categoria), // { nombre: "Proteínas", descripcion: "..." }
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error);
  }

  return res.json();
};

// PUT (ADMIN)
export const actualizarCategoria = async (id, categoria) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(categoria),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error);
  }

  return res.json();
};

// DELETE (ADMIN)
export const eliminarCategoria = async (id) => {
  const token = localStorage.getItem("accessToken");

  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error);
  }

  return res.ok ? null : res.json();
};