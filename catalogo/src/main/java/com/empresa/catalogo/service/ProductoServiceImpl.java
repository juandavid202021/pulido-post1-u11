package com.empresa.catalogo.service;

import com.empresa.catalogo.dto.ProductoRequestDTO;
import com.empresa.catalogo.dto.ProductoResponseDTO;
import com.empresa.catalogo.entity.Producto;
import com.empresa.catalogo.exception.RecursoNoEncontradoException;
import com.empresa.catalogo.factory.ProductoFactory;
import com.empresa.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final ProductoFactory    factory;

    // Inyeccion por constructor (buena practica)
    public ProductoServiceImpl(ProductoRepository repo, ProductoFactory factory) {
        this.repo    = repo;
        this.factory = factory;
    }

    @Override
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Producto p = factory.toEntity(dto);
        return factory.toResponseDTO(repo.save(p));
    }

    @Override
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
        return factory.toResponseDTO(p);
    }

    @Override
    public List<ProductoResponseDTO> listarActivos() {
        return repo.findByActivoTrue()
                .stream()
                .map(factory::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setCategoria(dto.getCategoria());
        return factory.toResponseDTO(repo.save(p));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        buscarPorId(id); // verifica que existe antes de eliminar
        repo.deleteById(id);
    }
}