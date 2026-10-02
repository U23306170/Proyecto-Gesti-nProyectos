package edu.pe.utp.grupo3.hoteldescansoeterno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.ReservaDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.RecursoNoEncontradoException;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.ReglaNegocioException;
import edu.pe.utp.grupo3.hoteldescansoeterno.mapper.HotelMapper;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Habitacion;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Huesped;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Reserva;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservaService {

    @Autowired private ReservaRepository   reservaRepository;
    @Autowired private HuespedRepository   huespedRepository;
    @Autowired private HabitacionRepository habitacionRepository;
    @Autowired private HotelMapper         mapper;

    @Transactional(readOnly = true)
    public List<ReservaDTO> listarTodas() {
        return mapper.toDTOListRes(reservaRepository.findAllConRelaciones());
    }

    @Transactional(readOnly = true)
    public ReservaDTO buscarPorId(Long id) {
        return mapper.toDTO(reservaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Reserva no encontrada: " + id)));
    }

    @Transactional
    public void registrar(ReservaDTO dto) {
        Huesped huesped = huespedRepository.findById(dto.getHuespedId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Huésped no encontrado"));

        Habitacion habitacion = habitacionRepository.findById(dto.getHabitacionId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Habitación no encontrada"));

        // Regla de negocio: habitación debe estar disponible
        if (!habitacion.isDisponible()) {
            throw new ReglaNegocioException(
                "La habitación " + habitacion.getNumero()
                + " no está disponible en este momento.");
        }

        // Regla de negocio: no fechas pasadas
        LocalDate hoy = LocalDate.now();
        if (dto.getFechaEntrada().isBefore(hoy)) {
            throw new ReglaNegocioException(
                "La fecha de entrada no puede ser anterior a hoy.");
        }
        if (dto.getFechaSalida().isBefore(hoy)) {
            throw new ReglaNegocioException(
                "La fecha de salida no puede ser anterior a hoy.");
        }

        // Regla de negocio: fechas válidas
        if (!dto.getFechaSalida().isAfter(dto.getFechaEntrada())) {
            throw new ReglaNegocioException(
                "La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        // Regla de negocio: sin conflicto de fechas
        if (reservaRepository.existeConflictoFechas(
                habitacion.getId(),
                dto.getFechaEntrada(),
                dto.getFechaSalida())) {
            throw new ReglaNegocioException(
                "La habitación " + habitacion.getNumero()
                + " ya tiene una reserva en esas fechas.");
        }

        Reserva reserva = new Reserva(
            huesped, habitacion,
            dto.getFechaEntrada(), dto.getFechaSalida(),
            dto.getNoches(), dto.getTotal(),
            "Pendiente",
            dto.getObservaciones() != null ? dto.getObservaciones() : ""
        );
        reservaRepository.save(reserva);

        // Nota: NO marcamos la habitación como "Ocupada" aquí.
        // El campo "estado" de Habitacion representa disponibilidad
        // administrativa (Disponible / Mantenimiento), no la ocupación
        // de fechas puntuales. La disponibilidad por fecha ya se valida
        // arriba con existeConflictoFechas() y se muestra en el
        // calendario (endpoint /habitaciones/{id}/fechas-ocupadas).
        // Así, una habitación con reservas futuras sigue apareciendo
        // como "Disponible" para otras fechas libres.
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Reserva no encontrada: " + id));

        if (reserva.isAnulada()) {
            throw new ReglaNegocioException(
                "La reserva #" + id + " ya está anulada.");
        }

        reserva.setEstado("Anulada");
        if (motivo != null && !motivo.isBlank()) {
            reserva.setObservaciones("Anulada: " + motivo);
        }
        reservaRepository.save(reserva);

        // Liberar habitación
        Habitacion hab = reserva.getHabitacion();
        hab.setEstado("Disponible");
        habitacionRepository.save(hab);
    }

    @Transactional
    public void editar(Long id, String estado, LocalDate fechaEntrada, LocalDate fechaSalida) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Reserva no encontrada: " + id));

        if (reserva.isAnulada()) {
            throw new ReglaNegocioException(
                "No se puede editar una reserva anulada.");
        }

        if (!"Pendiente".equals(estado) && !"Confirmada".equals(estado)) {
            throw new ReglaNegocioException(
                "Solo se permite el estado Pendiente o Confirmada.");
        }

        LocalDate hoy = LocalDate.now();
        if (fechaEntrada.isBefore(hoy)) {
            throw new ReglaNegocioException(
                "La fecha de entrada no puede ser anterior a hoy.");
        }
        if (fechaSalida.isBefore(hoy)) {
            throw new ReglaNegocioException(
                "La fecha de salida no puede ser anterior a hoy.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new ReglaNegocioException(
                "La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        Habitacion habitacion = reserva.getHabitacion();
        if (reservaRepository.existeConflictoFechasExcluyendo(
                habitacion.getId(), id, fechaEntrada, fechaSalida)) {
            throw new ReglaNegocioException(
                "La habitación " + habitacion.getNumero()
                + " ya tiene otra reserva en esas fechas.");
        }

        int noches = (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        double total = noches * habitacion.getPrecio();

        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setNoches(noches);
        reserva.setTotal(total);
        reserva.setEstado(estado);
        reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public long contarActivas() {
        return reservaRepository.findAll().stream()
            .filter(r -> !r.isAnulada()).count();
    }
}
