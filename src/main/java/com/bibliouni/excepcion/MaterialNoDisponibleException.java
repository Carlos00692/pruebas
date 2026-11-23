package com.bibliouni.excepcion;

public class MaterialNoDisponibleException extends RuntimeException {

    public MaterialNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
