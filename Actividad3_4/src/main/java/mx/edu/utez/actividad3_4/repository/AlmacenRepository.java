package mx.edu.utez.actividad3_4.repository;

import mx.edu.utez.actividad3_4.models.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    Optional<Almacen> findByClave(String clave);
    List<Almacen> findBySedeId(Long sedeId);
    List<Almacen> findByTamaño(Almacen.Tamaño tamaño);
}