package com.almacenes.service;

import com.almacenes.dto.SedeDTO;
import java.util.List;

public interface SedeService {
    SedeDTO crearSede(SedeDTO sedeDTO);
    SedeDTO obtenerSedePorId(Long id);
    List<SedeDTO> obtenerTodasLasSedes();
    SedeDTO actualizarSede(Long id, SedeDTO sedeDTO);
    void eliminarSede(Long id);
}
