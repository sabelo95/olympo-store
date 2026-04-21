import { useNavigate, useLocation } from "react-router-dom";

function AdminLayout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/");
  };

  const menuItems = [
    { path: "/dashboard",        label: "Dashboard" },
    { path: "/admin/productos",  label: "Productos" },
    { path: "/admin/ventas",     label: "Ventas / Pedidos" },
    { path: "/admin/clientes",   label: "Clientes" },
    { path: "/admin/usuarios",   label: "Usuarios" },
    { path: "/admin/cupones",    label: "Cupones" },
    { path: "/admin/marcas",     label: "Marcas" },
    { path: "/admin/categorias", label: "Categorías" },
    { path: "/admin/tamanos",    label: "Tamaños" },
    { path: "/admin/sabores",    label: "Sabores" },
  ];

  return (
    <div style={{ display: "flex", minHeight: "100vh" }}>
      {/* Sidebar */}
      <div style={{
        width: "250px",
        backgroundColor: "#2c3e50",
        color: "white",
        padding: "20px",
        display: "flex",
        flexDirection: "column"
      }}>
        <h2 style={{ marginBottom: "30px", textAlign: "center" }}>
          Panel Admin
        </h2>

        <nav style={{ flex: 1 }}>
          {menuItems.map(item => (
            <button
              key={item.path}
              onClick={() => navigate(item.path)}
              style={{
                width: "100%",
                padding: "12px 15px",
                marginBottom: "10px",
                backgroundColor: location.pathname === item.path ? "#34495e" : "transparent",
                color: "white",
                border: "none",
                borderRadius: "5px",
                cursor: "pointer",
                textAlign: "left",
                fontSize: "16px",
                transition: "background-color 0.3s"
              }}
              onMouseEnter={(e) => {
                if (location.pathname !== item.path) {
                  e.target.style.backgroundColor = "#34495e";
                }
              }}
              onMouseLeave={(e) => {
                if (location.pathname !== item.path) {
                  e.target.style.backgroundColor = "transparent";
                }
              }}
            >
              {item.label}
            </button>
          ))}
        </nav>

        <button
          onClick={handleLogout}
          style={{
            width: "100%",
            padding: "12px 15px",
            backgroundColor: "#e74c3c",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer",
            fontSize: "16px",
            marginTop: "auto"
          }}
        >
          Cerrar Sesión
        </button>
      </div>

      {/* Content */}
      <div style={{ flex: 1, backgroundColor: "#ecf0f1" }}>
        {children}
      </div>
    </div>
  );
}

export default AdminLayout;