package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Habitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstado(String estado);
    List<Habitacion> findByTipo(String tipo);
    Optional<Habitacion> findByNumero(String numero);
    boolean existsByNumero(String numero);

    // @Param obligatorio en Java 25 — sin él lanza IllegalStateException
    @Query("SELECT h FROM Habitacion h LEFT JOIN FETCH h.amenidades WHERE h.id = :id")
    Optional<Habitacion> findByIdConAmenidades(@Param("id") Long id);

    // "Disponibles" = reservables = no están en Mantenimiento.
    // Antes filtraba por estado = 'Disponible', lo que dejaba fuera para
    // SIEMPRE a cualquier habitación marcada 'Ocupada', incluso en fechas
    // libres. La ocupación real por fecha se calcula aparte (ver
    // ReservaRepository.existeReservaActivaHoy y /fechas-ocupadas).
    @Query("SELECT h FROM Habitacion h LEFT JOIN FETCH h.amenidades WHERE h.estado <> 'Mantenimiento'")
    List<Habitacion> findDisponiblesConAmenidades();
}