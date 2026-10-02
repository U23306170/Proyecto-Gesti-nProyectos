package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
