package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Amenidad;

import java.util.Optional;

@Repository
public interface AmenidadRepository extends JpaRepository<Amenidad, Long> {
    Optional<Amenidad> findByNombre(String nombre);
}
