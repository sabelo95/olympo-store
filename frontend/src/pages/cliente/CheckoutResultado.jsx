import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

export default function CheckoutResultado() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [estado, setEstado] = useState("cargando");

  useEffect(() => {
    const idTransaccion = searchParams.get("id");
    if (!idTransaccion) {
      setEstado("error");
      return;
    }
    // Wompi pasa el id de transacción en la URL
    // Consultamos directamente la API pública de Wompi
    fetch(`https://sandbox.wompi.co/v1/transactions/${idTransaccion}`)
      .then((r) => r.json())
      .then((data) => {
        const status = data?.data?.status;
        if (status === "APPROVED") setEstado("aprobado");
        else if (status === "DECLINED") setEstado("rechazado");
        else if (status === "VOIDED") setEstado("cancelado");
        else setEstado("pendiente");
      })
      .catch(() => setEstado("error"));
  }, []);

  const info = {
    cargando:  { icono: "⏳", titulo: "Verificando pago...",       color: "#666" },
    aprobado:  { icono: "🎉", titulo: "¡Pago aprobado!",           color: "#28a745" },
    rechazado: { icono: "❌", titulo: "Pago rechazado",             color: "#dc3545" },
    cancelado: { icono: "⚠️", titulo: "Pago cancelado",             color: "#ffc107" },
    pendiente: { icono: "⏳", titulo: "Pago pendiente",             color: "#007bff" },
    error:     { icono: "⚠️", titulo: "Error verificando el pago", color: "#dc3545" },
  }[estado];

  return (
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center",
      justifyContent: "center", backgroundColor: "#f8f9fa", fontFamily: "'Segoe UI', sans-serif" }}>
      <div style={{ background: "white", padding: "48px 40px", borderRadius: "16px",
        boxShadow: "0 4px 24px rgba(0,0,0,0.1)", textAlign: "center", maxWidth: "420px", width: "100%" }}>
        <div style={{ fontSize: "64px", marginBottom: "16px" }}>{info.icono}</div>
        <h2 style={{ color: info.color, marginBottom: "12px" }}>{info.titulo}</h2>
        {estado === "aprobado" && (
          <p style={{ color: "#666", marginBottom: "24px" }}>
            Tu pedido fue pagado exitosamente. Te notificaremos cuando esté en camino.
          </p>
        )}
        {estado === "rechazado" && (
          <p style={{ color: "#666", marginBottom: "24px" }}>
            El pago no pudo procesarse. Intenta con otro método de pago.
          </p>
        )}
        <button
          onClick={() => navigate("/")}
          style={{ padding: "12px 28px", backgroundColor: "#1a1a2e", color: "white",
            border: "none", borderRadius: "8px", fontSize: "15px",
            fontWeight: "600", cursor: "pointer" }}>
          Volver al catálogo
        </button>
      </div>
    </div>
  );
}