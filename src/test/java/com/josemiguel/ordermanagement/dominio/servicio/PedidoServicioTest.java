package com.josemiguel.ordermanagement.dominio.servicio;

import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;
import com.josemiguel.ordermanagement.dominio.modelo.Pedido;
import com.josemiguel.ordermanagement.infraestructura.excepcion.EstadoFinalException;
import com.josemiguel.ordermanagement.infraestructura.excepcion.PedidoNoEncontradoException;
import com.josemiguel.ordermanagement.infraestructura.repositorio.PedidoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServicioTest {

    private PedidoRepositorio pedidoRepositorio;
    private PedidoServicio pedidoServicio;

    @BeforeEach
    void setUp() {
        pedidoRepositorio = mock(PedidoRepositorio.class);
        pedidoServicio = new PedidoServicio(pedidoRepositorio);
    }

    @Test
    void crearPedido_deberiaGuardarPedido() {
        Pedido pedido = Pedido.builder()
                .nombreCliente("Jose")
                .contactoCliente("12345")
                .montoTotal(BigDecimal.valueOf(100))
                .build();

        when(pedidoRepositorio.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoServicio.crearPedido(pedido);

        assertEquals("Jose", resultado.getNombreCliente());
        verify(pedidoRepositorio, times(1)).save(pedido);
    }

    @Test
    void listarPedidos_deberiaRetornarTodos() {
        Pedido pedido1 = Pedido.builder().id(1L).build();
        Pedido pedido2 = Pedido.builder().id(2L).build();
        when(pedidoRepositorio.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<Pedido> resultado = pedidoServicio.listarPedidos();

        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerPorId_deberiaRetornarPedidoExistente() {
        Pedido pedido = Pedido.builder().id(1L).build();
        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pedidoServicio.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obtenerPorId_deberiaRetornarVacioSiNoExiste() {
        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.empty());

        Optional<Pedido> resultado = pedidoServicio.obtenerPorId(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void actualizarPedido_deberiaCambiarDatos() {
        Pedido existente = Pedido.builder()
                .id(1L)
                .nombreCliente("Jose")
                .contactoCliente("12345")
                .montoTotal(BigDecimal.valueOf(100))
                .estado(EstadoPedido.CREATED)
                .build();

        Pedido actualizado = Pedido.builder()
                .nombreCliente("Juan")
                .contactoCliente("67890")
                .montoTotal(BigDecimal.valueOf(200))
                .build();

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(existente));
        when(pedidoRepositorio.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        Pedido resultado = pedidoServicio.actualizarPedido(1L, actualizado);

        assertEquals("Juan", resultado.getNombreCliente());
        assertEquals("67890", resultado.getContactoCliente());
        assertEquals(BigDecimal.valueOf(200), resultado.getMontoTotal());
    }

    @Test
    void actualizarPedido_enEstadoFinal_deberiaLanzarExcepcion() {
        Pedido existente = Pedido.builder()
                .id(1L)
                .estado(EstadoPedido.DELIVERED)
                .build();

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(existente));

        EstadoFinalException ex = assertThrows(EstadoFinalException.class,
                () -> pedidoServicio.actualizarPedido(1L, new Pedido()));

        assertTrue(ex.getMessage().contains("No se puede actualizar un pedido en estado final"));
    }

    @Test
    void cambiarEstado_deberiaActualizarEstado() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .estado(EstadoPedido.CREATED)
                .build();

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositorio.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        Pedido resultado = pedidoServicio.cambiarEstado(1L, "CONFIRMED");

        assertEquals(EstadoPedido.CONFIRMED, resultado.getEstado());
    }

    @Test
    void cambiarEstado_aFinal_deberiaLanzarExcepcion() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .estado(EstadoPedido.DELIVERED)
                .build();

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));

        EstadoFinalException ex = assertThrows(EstadoFinalException.class,
                () -> pedidoServicio.cambiarEstado(1L, "CREATED"));

        assertTrue(ex.getMessage().contains("No se puede cambiar el estado de un pedido en estado final"));
    }

    @Test
    void cambiarEstado_cancelarShipped_deberiaLanzarIllegalStateException() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .estado(EstadoPedido.SHIPPED)
                .build();

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> pedidoServicio.cambiarEstado(1L, "CANCELLED"));

        assertTrue(ex.getMessage().contains("No se puede cancelar un pedido ya enviado"));
    }

    @Test
    void eliminarPedido_deberiaEliminarExistente() {
        when(pedidoRepositorio.existsById(1L)).thenReturn(true);

        pedidoServicio.eliminarPedido(1L);

        verify(pedidoRepositorio, times(1)).deleteById(1L);
    }

    @Test
    void eliminarPedido_noExistente_deberiaLanzarExcepcion() {
        when(pedidoRepositorio.existsById(1L)).thenReturn(false);

        PedidoNoEncontradoException ex = assertThrows(PedidoNoEncontradoException.class,
                () -> pedidoServicio.eliminarPedido(1L));

        assertTrue(ex.getMessage().contains("Pedido no encontrado: 1"));
    }
}
