package com.josemiguel.ordermanagement.infraestructura.controlador;

import com.josemiguel.ordermanagement.dominio.dto.PedidoDTO;
import com.josemiguel.ordermanagement.dominio.mapper.PedidoMapper;
import com.josemiguel.ordermanagement.dominio.modelo.Pedido;
import com.josemiguel.ordermanagement.dominio.servicio.PedidoServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de pedidos.
 * Proporciona endpoints para crear, listar, actualizar, cambiar estado y eliminar pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoControlador {

    private final PedidoServicio pedidoServicio;

    public PedidoControlador(PedidoServicio pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }

    /**
     * Lista todos los pedidos.
     *
     * @return Lista de pedidos en formato DTO
     */
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<PedidoDTO> pedidosDTO = pedidoServicio.listarPedidos().stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidosDTO);
    }

    /**
     * Crea un nuevo pedido.
     * - Valida el DTO recibido
     *
     * @param pedidoDTO Datos del pedido a crear
     * @return Pedido creado en formato DTO
     */
    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido = PedidoMapper.toEntity(pedidoDTO);
        Pedido nuevoPedido = pedidoServicio.crearPedido(pedido);
        return new ResponseEntity<>(PedidoMapper.toDTO(nuevoPedido), HttpStatus.CREATED);
    }

    /**
     * Actualiza un pedido existente.
     * - Valida que el pedido no esté en estado final
     *
     * @param id ID del pedido
     * @param pedidoDTO Datos a actualizar
     * @return Pedido actualizado en formato DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedidoActualizado = pedidoServicio.actualizarPedido(id, PedidoMapper.toEntity(pedidoDTO));
        return ResponseEntity.ok(PedidoMapper.toDTO(pedidoActualizado));
    }

    /**
     * Cambia el estado de un pedido.
     * - Valida que no se modifique si el pedido está en estado final
     *
     * @param id ID del pedido
     * @param estado Nuevo estado como String
     * @return Pedido con estado actualizado en formato DTO
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        Pedido pedidoActualizado = pedidoServicio.cambiarEstado(id, estado);
        return ResponseEntity.ok(PedidoMapper.toDTO(pedidoActualizado));
    }

    /**
     * Elimina un pedido por su ID.
     * - Retorna 204 No Content si se elimina correctamente
     *
     * @param id ID del pedido
     * @return ResponseEntity vacío
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoServicio.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        return pedidoServicio.obtenerPorId(id)
                .map(PedidoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
