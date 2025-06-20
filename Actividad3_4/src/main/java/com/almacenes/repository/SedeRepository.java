package com.almacenes.repository;

import com.almacenes.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    Optional<Sede> findByClave(String clave);
    boolean existsByClave(String clave);
}
