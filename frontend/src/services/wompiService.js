const WOMPI_PUBLIC_KEY = import.meta.env.VITE_WOMPI_PUBLIC_KEY;
const WOMPI_SCRIPT_URL = "https://checkout.wompi.co/widget.js";
const API_COMPRAS = import.meta.env.VITE_API_TIENDA;

export function cargarScriptWompi() {
  return new Promise((resolve, reject) => {
    if (window.WidgetCheckout) {
      resolve();
      return;
    }

    console.log("WOMPI KEY:", import.meta.env.VITE_WOMPI_PUBLIC_KEY);
    
    if (document.getElementById("wompi-script")) {
      const interval = setInterval(() => {
        if (window.WidgetCheckout) {
          clearInterval(interval);
          resolve();
        }
      }, 100);
      setTimeout(() => {
        clearInterval(interval);
        reject(new Error("Wompi tardó demasiado en cargar"));
      }, 10000);
      return;
    }

    const script = document.createElement("script");
    script.id = "wompi-script";
    script.src = WOMPI_SCRIPT_URL;
    script.setAttribute("data-render", "explicit");
    script.async = true;

    script.onload = () => {
      const interval = setInterval(() => {
        if (window.WidgetCheckout) {
          clearInterval(interval);
          resolve();
        }
      }, 100);
      setTimeout(() => {
        clearInterval(interval);
        reject(new Error("Wompi tardó demasiado en cargar"));
      }, 10000);
    };

    script.onerror = () => reject(new Error("No se pudo cargar el script de Wompi"));
    document.body.appendChild(script);
  });
}

async function obtenerFirma(referencia, montoEnCentavos) {
  const res = await fetch(
    `${API_COMPRAS}/api/wompi/firma?referencia=${referencia}&montoEnCentavos=${montoEnCentavos}&moneda=COP`
  );
  if (!res.ok) throw new Error("Error obteniendo firma de integridad");
  const data = await res.json();
  return data.firma;
}

export async function abrirWompi({
  referencia,
  montoEnCentavos,
  redirectUrl,
  onExito,
  onError,
}) {
  try {
    await cargarScriptWompi();

    const firma = await obtenerFirma(referencia, montoEnCentavos);

    const checkout = new window.WidgetCheckout({
      currency: "COP",
      amountInCents: Math.round(montoEnCentavos),
      reference: referencia,
      publicKey: WOMPI_PUBLIC_KEY,
      redirectUrl,
      signature: {
        integrity: firma,
      },
    });

    checkout.open((result) => {
      const { transaction } = result;
      if (transaction?.status === "APPROVED") {
        onExito(transaction);
      } else {
        onError(transaction?.status || "DECLINED");
      }
    });
  } catch (err) {
    onError(err.message);
  }
}