package com.almacenes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDTO {
    @NotNull(message = "El ID del almacén es obligatorio")
    private Long almacenId;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El tipo de transacción es obligatorio")
    private String tipoTransaccion; // "COMPRA" o "RENTA"
}
