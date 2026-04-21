package com.producto_service.Service;

import com.producto_service.DTO.CategoriaDto;
import com.producto_service.Model.Categoria;
import com.producto_service.Repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // =========================================================================
    // CONSULTAS
    // =========================================================================

    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // =========================================================================
    // CREAR
    // =========================================================================

    public Categoria crearCategoria(CategoriaDto categoriaDto) {
        // Validar datos básicos
        if (categoriaDto.getNombre() == null || categoriaDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        // Validar que el nombre no exista
        if (categoriaRepository.findByNombre(categoriaDto.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una categoría con el nombre '" + categoriaDto.getNombre() + "'"
            );
        }

        // Crear y guardar
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDto.getNombre());
        nuevaCategoria.setDescripcion(categoriaDto.getDescripcion());
        nuevaCategoria.setProductos(categoriaDto.getProductos());

        return categoriaRepository.save(nuevaCategoria);
    }

    // =========================================================================
    // ACTUALIZAR
    // =========================================================================

    /**
     * Actualiza una categoría existente por su ID.
     * Valida que:
     * - La categoría exista
     * - El nuevo nombre no esté en uso por otra categoría
     */
    public Categoria actualizarCategoria(Long id, CategoriaDto categoriaDto) {
        // 1. Validar que la categoría existe
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una categoría con el ID " + id
                ));

        // 2. Validar datos básicos
        if (categoriaDto.getNombre() == null || categoriaDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        // 3. Validar que el nuevo nombre no esté en uso por OTRA categoría
        String nuevoNombre = categoriaDto.getNombre().trim();
        if (!nuevoNombre.equals(categoriaExistente.getNombre())) {
            Categoria categoriaConNombre = categoriaRepository.findByNombre(nuevoNombre);

            if (categoriaConNombre != null && !categoriaConNombre.getId().equals(id)) {
                throw new IllegalArgumentException(
                        "Ya existe otra categoría con el nombre '" + nuevoNombre + "'"
                );
            }
        }

        // 4. Actualizar los campos
        categoriaExistente.setNombre(nuevoNombre);
        categoriaExistente.setDescripcion(categoriaDto.getDescripcion());

        // Solo actualizar productos si vienen en el DTO (opcional)
        if (categoriaDto.getProductos() != null) {
            categoriaExistente.setProductos(categoriaDto.getProductos());
        }

        // 5. Guardar y retornar
        return categoriaRepository.save(categoriaExistente);
    }

    /**
     * @deprecated Usar actualizarCategoria(Long id, CategoriaDto categoriaDto) en su lugar
     */
    @Deprecated
    public Categoria actualizarCategoria(Categoria categoria) {
        if (categoria.getId() == null || !categoriaRepository.existsById(categoria.getId())) {
            throw new IllegalArgumentException("La categoría con ID " + categoria.getId() + " no existe");
        }
        return categoriaRepository.save(categoria);
    }

    // =========================================================================
    // ELIMINAR
    // =========================================================================

    /**
     * Elimina una categoría por su ID
     */
    public void eliminarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una categoría con el ID " + id
                ));

        // Validar si tiene productos asociados (opcional, según tu lógica de negocio)
        if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la categoría porque tiene productos asociados"
            );
        }

        categoriaRepository.delete(categoria);
    }

    /**
     * Elimina una categoría por su nombre
     */
    public void eliminarCategoriaPorNombre(String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre);

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "No existe una categoría con el nombre '" + nombre + "'"
            );
        }

        // Validar si tiene productos asociados
        if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la categoría porque tiene productos asociados"
            );
        }

        categoriaRepository.delete(categoria);
    }

    // =========================================================================
    // VALIDACIONES
    // =========================================================================

    /**
     * Valida si existe una categoría con el ID dado
     */
    public boolean existeCategoria(Long id) {
        return categoriaRepository.existsById(id);
    }

    /**
     * Valida si existe una categoría con el nombre dado
     */
    public boolean existeCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre) != null;
    }

    /**
     * @deprecated Usar existeCategoria(Long id) en su lugar
     */
    @Deprecated
    public boolean validarCategoria(Categoria categoria) {
        if (categoria == null || categoria.getId() == null) {
            return false;
        }
        return categoriaRepository.existsById(categoria.getId());
    }
}
