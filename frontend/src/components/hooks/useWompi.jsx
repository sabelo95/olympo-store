import { useState } from "react";
import { abrirWompi } from "../../services/wompiService";

export function useWompi() {
  const [cargandoWompi, setCargandoWompi] = useState(false);
  const [errorWompi, setErrorWompi] = useState(null);

  const iniciarPagoWompi = async ({
    referencia,
    montoEnCentavos,
    redirectUrl,
    onExito,
  }) => {
    setCargandoWompi(true);
    setErrorWompi(null);

    await abrirWompi({
      referencia,
      montoEnCentavos,
      redirectUrl,
      onExito: (transaction) => {
        setCargandoWompi(false);
        onExito(transaction);
      },
      onError: (status) => {
        setCargandoWompi(false);
        setErrorWompi(
          status === "DECLINED"
            ? "Pago rechazado. Verifica tus datos o intenta con otro medio."
            : status === "VOIDED"
            ? "El pago fue cancelado."
            : `Error en el pago: ${status}`
        );
      },
    });
  };

  return { iniciarPagoWompi, cargandoWompi, errorWompi };
}