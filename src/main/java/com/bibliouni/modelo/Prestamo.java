package com.bibliouni.modelo;

import java.time.LocalDate;

public class Prestamo {

    private Long id;
    private Long usuarioId;
    private Long materialId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;

    public Prestamo() {
    }

    public Prestamo(Long id, Long usuarioId, Long materialId,
                    LocalDate fechaPrestamo, LocalDate fechaVencimiento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.materialId = materialId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
