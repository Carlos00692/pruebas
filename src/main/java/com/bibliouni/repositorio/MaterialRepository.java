package com.bibliouni.repositorio;

import com.bibliouni.modelo.Material;

import java.util.Optional;

public interface MaterialRepository {

    Material guardar(Material material);

    Optional<Material> buscarPorId(Long id);
}
