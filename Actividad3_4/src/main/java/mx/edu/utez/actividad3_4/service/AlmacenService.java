package mx.edu.utez.actividad3_4.service;

import mx.edu.utez.actividad3_4.models.Almacen;
import mx.edu.utez.actividad3_4.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private ValidationService validationService;

    public List<Almacen> findAll() {
        return almacenRepository.findAll();
    }

    public Optional<Almacen> findById(Long id) {
        return almacenRepository.findById(id);
    }

    public Almacen save(Almacen almacen) {
        // Input validation
        if (almacen.getPrecioVenta() == null || almacen.getPrecioVenta().compareTo(java.math.BigDecimal.ZERO) <= 0 ||
                almacen.getPrecioRenta() == null || almacen.getPrecioRenta().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precios inválidos");
        }
        return almacenRepository.save(almacen);
    }

    public void deleteById(Long id) {
        almacenRepository.deleteById(id);
    }
}