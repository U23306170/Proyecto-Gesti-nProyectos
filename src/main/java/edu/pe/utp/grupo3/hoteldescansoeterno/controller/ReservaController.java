package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HabitacionDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.dto.ReservaDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HabitacionService;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HuespedService;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.ReservaService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired private ReservaService    reservaService;
    @Autowired private HuespedService    huespedService;
    @Autowired private HabitacionService habitacionService;

    @GetMapping
    public String reservas(Model model) {
        model.addAttribute("activePage",              "reservas");
        model.addAttribute("reservas",                reservaService.listarTodas());
        model.addAttribute("huespedes",               huespedService.listarTodos());
        model.addAttribute("habitacionesDisponibles", habitacionService.listarDisponibles());
        return "reservas";
    }

    @PostMapping("/registrar")
    public String registrarReserva(
            @Valid @ModelAttribute ReservaDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            String errores = result.getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(". "));
            redirectAttrs.addFlashAttribute("mensajeError", errores);
            return "redirect:/reservas";
        }

        if (dto.getFechaEntrada() != null && dto.getFechaSalida() != null) {
            int noches = (int) ChronoUnit.DAYS.between(
                dto.getFechaEntrada(), dto.getFechaSalida());
            dto.setNoches(noches);

            HabitacionDTO hab = habitacionService.buscarPorId(dto.getHabitacionId());
            dto.setTotal(noches * hab.getPrecio());
        }

        dto.setEstado("Pendiente");

        reservaService.registrar(dto);
        redirectAttrs.addFlashAttribute("mensajeExito",
            "Reserva registrada correctamente.");
        return "redirect:/reservas";
    }

    @PostMapping("/anular")
    public String cancelarReserva(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "motivo", required = false, defaultValue = "") String motivo,
            RedirectAttributes redirectAttrs) {

        reservaService.cancelar(id, motivo);
        redirectAttrs.addFlashAttribute("mensajeExito",
            "Reserva #" + id + " anulada.");
        return "redirect:/reservas";
    }

    @PostMapping("/editar")
    public String editarReserva(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "estado") String estado,
            @RequestParam(name = "fechaEntrada")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam(name = "fechaSalida")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            RedirectAttributes redirectAttrs) {

        if (estado == null || estado.isBlank()) {
            redirectAttrs.addFlashAttribute("mensajeError",
                "Debes seleccionar un estado.");
            return "redirect:/reservas";
        }
        if (fechaEntrada == null || fechaSalida == null) {
            redirectAttrs.addFlashAttribute("mensajeError",
                "Las fechas de entrada y salida son obligatorias.");
            return "redirect:/reservas";
        }

        reservaService.editar(id, estado, fechaEntrada, fechaSalida);
        redirectAttrs.addFlashAttribute("mensajeExito",
            "Reserva actualizada correctamente.");
        return "redirect:/reservas";
    }
}
