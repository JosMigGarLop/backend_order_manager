package com.josemiguel.ordermanagement.dominio.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Pedido.
 * Representa un pedido realizado por un cliente, con su lista de productos y estado.
 * Adaptada para consumo seguro desde Angular.
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String nombreCliente;

    @NotBlank(message = "El contacto del cliente es obligatorio")
    @Size(max = 100, message = "El contacto del cliente no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String contactoCliente;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser mayor a cero")
    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Lista de productos asociados al pedido.
     * Siempre inicializada para evitar nulls en Angular.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    /**
     * Método que se ejecuta al crear el pedido.
     * - Asigna fecha de creación
     * - Inicializa estado en CREATED
     */
    @PrePersist
    protected void alCrear() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.CREATED;
    }
}
