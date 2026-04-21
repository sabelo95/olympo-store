package com.compras_service.infrastructure.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/api/wompi")
@RequiredArgsConstructor
public class WompiController {

    private static final String INTEGRITY_SECRET = "test_integrity_lOmazuoUrCcbAfxSyeSU4uQk8HWZYAQk";

    @GetMapping("/firma")
    public ResponseEntity<Map<String, String>> generarFirma(
            @RequestParam String referencia,
            @RequestParam Long montoEnCentavos,
            @RequestParam String moneda) {
        try {
            String cadena = referencia + montoEnCentavos + moneda + INTEGRITY_SECRET;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(cadena.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return ResponseEntity.ok(Map.of("firma", hexString.toString()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error generando firma: " + e.getMessage()));
        }
    }
}
