package com.almacenes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeDTO {
    private Long id;
    private String clave;
    
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    
    @NotBlank(message = "El municipio es obligatorio")
    private String municipio;
}
