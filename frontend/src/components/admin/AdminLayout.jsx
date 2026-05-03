import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../../styles/AdminLayout.css";

function AdminLayout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarAbierto, setSidebarAbierto] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/");
  };

  const navegar = (path) => {
    navigate(path);
    setSidebarAbierto(false);
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
    <div className="admin-shell">
      {/* Mobile topbar */}
      <div className="admin-topbar">
        <button className="btn-hamburger" onClick={() => setSidebarAbierto(true)}>☰</button>
        <span className="admin-topbar-titulo">Panel Admin</span>
      </div>

      {/* Overlay (mobile only) */}
      {sidebarAbierto && (
        <div className="admin-overlay" onClick={() => setSidebarAbierto(false)} />
      )}

      <aside className={`admin-sidebar${sidebarAbierto ? " abierto" : ""}`}>
        <div className="admin-sidebar-header">
          <h2 className="admin-sidebar-title">Panel Admin</h2>
          <button className="btn-sidebar-cerrar" onClick={() => setSidebarAbierto(false)}>✕</button>
        </div>
        <nav className="admin-nav">
          {menuItems.map(item => (
            <button
              key={item.path}
              onClick={() => navegar(item.path)}
              className={`admin-nav-btn${location.pathname === item.path ? " active" : ""}`}
            >
              {item.label}
            </button>
          ))}
        </nav>
        <button className="admin-logout-btn" onClick={handleLogout}>
          Cerrar Sesión
        </button>
      </aside>

      <main className="admin-content">
        {children}
      </main>
    </div>
  );
}

export default AdminLayout;
