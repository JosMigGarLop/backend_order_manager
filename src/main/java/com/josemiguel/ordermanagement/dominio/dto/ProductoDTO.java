package com.josemiguel.ordermanagement.dominio.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para la transferencia de datos de un Producto.
 * Contiene id, nombre, cantidad y precio unitario.
 */
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre del producto no puede exceder 100 caracteres")
    private String nombre;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a cero")
    private BigDecimal precioUnitario;

    // Getters y setters para id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Los demás getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}