package com.empresa.catalogo.service;

import com.empresa.catalogo.dto.ProductoRequestDTO;
import com.empresa.catalogo.dto.ProductoResponseDTO;

import java.util.List;

// Interfaz — aplica DIP: el controlador depende de esta abstraccion
public interface ProductoService {
    ProductoResponseDTO crear(ProductoRequestDTO dto);
    ProductoResponseDTO buscarPorId(Long id);
    List<ProductoResponseDTO> listarActivos();
    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto);
    void eliminar(Long id);
}