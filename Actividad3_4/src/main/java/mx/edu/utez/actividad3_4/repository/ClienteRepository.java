package mx.edu.utez.actividad3_4.repository;
import mx.edu.utez.actividad3_4.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreoElectronico(String correo);
}
