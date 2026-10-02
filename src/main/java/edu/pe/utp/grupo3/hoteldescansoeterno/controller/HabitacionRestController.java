package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.RangoOcupadoDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Reserva;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.ReservaRepository;

import java.util.List;
import java.util.stream.Collectors;

// Controlador PUBLICO (no va bajo /api/** porque ese prefijo exige ROLE_ADMIN
// en SecurityConfig). Los clientes del sitio necesitan ver la disponibilidad
// sin haber iniciado sesion.
@RestController
@RequestMapping("/habitaciones")
public class HabitacionRestController {

    @Autowired
    private ReservaRepository reservaRepository;

    // Devuelve los rangos de fechas ya reservados (no cancelados/anulados)
    // para pintar el calendario de disponibilidad en el modal de detalle.
    @GetMapping("/{id}/fechas-ocupadas")
    public List<RangoOcupadoDTO> fechasOcupadas(@PathVariable Long id) {
        List<Reserva> activas = reservaRepository.findActivasPorHabitacion(id);
        return activas.stream()
                .map(r -> new RangoOcupadoDTO(r.getFechaEntrada(), r.getFechaSalida()))
                .collect(Collectors.toList());
    }
}