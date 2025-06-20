package com.almacenes.service.impl;

import com.almacenes.dto.ClienteDTO;
import com.almacenes.exception.BusinessException;
import com.almacenes.exception.ResourceNotFoundException;
import com.almacenes.model.Cliente;
import com.almacenes.repository.ClienteRepository;
import com.almacenes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    @Override
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente: {}", clienteDTO);
        
        // Validar que no exista un cliente con el mismo correo
        if (clienteRepository.existsByCorreoElectronico(clienteDTO.getCorreoElectronico())) {
            throw new BusinessException("Ya existe un cliente con el correo electrónico: " + clienteDTO.getCorreoElectronico());
        }
        
        // Validar que no exista un cliente con el mismo teléfono
        if (clienteRepository.existsByTelefono(clienteDTO.getTelefono())) {
            throw new BusinessException("Ya existe un cliente con el teléfono: " + clienteDTO.getTelefono());
        }
        
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirADTO(clienteGuardado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerClientePorId(Long id) {
        log.info("Obteniendo cliente por ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return convertirADTO(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        log.info("Actualizando cliente ID: {} con datos: {}", id, clienteDTO);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        // Validar correo único (excluyendo el cliente actual)
        if (!cliente.getCorreoElectronico().equals(clienteDTO.getCorreoElectronico()) &&
            clienteRepository.existsByCorreoElectronico(clienteDTO.getCorreoElectronico())) {
            throw new BusinessException("Ya existe un cliente con el correo electrónico: " + clienteDTO.getCorreoElectronico());
        }
        
        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
        
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirADTO(clienteActualizado);
    }
    
    @Override
    public void eliminarCliente(Long id) {
        log.info("Eliminando cliente ID: {}", id);
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombreCompleto(cliente.getNombreCompleto());
        dto.setTelefono(cliente.getTelefono());
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        return dto;
    }
}
