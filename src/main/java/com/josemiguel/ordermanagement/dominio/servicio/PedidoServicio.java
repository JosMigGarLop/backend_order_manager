package com.josemiguel.ordermanagement.dominio.servicio;

import com.josemiguel.ordermanagement.dominio.modelo.Pedido;
import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;
import com.josemiguel.ordermanagement.infraestructura.repositorio.PedidoRepositorio;
import com.josemiguel.ordermanagement.infraestructura.excepcion.PedidoNoEncontradoException;
import com.josemiguel.ordermanagement.infraestructura.excepcion.EstadoFinalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio para la gestión de pedidos.
 * Contiene métodos para crear, listar, actualizar, cambiar estado y eliminar pedidos.
 */
@Service
@RequiredArgsConstructor
public class PedidoServicio {

    private final PedidoRepositorio pedidoRepositorio;

    /**
     * Determina si un estado es final (DELIVERED, CANCELLED, RETURNED)
     *
     * @param estado EstadoPedido a evaluar
     * @return true si el estado es final
     */
    private boolean esEstadoFinal(EstadoPedido estado) {
        return estado == EstadoPedido.DELIVERED ||
                estado == EstadoPedido.CANCELLED ||
                estado == EstadoPedido.RETURNED;
    }

    /**
     * Crea un nuevo pedido.
     * - El estado y la fecha de creación se asignan automáticamente en @PrePersist de la entidad Pedido.
     *
     * @param pedido Pedido a crear
     * @return Pedido creado
     */
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepositorio.save(pedido);
    }

    /**
     * Lista todos los pedidos existentes en la base de datos.
     *
     * @return Lista de pedidos
     */
    public List<Pedido> listarPedidos() {
        return pedidoRepositorio.findAll();
    }

    /**
     * Obtiene un pedido por su ID.
     *
     * @param id Identificador del pedido
     * @return Optional con el pedido si existe
     */
    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepositorio.findById(id);
    }

    /**
     * Actualiza los datos de un pedido existente.
     * - No permite actualizar pedidos en estado final (DELIVERED, CANCELLED, RETURNED)
     * - Actualiza los campos básicos: nombreCliente, contactoCliente, montoTotal
     * - Sustituye la lista de productos con la nueva lista proporcionada
     *
     * @param id ID del pedido a actualizar
     * @param pedidoActualizado Pedido con los nuevos datos
     * @return Pedido actualizado
     */
    @Transactional
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedidoExistente = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));

        if (esEstadoFinal(pedidoExistente.getEstado())) {
            throw new EstadoFinalException("No se puede actualizar un pedido en estado final: "
                    + pedidoExistente.getEstado());
        }

        pedidoExistente.setNombreCliente(pedidoActualizado.getNombreCliente());
        pedidoExistente.setContactoCliente(pedidoActualizado.getContactoCliente());
        pedidoExistente.setMontoTotal(pedidoActualizado.getMontoTotal());

        // Actualizamos la lista de productos
        pedidoExistente.getProductos().clear();
        if (pedidoActualizado.getProductos() != null) {
            pedidoExistente.getProductos().addAll(pedidoActualizado.getProductos());
        }

        return pedidoRepositorio.save(pedidoExistente);
    }

    /**
     * Cambia el estado de un pedido existente.
     * - Valida que el pedido no esté en estado final
     * - Evita cancelar un pedido ya enviado (SHIPPED)
     *
     * @param id ID del pedido
     * @param estadoStr Nuevo estado como String
     * @return Pedido con estado actualizado
     */
    @Transactional
    public Pedido cambiarEstado(Long id, String estadoStr) {
        Pedido pedido = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));

        EstadoPedido nuevoEstado;
        try {
            nuevoEstado = EstadoPedido.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estadoStr);
        }

        if (esEstadoFinal(pedido.getEstado())) {
            throw new EstadoFinalException("No se puede cambiar el estado de un pedido en estado final: "
                    + pedido.getEstado());
        }

        if (pedido.getEstado() == EstadoPedido.SHIPPED && nuevoEstado == EstadoPedido.CANCELLED) {
            throw new IllegalStateException("No se puede cancelar un pedido ya enviado (SHIPPED). " +
                    "Considera marcarlo como RETURNED si es un error de entrega.");
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepositorio.save(pedido);
    }

    /**
     * Elimina un pedido por su ID.
     * - Lanza excepción si el pedido no existe
     *
     * @param id ID del pedido a eliminar
     */
    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepositorio.existsById(id)) {
            throw new PedidoNoEncontradoException(id);
        }
        pedidoRepositorio.deleteById(id);
    }
}
