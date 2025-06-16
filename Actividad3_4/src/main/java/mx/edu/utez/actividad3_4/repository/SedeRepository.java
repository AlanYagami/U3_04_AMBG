package mx.edu.utez.actividad3_4.repository;

import mx.edu.utez.actividad3_4.models.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    Optional<Sede> findByClave(String clave);
    List<Sede> findByEstado(String estado);
}