package com.josemiguel.ordermanagement.dominio.mapper;

import com.josemiguel.ordermanagement.dominio.dto.PedidoDTO;
import com.josemiguel.ordermanagement.dominio.dto.ProductoDTO;
import com.josemiguel.ordermanagement.dominio.modelo.Pedido;
import com.josemiguel.ordermanagement.dominio.modelo.Producto;
import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Mapper entre DTOs y entidades.
 * Convierte objetos de tipo Pedido/Producto a PedidoDTO/ProductoDTO y viceversa.
 */
public class PedidoMapper {

    /**
     * Convierte un PedidoDTO a la entidad Pedido
     * - Si el estado es null en el DTO, se asigna CREATED
     * - Convierte la lista de ProductoDTO a Producto
     *
     * @param dto PedidoDTO a convertir
     * @return Pedido convertido
     */
    public static Pedido toEntity(PedidoDTO dto) {
        return Pedido.builder()
                // ¡IMPORTANTE! NO copiamos el id aquí para creación de nuevos pedidos
                // Solo se copia en actualizaciones (ver actualizarPedido en el servicio)
                .nombreCliente(dto.getNombreCliente())
                .contactoCliente(dto.getContactoCliente())
                .montoTotal(dto.getMontoTotal())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoPedido.CREATED)
                .productos(dto.getProductos() != null ? dto.getProductos().stream()
                        .map(p -> Producto.builder()
                                .nombre(p.getNombre())
                                .cantidad(p.getCantidad())
                                .precioUnitario(p.getPrecioUnitario())
                                .build())
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    /**
     * Convierte un Pedido (Entidad) a PedidoDTO
     * - Convierte la lista de Producto a ProductoDTO
     *
     * @param pedido Pedido a convertir
     * @return PedidoDTO convertido
     */
    public static PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();

        // ¡ESTO ES LO QUE FALTABA! Copiar el ID
        dto.setId(pedido.getId());

        dto.setNombreCliente(pedido.getNombreCliente());
        dto.setContactoCliente(pedido.getContactoCliente());
        dto.setMontoTotal(pedido.getMontoTotal());
        dto.setEstado(pedido.getEstado());
        dto.setFechaCreacion(pedido.getFechaCreacion());

        dto.setProductos(pedido.getProductos() != null ? pedido.getProductos().stream()
                .map(p -> {
                    ProductoDTO pdto = new ProductoDTO();
                    // También copia el ID de los productos si existe
                    pdto.setId(p.getId());
                    pdto.setNombre(p.getNombre());
                    pdto.setCantidad(p.getCantidad());
                    pdto.setPrecioUnitario(p.getPrecioUnitario());
                    return pdto;
                }).collect(Collectors.toList())
                : Collections.emptyList());
        return dto;
    }
}