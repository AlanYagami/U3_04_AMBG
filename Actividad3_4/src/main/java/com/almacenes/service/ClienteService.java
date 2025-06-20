package com.almacenes.service;

import com.almacenes.dto.ClienteDTO;
import java.util.List;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    ClienteDTO obtenerClientePorId(Long id);
    List<ClienteDTO> obtenerTodosLosClientes();
    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);
    void eliminarCliente(Long id);
}
