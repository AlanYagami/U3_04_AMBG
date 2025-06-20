package com.almacenes.repository;

import com.almacenes.model.Almacen;
import com.almacenes.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    Optional<Almacen> findByClave(String clave);
    List<Almacen> findBySede(Sede sede);
    List<Almacen> findByClienteIsNull();
    List<Almacen> findByClienteIsNotNull();
    
    @Query("SELECT a FROM Almacen a WHERE a.cliente IS NULL")
    List<Almacen> findAlmacenesDisponibles();
    
    @Query("SELECT a FROM Almacen a WHERE a.cliente IS NOT NULL")
    List<Almacen> findAlmacenesOcupados();
}
