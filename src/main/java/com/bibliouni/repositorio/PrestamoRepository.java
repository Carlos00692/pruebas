package com.bibliouni.repositorio;

import com.bibliouni.modelo.Prestamo;

import java.util.Optional;

public interface PrestamoRepository {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorId(Long id);

    long contarActivosPorUsuario(Long usuarioId);
}
