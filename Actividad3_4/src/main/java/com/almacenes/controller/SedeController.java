package com.almacenes.controller;

import com.almacenes.dto.SedeDTO;
import com.almacenes.service.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SedeController {
    
    private final SedeService sedeService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SedeDTO> crearSede(@Valid @RequestBody SedeDTO sedeDTO) {
        SedeDTO nuevaSede = sedeService.crearSede(sedeDTO);
        return new ResponseEntity<>(nuevaSede, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SedeDTO> obtenerSede(@PathVariable Long id) {
        SedeDTO sede = sedeService.obtenerSedePorId(id);
        return ResponseEntity.ok(sede);
    }
    
    @GetMapping
    public ResponseEntity<List<SedeDTO>> obtenerTodasLasSedes() {
        List<SedeDTO> sedes = sedeService.obtenerTodasLasSedes();
        return ResponseEntity.ok(sedes);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SedeDTO> actualizarSede(@PathVariable Long id, @Valid @RequestBody SedeDTO sedeDTO) {
        SedeDTO sedeActualizada = sedeService.actualizarSede(id, sedeDTO);
        return ResponseEntity.ok(sedeActualizada);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarSede(@PathVariable Long id) {
        sedeService.eliminarSede(id);
        return ResponseEntity.noContent().build();
    }
}
