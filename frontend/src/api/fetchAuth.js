const API_AUTH = import.meta.env.VITE_API_AUTH;

async function refreshAccessToken() {
  const refreshToken = localStorage.getItem("refreshToken");
  if (!refreshToken) throw new Error("Sin refresh token");

  const res = await fetch(`${API_AUTH}/auth/refresh`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken }),
  });
  if (!res.ok) throw new Error("Refresh fallido");

  const data = await res.json();
  localStorage.setItem("accessToken", data.accessToken);
  if (data.refreshToken) localStorage.setItem("refreshToken", data.refreshToken);
  return data.accessToken;
}

function clearSession() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("clienteId");
}

/**
 * fetch autenticado con reintenno automático tras un 401.
 * Úsalo en lugar de fetch() en llamadas que requieren JWT.
 */
export async function fetchAuth(url, options = {}) {
  const token = localStorage.getItem("accessToken");

  const makeRequest = (tkn) =>
    fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
        Authorization: `Bearer ${tkn}`,
      },
    });

  let res = await makeRequest(token);

  if (res.status === 401) {
    try {
      const newToken = await refreshAccessToken();
      res = await makeRequest(newToken);
    } catch {
      clearSession();
      window.location.href = "/login";
      throw new Error("Sesión expirada");
    }
  }

  return res;
}
