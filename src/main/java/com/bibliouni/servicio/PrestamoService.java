package com.bibliouni.servicio;

import com.bibliouni.excepcion.LimitePrestamosExcedidoException;
import com.bibliouni.excepcion.MaterialNoDisponibleException;
import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.modelo.Material;
import com.bibliouni.modelo.Prestamo;
import com.bibliouni.repositorio.MaterialRepository;
import com.bibliouni.repositorio.PrestamoRepository;
import com.bibliouni.repositorio.UsuarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PrestamoService {

    static final int LIMITE_PRESTAMOS_ACTIVOS = 3;
    static final int DIAS_DE_PRESTAMO = 7;
    static final long MULTA_POR_DIA = 1000L;

    private final PrestamoRepository prestamoRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           MaterialRepository materialRepository,
                           UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Prestamo registrarPrestamo(Long usuarioId, Long materialId, LocalDate fechaPrestamo) {
        usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + usuarioId));
        verificarLimiteDePrestamos(usuarioId);
        Material material = materialRepository.buscarPorId(materialId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Material no encontrado: " + materialId));
        if (!material.estaDisponible()) {
            throw new MaterialNoDisponibleException("El material no tiene ejemplares disponibles: " + materialId);
        }

        material.setEjemplaresDisponibles(material.getEjemplaresDisponibles() - 1);
        materialRepository.guardar(material);

        Prestamo prestamo = new Prestamo(null, usuarioId, materialId,
                fechaPrestamo, fechaPrestamo.plusDays(DIAS_DE_PRESTAMO));
        return prestamoRepository.guardar(prestamo);
    }

    public long calcularMulta(Long prestamoId, LocalDate fechaDevolucion) {
        Prestamo prestamo = prestamoRepository.buscarPorId(prestamoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo no encontrado: " + prestamoId));
        long diasDeRetraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), fechaDevolucion);
        return diasDeRetraso > 0 ? diasDeRetraso * MULTA_POR_DIA : 0L;
    }

    private void verificarLimiteDePrestamos(Long usuarioId) {
        if (prestamoRepository.contarActivosPorUsuario(usuarioId) >= LIMITE_PRESTAMOS_ACTIVOS) {
            throw new LimitePrestamosExcedidoException(
                    "El usuario ya tiene " + LIMITE_PRESTAMOS_ACTIVOS + " préstamos activos");
        }
    }
}
