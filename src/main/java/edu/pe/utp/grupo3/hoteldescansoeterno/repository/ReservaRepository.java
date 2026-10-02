package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Reserva;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(String estado);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.huesped JOIN FETCH r.habitacion ORDER BY r.id DESC")
    List<Reserva> findAllConRelaciones();

    @Query("SELECT r FROM Reserva r JOIN FETCH r.huesped h JOIN FETCH r.habitacion WHERE h.id = :huespedId")
    List<Reserva> findByHuespedId(@Param("huespedId") Long huespedId);

    // Verifica conflicto de fechas para una habitación (evita doble reserva)
    @Query("SELECT COUNT(r) > 0 FROM Reserva r " +
            "WHERE r.habitacion.id = :habId " +
            "AND r.estado NOT IN ('Cancelada', 'Anulada') " +
            "AND r.fechaEntrada < :salida " +
            "AND r.fechaSalida > :entrada")
    boolean existeConflictoFechas(
            @Param("habId")    Long habId,
            @Param("entrada")  LocalDate entrada,
            @Param("salida")   LocalDate salida);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r " +
            "WHERE r.habitacion.id = :habId " +
            "AND r.id != :reservaId " +
            "AND r.estado NOT IN ('Cancelada', 'Anulada') " +
            "AND r.fechaEntrada < :salida " +
            "AND r.fechaSalida > :entrada")
    boolean existeConflictoFechasExcluyendo(
            @Param("habId")      Long habId,
            @Param("reservaId")  Long reservaId,
            @Param("entrada")    LocalDate entrada,
            @Param("salida")     LocalDate salida);

    long countByEstado(String estado);

    // Indica si una habitación tiene, en este momento, una reserva activa
    // que cubre la fecha de hoy (fechaEntrada <= hoy < fechaSalida).
    // Se usa para mostrar "Ocupada hoy" en la tarjeta pública sin bloquear
    // la habitación para fechas futuras.
    @Query("SELECT COUNT(r) > 0 FROM Reserva r " +
            "WHERE r.habitacion.id = :habId " +
            "AND r.estado NOT IN ('Cancelada', 'Anulada') " +
            "AND r.fechaEntrada <= :hoy AND r.fechaSalida > :hoy")
    boolean existeReservaActivaHoy(
            @Param("habId") Long habId,
            @Param("hoy")   LocalDate hoy);

    // Reservas activas (no canceladas/anuladas) de una habitación — usado para
    // pintar el calendario de disponibilidad en la vista pública
    @Query("SELECT r FROM Reserva r " +
            "WHERE r.habitacion.id = :habId " +
            "AND r.estado NOT IN ('Cancelada', 'Anulada') " +
            "ORDER BY r.fechaEntrada")
    List<Reserva> findActivasPorHabitacion(@Param("habId") Long habId);
}