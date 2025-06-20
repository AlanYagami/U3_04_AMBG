package com.almacenes.controller;

import com.almacenes.dto.AlmacenDTO;
import com.almacenes.dto.TransaccionDTO;
import com.almacenes.service.AlmacenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlmacenController {
    
    private final AlmacenService almacenService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlmacenDTO> crearAlmacen(@Valid @RequestBody AlmacenDTO almacenDTO) {
        AlmacenDTO nuevoAlmacen = almacenService.crearAlmacen(almacenDTO);
        return new ResponseEntity<>(nuevoAlmacen, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerAlmacen(@PathVariable Long id) {
        AlmacenDTO almacen = almacenService.obtenerAlmacenPorId(id);
        return ResponseEntity.ok(almacen);
    }
    
    @GetMapping
    public ResponseEntity<List<AlmacenDTO>> obtenerTodosLosAlmacenes() {
        List<AlmacenDTO> almacenes = almacenService.obtenerTodosLosAlmacenes();
        return ResponseEntity.ok(almacenes);
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenesDisponibles() {
        List<AlmacenDTO> almacenesDisponibles = almacenService.obtenerAlmacenesDisponibles();
        return ResponseEntity.ok(almacenesDisponibles);
    }
    
    @GetMapping("/ocupados")
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenesOcupados() {
        List<AlmacenDTO> almacenesOcupados = almacenService.obtenerAlmacenesOcupados();
        return ResponseEntity.ok(almacenesOcupados);
    }
    
    @PostMapping("/transaccion")
    public ResponseEntity<AlmacenDTO> procesarTransaccion(@Valid @RequestBody TransaccionDTO transaccionDTO) {
        AlmacenDTO almacenTransaccion = almacenService.procesarTransaccion(transaccionDTO);
        return ResponseEntity.ok(almacenTransaccion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlmacenDTO> actualizarAlmacen(@PathVariable Long id, @Valid @RequestBody AlmacenDTO almacenDTO) {
        AlmacenDTO almacenActualizado = almacenService.actualizarAlmacen(id, almacenDTO);
        return ResponseEntity.ok(almacenActualizado);
    }
    
    @PutMapping("/{id}/liberar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlmacenDTO> liberarAlmacen(@PathVariable Long id) {
        AlmacenDTO almacenLiberado = almacenService.liberarAlmacen(id);
        return ResponseEntity.ok(almacenLiberado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        almacenService.eliminarAlmacen(id);
        return ResponseEntity.noContent().build();
    }
}
