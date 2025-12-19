package com.josemiguel.ordermanagement.infraestructura.excepcion;

/**
 * Excepción para indicar que un pedido se encuentra en un estado final
 * y no puede ser modificado (DELIVERED, CANCELLED, RETURNED).
 */
public class EstadoFinalException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param mensaje Mensaje de error que describe la situación
     */
    public EstadoFinalException(String mensaje) {
        super(mensaje);
    }
}
