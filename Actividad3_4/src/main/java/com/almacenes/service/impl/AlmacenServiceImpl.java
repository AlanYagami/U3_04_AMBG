package com.almacenes.service.impl;

import com.almacenes.dto.AlmacenDTO;
import com.almacenes.dto.TransaccionDTO;
import com.almacenes.exception.BusinessException;
import com.almacenes.exception.ResourceNotFoundException;
import com.almacenes.model.Almacen;
import com.almacenes.model.Cliente;
import com.almacenes.model.Sede;
import com.almacenes.repository.AlmacenRepository;
import com.almacenes.repository.ClienteRepository;
import com.almacenes.repository.SedeRepository;
import com.almacenes.service.AlmacenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlmacenServiceImpl implements AlmacenService {
    
    private final AlmacenRepository almacenRepository;
    private final SedeRepository sedeRepository;
    private final ClienteRepository clienteRepository;
    
    @Override
    public AlmacenDTO crearAlmacen(AlmacenDTO almacenDTO) {
        log.info("Creando nuevo almacén: {}", almacenDTO);
        
        Sede sede = sedeRepository.findById(almacenDTO.getSedeId())
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada con ID: " + almacenDTO.getSedeId()));
        
        Almacen almacen = new Almacen();
        almacen.setPrecioVenta(almacenDTO.getPrecioVenta());
        almacen.setPrecioRenta(almacenDTO.getPrecioRenta());
        almacen.setTamano(almacenDTO.getTamano());
        almacen.setSede(sede);
        almacen.setFechaRegistro(LocalDate.now());
        
        Almacen almacenGuardado = almacenRepository.save(almacen);
        
        // Generar clave después de guardar para tener el ID
        almacenGuardado.setClave(String.format("%s-A%d", sede.getClave(), almacenGuardado.getId()));
        almacenGuardado = almacenRepository.save(almacenGuardado);
        
        return convertirADTO(almacenGuardado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AlmacenDTO obtenerAlmacenPorId(Long id) {
        log.info("Obteniendo almacén por ID: {}", id);
        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado con ID: " + id));
        return convertirADTO(almacen);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> obtenerTodosLosAlmacenes() {
        log.info("Obteniendo todos los almacenes");
        return almacenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> obtenerAlmacenesDisponibles() {
        log.info("Obteniendo almacenes disponibles");
        return almacenRepository.findAlmacenesDisponibles().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> obtenerAlmacenesOcupados() {
        log.info("Obteniendo almacenes ocupados");
        return almacenRepository.findAlmacenesOcupados().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AlmacenDTO procesarTransaccion(TransaccionDTO transaccionDTO) {
        log.info("Procesando transacción: {}", transaccionDTO);
        
        Almacen almacen = almacenRepository.findById(transaccionDTO.getAlmacenId())
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado con ID: " + transaccionDTO.getAlmacenId()));
        
        Cliente cliente = clienteRepository.findById(transaccionDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + transaccionDTO.getClienteId()));
        
        if (!almacen.estaDisponible()) {
            throw new BusinessException("El almacén ya está ocupado y no puede ser comprado o rentado");
        }
        
        if (!"COMPRA".equals(transaccionDTO.getTipoTransaccion()) && !"RENTA".equals(transaccionDTO.getTipoTransaccion())) {
            throw new BusinessException("Tipo de transacción inválido. Debe ser 'COMPRA' o 'RENTA'");
        }
        
        almacen.setCliente(cliente);
        Almacen almacenActualizado = almacenRepository.save(almacen);
        
        log.info("Transacción procesada exitosamente. Almacén {} {} por cliente {}", 
                almacen.getClave(), 
                transaccionDTO.getTipoTransaccion().toLowerCase(), 
                cliente.getNombreCompleto());
        
        return convertirADTO(almacenActualizado);
    }
    
    @Override
    public AlmacenDTO liberarAlmacen(Long almacenId) {
        log.info("Liberando almacén ID: {}", almacenId);
        
        Almacen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado con ID: " + almacenId));
        
        if (almacen.estaDisponible()) {
            throw new BusinessException("El almacén ya está disponible");
        }
        
        almacen.setCliente(null);
        Almacen almacenLiberado = almacenRepository.save(almacen);
        
        return convertirADTO(almacenLiberado);
    }
    
    @Override
    public void eliminarAlmacen(Long id) {
        log.info("Eliminando almacén ID: {}", id);
        
        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado con ID: " + id));
        
        if (!almacen.estaDisponible()) {
            throw new BusinessException("No se puede eliminar un almacén que está ocupado. Primero debe liberarlo.");
        }
        
        almacenRepository.deleteById(id);
    }

    @Override
    public AlmacenDTO actualizarAlmacen(Long id, AlmacenDTO almacenDTO) {
        log.info("Actualizando almacén ID: {} con datos: {}", id, almacenDTO);

        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado con ID: " + id));

        // Verificar si se está cambiando la sede
        if (!almacen.getSede().getId().equals(almacenDTO.getSedeId())) {
            Sede nuevaSede = sedeRepository.findById(almacenDTO.getSedeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada con ID: " + almacenDTO.getSedeId()));
            almacen.setSede(nuevaSede);

            // Regenerar clave si cambió la sede
            almacen.setClave(String.format("%s-A%d", nuevaSede.getClave(), almacen.getId()));
        }

        almacen.setPrecioVenta(almacenDTO.getPrecioVenta());
        almacen.setPrecioRenta(almacenDTO.getPrecioRenta());
        almacen.setTamano(almacenDTO.getTamano());

        Almacen almacenActualizado = almacenRepository.save(almacen);
        return convertirADTO(almacenActualizado);
    }
    
    private AlmacenDTO convertirADTO(Almacen almacen) {
        AlmacenDTO dto = new AlmacenDTO();
        dto.setId(almacen.getId());
        dto.setClave(almacen.getClave());
        dto.setFechaRegistro(almacen.getFechaRegistro());
        dto.setPrecioVenta(almacen.getPrecioVenta());
        dto.setPrecioRenta(almacen.getPrecioRenta());
        dto.setTamano(almacen.getTamano());
        dto.setSedeId(almacen.getSede().getId());
        dto.setSedeEstado(almacen.getSede().getEstado());
        dto.setSedeMunicipio(almacen.getSede().getMunicipio());
        dto.setDisponible(almacen.estaDisponible());
        
        if (almacen.getCliente() != null) {
            dto.setClienteId(almacen.getCliente().getId());
            dto.setClienteNombre(almacen.getCliente().getNombreCompleto());
        }
        
        return dto;
    }
}
