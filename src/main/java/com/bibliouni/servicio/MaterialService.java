package com.bibliouni.servicio;

import com.bibliouni.modelo.Material;
import com.bibliouni.repositorio.MaterialRepository;

import java.util.Optional;

public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material crear(Material material) {
        if (material == null || material.getTitulo() == null || material.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título del material es obligatorio");
        }
        if (material.getEjemplaresDisponibles() < 0) {
            throw new IllegalArgumentException("Los ejemplares no pueden ser negativos");
        }
        return materialRepository.guardar(material);
    }

    public Optional<Material> buscarPorId(Long id) {
        return materialRepository.buscarPorId(id);
    }
}
