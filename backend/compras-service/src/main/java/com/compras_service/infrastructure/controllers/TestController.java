//package com.compras_service.infrastructure.controllers;
//import com.compras_service.domain.model.Producto;
//import com.compras_service.infrastructure.adapters.repository.Producto.ProductoGatewayImpl;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@AllArgsConstructor
//public class TestController {
//
//    private final ProductoGatewayImpl productoGatewayImpl;
//
//    @GetMapping("/test")
//    public ResponseEntity<Producto> test() {
//        return productoGatewayImpl.obtenerProductoPorId(9L)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//}
//
