package com.josemiguel.ordermanagement.infraestructura.excepcion;

/**
 * Excepción lanzada cuando un pedido con un ID específico
 * no se encuentra en la base de datos.
 */
public class PedidoNoEncontradoException extends RuntimeException {

    /**
     * Constructor que genera el mensaje con el ID del pedido.
     *
     * @param id ID del pedido que no se encontró
     */
    public PedidoNoEncontradoException(Long id) {
        super("Pedido no encontrado: " + id);
    }
}
