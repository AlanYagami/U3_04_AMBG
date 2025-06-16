package mx.edu.utez.actividad3_4.service;

import mx.edu.utez.actividad3_4.models.Sede;
import mx.edu.utez.actividad3_4.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private ValidationService validationService;

    public List<Sede> findAll() {
        return sedeRepository.findAll();
    }

    public Optional<Sede> findById(Long id) {
        return sedeRepository.findById(id);
    }

    public Sede save(Sede sede) {
        // Input validation
        if (!validationService.isValidString(sede.getEstado(), 2, 100) ||
                !validationService.isValidString(sede.getMunicipio(), 2, 100)) {
            throw new IllegalArgumentException("Datos inválidos");
        }
        return sedeRepository.save(sede);
    }

    public void deleteById(Long id) {
        sedeRepository.deleteById(id);
    }
}

