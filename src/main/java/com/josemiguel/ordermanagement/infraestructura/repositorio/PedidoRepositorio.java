package com.josemiguel.ordermanagement.infraestructura.repositorio;

import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;
import com.josemiguel.ordermanagement.dominio.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Pedido.
 * Proporciona métodos de búsqueda por estado, fecha de creación y cliente.
 */
@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

    /**
     * Encuentra todos los pedidos con un estado específico.
     *
     * @param estado Estado del pedido
     * @return Lista de pedidos con el estado dado
     */
    List<Pedido> findByEstado(EstadoPedido estado);

    /**
     * Encuentra pedidos creados entre dos fechas.
     *
     * @param inicio Fecha de inicio
     * @param fin    Fecha de fin
     * @return Lista de pedidos dentro del rango de fechas
     */
    List<Pedido> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Encuentra pedidos por coincidencia parcial del nombre del cliente.
     *
     * @param nombreCliente Nombre o parte del nombre del cliente
     * @return Lista de pedidos que coinciden con el nombre
     */
    List<Pedido> findByNombreClienteContainingIgnoreCase(String nombreCliente);

    /**
     * Encuentra pedidos filtrando por estado y nombre del cliente.
     *
     * @param estado        Estado del pedido
     * @param nombreCliente Nombre del cliente
     * @return Lista de pedidos que cumplen ambos criterios
     */
    List<Pedido> findByEstadoAndNombreClienteContainingIgnoreCase(EstadoPedido estado, String nombreCliente);

    /**
     * Encuentra pedidos filtrando por estado y rango de fecha de creación.
     *
     * @param estado Estado del pedido
     * @param inicio Fecha de inicio
     * @param fin    Fecha de fin
     * @return Lista de pedidos que cumplen ambos criterios
     */
    List<Pedido> findByEstadoAndFechaCreacionBetween(EstadoPedido estado, LocalDateTime inicio, LocalDateTime fin);
}
