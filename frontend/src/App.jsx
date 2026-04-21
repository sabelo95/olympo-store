import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import AdminLayout from "./components/admin/AdminLayout";
import Login from "./pages/Login";
import AdminLogin from "./pages/AdminLogin";
import ProductosCliente from "./pages/cliente/Productos";
import DetalleProducto from "./pages/cliente/DetalleProducto";
import Perfil from "./pages/cliente/Perfil";
import MisPedidos from "./pages/cliente/MisPedidos";
import Dashboard from "./pages/administrador/Dashboard";
import ProductosAdmin from "./pages/administrador/ProductosAdmin";
import Ventas from "./pages/administrador/Ventas";
import Clientes from "./pages/administrador/Clientes";
import Usuarios from "./pages/administrador/Usuarios";
import CuponesAdmin from "./pages/administrador/CuponesAdmin";
import MarcasAdmin from "./pages/administrador/MarcasAdmin";
import CategoriasAdmin from "./pages/administrador/CategoriasAdmin";
import EditarProducto from "./pages/administrador/EditarProducto";
import AgregarProducto from "./pages/administrador/AgregarProducto";
import TamanosAdmin from './pages/administrador/TamanosAdmin';
import SaboresAdmin from "./pages/administrador/SaboresAdmin";
import RegisterForm from "./components/login/RegisterForm";
import Checkout from "./pages/cliente/Checkout";
import CheckoutResultado from "./pages/cliente/CheckoutResultado";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* TIENDA PÚBLICA — acceso directo sin login */}
        <Route path="/" element={<ProductosCliente />} />
        <Route path="/productos" element={<ProductosCliente />} />

        {/* DETALLE DE PRODUCTO */}
        <Route path="/producto/:nombre" element={<DetalleProducto />} />

        {/* CLIENTE AUTENTICADO */}
        <Route path="/perfil" element={<Perfil />} />
        <Route path="/mis-pedidos" element={<MisPedidos />} />

        {/* LOGIN CLIENTES (opcional) */}
        <Route path="/login" element={<Login />} />
        <Route path="/registro" element={<RegisterForm />} />

        {/* LOGIN ADMINISTRADOR */}
        <Route path="/admin/login" element={<AdminLogin />} />

        {/* CHECKOUT */}
        <Route path="/checkout" element={<Checkout />} />
        <Route path="/checkout/resultado" element={<CheckoutResultado />} />

        {/* ADMIN — rutas protegidas */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <Dashboard />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/productos"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <ProductosAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/ventas"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <Ventas />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/clientes"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <Clientes />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/usuarios"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <Usuarios />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/cupones"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <CuponesAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/marcas"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <MarcasAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/categorias"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <CategoriasAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/tamanos"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <TamanosAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/sabores"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <SaboresAdmin />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/productos/agregar"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <AgregarProducto />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/productos/editar/:nombre"
          element={
            <ProtectedRoute allowedRoles={["ADMINISTRADOR"]}>
              <AdminLayout>
                <EditarProducto />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
