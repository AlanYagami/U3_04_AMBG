package com.almacenes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "almacenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String clave;
    
    @Column(nullable = false)
    private LocalDate fechaRegistro;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioVenta;
    
    @NotNull(message = "El precio de renta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de renta debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioRenta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TamanoAlmacen tamano;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @PrePersist
    public void generarClave() {
        if (this.clave == null && this.sede != null) {
            this.clave = String.format("%s-A%d", this.sede.getClave(), this.id != null ? this.id : 0);
        }
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDate.now();
        }
    }
    
    public boolean estaDisponible() {
        return this.cliente == null;
    }
}
