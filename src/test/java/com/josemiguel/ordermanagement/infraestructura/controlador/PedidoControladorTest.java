package com.josemiguel.ordermanagement.infraestructura.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josemiguel.ordermanagement.dominio.dto.PedidoDTO;
import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;
import com.josemiguel.ordermanagement.dominio.servicio.PedidoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControladorTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PedidoServicio pedidoServicio;

    @InjectMocks
    private PedidoControlador pedidoControlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoControlador)
                .setControllerAdvice(new com.josemiguel.ordermanagement.infraestructura.excepcion.GlobalExceptionHandler()) // si tienes handler
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearPedido_deberiaRetornar201() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setNombreCliente("Jose");
        dto.setContactoCliente("12345");
        dto.setMontoTotal(new java.math.BigDecimal("100.0"));
        dto.setEstado(EstadoPedido.CREATED);

        when(pedidoServicio.crearPedido(any())).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCliente").value("Jose"))
                .andExpect(jsonPath("$.contactoCliente").value("12345"))
                .andExpect(jsonPath("$.montoTotal").value(100.0))
                .andExpect(jsonPath("$.estado").value("CREATED"));
    }

    @Test
    void crearPedido_datosInvalidos_deberiaRetornar400() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setNombreCliente(""); // Nombre inválido
        dto.setContactoCliente("12345");
        dto.setMontoTotal(new java.math.BigDecimal("100.0"));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field=='nombreCliente')]").exists()); // Cambiado para reflejar array de errores
    }
}
