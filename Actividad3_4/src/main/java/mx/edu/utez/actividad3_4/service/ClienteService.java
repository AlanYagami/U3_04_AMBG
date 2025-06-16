package mx.edu.utez.actividad3_4.service;

import mx.edu.utez.actividad3_4.models.Cliente;
import mx.edu.utez.actividad3_4.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ValidationService validationService;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        // Input validation
        if (!validationService.isValidString(cliente.getNombreCompleto(), 2, 200) ||
                !validationService.isValidEmail(cliente.getCorreoElectronico()) ||
                !validationService.isValidPhone(cliente.getNumeroTelefono())) {
            throw new IllegalArgumentException("Datos del cliente inválidos");
        }
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}