package com.bibliouni.excepcion;

public class LimitePrestamosExcedidoException extends RuntimeException {

    public LimitePrestamosExcedidoException(String mensaje) {
        super(mensaje);
    }
}
