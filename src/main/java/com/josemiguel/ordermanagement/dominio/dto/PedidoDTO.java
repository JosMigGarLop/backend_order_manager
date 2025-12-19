package com.josemiguel.ordermanagement.dominio.dto;

import com.josemiguel.ordermanagement.dominio.modelo.EstadoPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la transferencia de datos de un Pedido.
 * Contiene información básica del cliente, monto, estado y lista de productos.
 */
public class PedidoDTO {

    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    private String nombreCliente;

    @NotBlank(message = "El contacto del cliente es obligatorio")
    @Size(max = 100, message = "El contacto del cliente no puede exceder 100 caracteres")
    private String contactoCliente;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser mayor a cero")
    private BigDecimal montoTotal;

    /**
     * Estado del pedido.
     * Puede ser null al crear un pedido, se asignará CREATED por defecto.
     */
    private EstadoPedido estado;

    // AÑADE ESTE CAMPO
    private LocalDateTime fechaCreacion;

    /**
     * Lista de productos asociados al pedido.
     * @Valid asegura que cada ProductoDTO sea validado
     */
    @Valid
    private List<ProductoDTO> productos = new ArrayList<>();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getContactoCliente() { return contactoCliente; }
    public void setContactoCliente(String contactoCliente) { this.contactoCliente = contactoCliente; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<ProductoDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoDTO> productos) { this.productos = productos; }
}