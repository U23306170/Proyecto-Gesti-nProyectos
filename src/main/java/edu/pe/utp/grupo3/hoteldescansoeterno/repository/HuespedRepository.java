package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Huesped;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    Optional<Huesped> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByDniAndIdNot(String dni, Long id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByTelefono(String telefono);
    boolean existsByTelefonoAndIdNot(String telefono, Long id);
    List<Huesped> findByEstado(String estado);
    List<Huesped> findByNombreContainingIgnoreCase(String nombre);
    @Query("""
        SELECT h
        FROM Huesped h
        WHERE LOWER(h.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(h.apellidoPaterno) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(h.apellidoMaterno) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Huesped> buscarPorNombreCompleto(@Param("texto") String texto);
}