package com.almacenes.service;

import com.almacenes.dto.AlmacenDTO;
import com.almacenes.dto.TransaccionDTO;
import java.util.List;

public interface AlmacenService {
    AlmacenDTO crearAlmacen(AlmacenDTO almacenDTO);
    AlmacenDTO obtenerAlmacenPorId(Long id);
    List<AlmacenDTO> obtenerTodosLosAlmacenes();
    List<AlmacenDTO> obtenerAlmacenesDisponibles();
    List<AlmacenDTO> obtenerAlmacenesOcupados();
    AlmacenDTO procesarTransaccion(TransaccionDTO transaccionDTO);
    AlmacenDTO liberarAlmacen(Long almacenId);
    AlmacenDTO actualizarAlmacen(Long id, AlmacenDTO almacenDTO);
    void eliminarAlmacen(Long id);
}
