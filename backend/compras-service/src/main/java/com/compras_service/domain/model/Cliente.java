package com.compras_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private Long id;
    private String nombre;
    private String nit;
    private String ciudad;
    private String pais;
    private Long usuario_id;

}
