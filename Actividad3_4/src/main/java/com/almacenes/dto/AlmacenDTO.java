package com.almacenes.dto;

import com.almacenes.model.TamanoAlmacen;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlmacenDTO {
    private Long id;
    private String clave;
    private LocalDate fechaRegistro;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    private Double precioVenta;
    
    @NotNull(message = "El precio de renta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de renta debe ser mayor a 0")
    private Double precioRenta;
    
    @NotNull(message = "El tamaño es obligatorio")
    private TamanoAlmacen tamano;
    
    @NotNull(message = "La sede es obligatoria")
    private Long sedeId;
    
    private Long clienteId;
    private String sedeEstado;
    private String sedeMunicipio;
    private String clienteNombre;
    private boolean disponible;
}
