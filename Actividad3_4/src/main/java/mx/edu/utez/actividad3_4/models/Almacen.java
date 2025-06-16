package mx.edu.utez.actividad3_4.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
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

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "precio_renta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioRenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tamaño tamaño;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    public enum Tamaño {
        G, M, P
    }

    @PrePersist
    public void init() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDate.now();
        }
        if (this.clave == null && this.sede != null) {
            this.clave = this.sede.getClave() + "-A" + this.id;
        }
    }
}