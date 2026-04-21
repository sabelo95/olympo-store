const API_URL = `${import.meta.env.VITE_API_ARCHIVOS}/productos`;

const getToken = () => localStorage.getItem("accessToken");

const headers = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`
});

const publicHeaders = () => ({
  "Content-Type": "application/json"
});

export const obtenerProductos = async () => {
  try {
    const res = await fetch(API_URL, { headers: publicHeaders() });
    
    if (!res.ok) {
      throw new Error(`Error al obtener productos: ${res.status}`);
    }
    
    const data = await res.json();
    
    
    // Asegurar que cada producto tenga un id único
    return data.map((producto, index) => ({
      ...producto,
      // Si no tiene id, usar el índice como fallback
      id: producto.id || `temp-${index}-${Date.now()}`
    }));
  } catch (error) {
    console.error("Error en obtenerProductos:", error);
    throw error;
  }
};

export const crearProducto = async (producto) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: headers(),
    body: JSON.stringify(producto)
  });
  
  if (!res.ok) {
    const errorData = await res.text();
    throw new Error(errorData || 'Error al crear producto');
  }
  
  return res.json();
};

// Nueva función para crear producto con imágenes
export const crearProductoConImagenes = async (productoData, imagenGeneral, imagenNutricional) => {
  const formData = new FormData();
  
  // Agregar el producto como JSON
  formData.append('producto', new Blob([JSON.stringify(productoData)], {
    type: 'application/json'
  }));
  
  // Agregar las imágenes si existen
  if (imagenGeneral) {
    formData.append('imagenGeneral', imagenGeneral);
  }
  if (imagenNutricional) {
    formData.append('imagenNutricional', imagenNutricional);
  }
  
  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${getToken()}`
      // No incluir Content-Type, el navegador lo establece automáticamente con el boundary correcto
    },
    body: formData
  });
  
  if (!res.ok) {
    const errorData = await res.text();
    throw new Error(errorData || 'Error al crear producto con imágenes');
  }
  
  return res.json();
};

export const actualizarProducto = async (nombre, producto) => {
  const encodedNombre = encodeURIComponent(nombre);
  const res = await fetch(`${API_URL}/${encodedNombre}`, {
    method: "PUT",
    headers: headers(),
    body: JSON.stringify(producto)
  });
  
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    console.error("Error del servidor:", errorData);
    throw new Error(JSON.stringify(errorData));
  }
  
  return res.json();
};

// Nueva función para actualizar producto con imágenes
export const actualizarProductoConImagenes = async (nombre, productoData, imagenGeneral, imagenNutricional) => {
  const formData = new FormData();
  
  // Agregar el producto como JSON
  formData.append('producto', new Blob([JSON.stringify(productoData)], {
    type: 'application/json'
  }));
  
  // Agregar las imágenes si existen
  if (imagenGeneral) {
    formData.append('imagenGeneral', imagenGeneral);
  }
  if (imagenNutricional) {
    formData.append('imagenNutricional', imagenNutricional);
  }
  
  const encodedNombre = encodeURIComponent(nombre);
  const res = await fetch(`${API_URL}/${encodedNombre}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${getToken()}`
    },
    body: formData
  });
  
  if (!res.ok) {
    const errorData = await res.text();
    throw new Error(errorData || 'Error al actualizar producto');
  }
  
  return res.json();
};

export const obtenerProductoPorNombre = async (nombre) => {
  const encodedNombre = encodeURIComponent(nombre);
  const res = await fetch(`${API_URL}/${encodedNombre}`, {
    headers: headers()
  });
  
  if (!res.ok) {
    throw new Error(`Error al obtener producto: ${res.status}`);
  }
  
  return res.json();
};

export const eliminarProducto = async (nombre) => {
  const encodedNombre = encodeURIComponent(nombre);
  const res = await fetch(`${API_URL}/${encodedNombre}`, {
    method: "DELETE",
    headers: headers()
  });
  
  if (!res.ok) {
    throw new Error(`Error al eliminar producto: ${res.status}`);
  }
};