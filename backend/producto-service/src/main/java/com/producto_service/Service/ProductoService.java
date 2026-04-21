package com.producto_service.Service;
import com.producto_service.DTO.RequestProductoDto;
import com.producto_service.DTO.ProductoResponseDto;
import com.producto_service.Mapper.ProductoMapper;
import com.producto_service.Model.Marca;
import com.producto_service.Model.Producto;

import com.producto_service.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
@AllArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaService marcaService;
    private final CategoriaService categoriaService;
    private final HistorialService historialService;
    private final ProductoMapper productoMapper;


    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();

    }

    public List<Producto> obtenerProductosPorCategoria(String categoriaNombre) {
        if (categoriaNombre == null || categoriaNombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío.");
        }
        if (categoriaService.obtenerCategoriaPorNombre(categoriaNombre) == null) {
            throw new IllegalArgumentException("La categoría con nombre " + categoriaNombre + " no existe.");
        }

        return productoRepository.findByCategoriaNombre(categoriaNombre);
    }

    public List<String> obtenerProductosPorMarca(String marcaNombre) {
        if (marcaNombre == null || marcaNombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca no puede ser nulo o vacío.");
        }
        if (marcaService.obtenerMarcaPorNombre(marcaNombre) == null) {
            throw new IllegalArgumentException("La marca con nombre " + marcaNombre + " no existe.");
        }
        List<Producto> productos = productoRepository.findByMarcaNombre(marcaNombre);
        List<String> nombresProductos = new ArrayList<>();
        for (Producto producto : productos) {
            nombresProductos.add(producto.getNombre());
        }
        return nombresProductos;
    }

    public List<Producto> obtenerProductosPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede ser nula o vacía.");
        }

        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new IllegalArgumentException("Todos los IDs deben ser números positivos y no nulos.");
        }

        if (productoRepository.findAllById(ids).isEmpty()) {
            throw new IllegalArgumentException("Ningún producto encontrado con los IDs proporcionados.");
        }
        return productoRepository.findAllById(ids);
    }

    public Producto obtenerProductoPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre).orElse(null);
    }

    @Transactional
    public ProductoResponseDto crearProducto(RequestProductoDto productoDto) {

        Producto producto = new Producto();

        if(obtenerProductoPorNombre(productoDto.getNombre()) != null){
            throw new IllegalArgumentException("El producto con nombre " + productoDto.getNombre() + " ya existe.");
        }

        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());

        if(productoDto.getCantidad() == null || productoDto.getCantidad() < 0){
            throw new IllegalArgumentException("La cantidad no puede ser nula o negativa.");
        }
        producto.setCantidad(productoDto.getCantidad());

        if (categoriaService.validarCategoria(productoDto.getCategoria())) {
            producto.setCategoria(productoDto.getCategoria());
        } else {
            throw new IllegalArgumentException("La categoría con ID " + productoDto.getCategoria().getId() + " no existe.");
        }

        if (marcaService.validarMarca(productoDto.getMarca().getNombre())) {
            producto.setMarca(productoDto.getMarca());
        } else {
            throw new IllegalArgumentException("La marca con ID " + productoDto.getMarca().getId() + " no existe.");
        }

        producto.setPrecio(productoDto.getPrecio());
        producto.setCosto(productoDto.getCosto());


        producto.setImagenGeneral(productoDto.getImagenGeneral());
        producto.setImagenNutricional(productoDto.getImagenNutricional());

        Producto productoGuardado = productoRepository.save(producto);
        historialService.agregarHistorial(producto, productoDto.getCantidad());

        return productoMapper.toDto(productoGuardado);
    }




    public void eliminarProducto(String nombre) {
        Producto producto = productoRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con nombre: " + nombre));
        productoRepository.delete(producto);
    }

    public Producto actualizarProducto(String nombre, RequestProductoDto productoDto) {
        Producto productoExistente = obtenerProductoPorNombre(nombre);
        if (productoExistente == null) {
            throw new IllegalArgumentException("El producto con nombre " + nombre + " no existe.");
        }

        // Solo validar si el nombre cambió
        if (!productoExistente.getNombre().equals(productoDto.getNombre())) {
            boolean existe = obtenerTodosLosProductos().stream()
                    .anyMatch(producto -> producto.getNombre().equals(productoDto.getNombre()));

            if (existe) {
                throw new IllegalArgumentException("El producto con nombre " + productoDto.getNombre() + " ya existe.");
            }
        }

        if (productoDto.getNombre() != null) {
            productoExistente.setNombre(productoDto.getNombre());
        }
        if (productoDto.getDescripcion() != null) {
            productoExistente.setDescripcion(productoDto.getDescripcion());
        }
        if (productoDto.getCategoria() != null) {
            if (!categoriaService.validarCategoria(productoDto.getCategoria())) {
                throw new IllegalArgumentException("La categoría con id " + productoDto.getCategoria().getId() + " no existe.");
            }
            productoExistente.setCategoria(productoDto.getCategoria());
        }

        if (productoDto.getMarca() != null) {
            if (!marcaService.validarMarca(productoDto.getMarca().getNombre())) {
                throw new IllegalArgumentException("La marca con nombre " + productoDto.getMarca().getNombre() + " no existe.");
            }
            productoExistente.setMarca(productoDto.getMarca());
        }
        if (productoDto.getPrecio() != null) {
            productoExistente.setPrecio(productoDto.getPrecio());
        }
        if (productoDto.getCantidad() != null) {
            int nuevaCantidad = productoDto.getCantidad();
            productoExistente.setCantidad(nuevaCantidad);
            historialService.agregarHistorial(productoExistente, productoDto.getCantidad());
        }

        return productoRepository.save(productoExistente);
    }

    public void reduccionStock(Map<Long, Integer> productos) {
        for (Map.Entry<Long, Integer> entry : productos.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidadAReducir = entry.getValue();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

            if (cantidadAReducir <= 0) {
                throw new IllegalArgumentException("La cantidad a reducir debe ser mayor que cero para el producto con ID: " + productoId);
            }

            if (producto.getCantidad() < cantidadAReducir) {
                throw new IllegalArgumentException("Stock insuficiente para el producto con ID: " + productoId);
            }

            producto.setCantidad(producto.getCantidad() - cantidadAReducir);
            historialService.agregarHistorial(producto, producto.getCantidad());
            productoRepository.save(producto);
        }
    }

        public void reposicionStock(Map<Long, Integer> productos) {
            for (Map.Entry<Long, Integer> entry : productos.entrySet()) {
                Long productoId = entry.getKey();
                Integer cantidadAReponer = entry.getValue();

                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

                if (cantidadAReponer <= 0) {
                    throw new IllegalArgumentException("La cantidad a reponer debe ser mayor que cero para el producto con ID: " + productoId);
                }

                producto.setCantidad(producto.getCantidad() + cantidadAReponer);
                historialService.agregarHistorial(producto, producto.getCantidad());
                productoRepository.save(producto);
            }
    }

    public List<Producto> obtenerProductosConStockMenorA(int limite) {
        return productoRepository.findAll()
                .stream()
                .filter(p -> p.getCantidad() < limite)
                .toList();
    }





}
