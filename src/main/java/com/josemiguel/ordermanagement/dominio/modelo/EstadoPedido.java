package com.josemiguel.ordermanagement.dominio.modelo;

/**
 * Estados posibles de un pedido.
 * Algunos son finales (DELIVERED, CANCELLED, RETURNED) y bloquean actualizaciones.
 */
public enum EstadoPedido {
    CREATED,   // Pedido creado
    CONFIRMED, // Pedido confirmado para envío
    SHIPPED,   // Pedido enviado a transportista
    DELIVERED, // Pedido entregado al cliente (final exitoso)
    CANCELLED, // Pedido cancelado (final negativo)
    RETURNED   // Pedido devuelto o no entregado (final negativo)
}
