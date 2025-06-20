package com.almacenes.service.impl;

import com.almacenes.dto.SedeDTO;
import com.almacenes.exception.ResourceNotFoundException;
import com.almacenes.model.Sede;
import com.almacenes.repository.SedeRepository;
import com.almacenes.service.SedeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SedeServiceImpl implements SedeService {
    
    private final SedeRepository sedeRepository;
    
    @Override
    public SedeDTO crearSede(SedeDTO sedeDTO) {
        log.info("Creando nueva sede: {}", sedeDTO);
        
        Sede sede = new Sede();
        sede.setEstado(sedeDTO.getEstado());
        sede.setMunicipio(sedeDTO.getMunicipio());
        
        Sede sedeGuardada = sedeRepository.save(sede);
        
        // Generar clave después de guardar para tener el ID
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String digitosAleatorios = String.format("%04d", new Random().nextInt(10000));
        sedeGuardada.setClave(String.format("C%d-%s-%s", sedeGuardada.getId(), fecha, digitosAleatorios));
        
        sedeGuardada = sedeRepository.save(sedeGuardada);
        
        return convertirADTO(sedeGuardada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SedeDTO obtenerSedePorId(Long id) {
        log.info("Obteniendo sede por ID: {}", id);
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada con ID: " + id));
        return convertirADTO(sede);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SedeDTO> obtenerTodasLasSedes() {
        log.info("Obteniendo todas las sedes");
        return sedeRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SedeDTO actualizarSede(Long id, SedeDTO sedeDTO) {
        log.info("Actualizando sede ID: {} con datos: {}", id, sedeDTO);
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada con ID: " + id));
        
        sede.setEstado(sedeDTO.getEstado());
        sede.setMunicipio(sedeDTO.getMunicipio());
        
        Sede sedeActualizada = sedeRepository.save(sede);
        return convertirADTO(sedeActualizada);
    }
    
    @Override
    public void eliminarSede(Long id) {
        log.info("Eliminando sede ID: {}", id);
        if (!sedeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede no encontrada con ID: " + id);
        }
        sedeRepository.deleteById(id);
    }
    
    private SedeDTO convertirADTO(Sede sede) {
        SedeDTO dto = new SedeDTO();
        dto.setId(sede.getId());
        dto.setClave(sede.getClave());
        dto.setEstado(sede.getEstado());
        dto.setMunicipio(sede.getMunicipio());
        return dto;
    }
}
