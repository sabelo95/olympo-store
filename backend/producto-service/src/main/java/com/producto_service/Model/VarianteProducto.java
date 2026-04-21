package com.producto_service.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "variante_producto")
@Data
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 MANY variantes pertenecen a UN producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // 🔹 MANY variantes pueden compartir el mismo tamaño
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tamano_id", nullable = false)
    private Tamano tamano;

    // 🔹 MANY variantes pueden compartir el mismo sabor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sabor_id", nullable = false)
    private Sabor sabor;

    private Integer stock;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}