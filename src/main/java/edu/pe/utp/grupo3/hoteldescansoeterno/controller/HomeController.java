package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HabitacionDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HabitacionService;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HuespedService;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.ReservaService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired private HabitacionService habitacionService;
    @Autowired private HuespedService    huespedService;
    @Autowired private ReservaService    reservaService;

    @GetMapping("/")
    public String index(Model model) {
        List<HabitacionDTO> habitaciones = habitacionService.listarTodas();
        List<HabitacionDTO> disponibles  = habitacionService.listarDisponibles();

        Map<String, Long> disponiblesPorTipo = habitaciones.stream()
            .filter(HabitacionDTO::isDisponible)
            .collect(Collectors.groupingBy(HabitacionDTO::getTipo, Collectors.counting()));

        Map<String, Long> totalPorTipo = habitaciones.stream()
            .collect(Collectors.groupingBy(HabitacionDTO::getTipo, Collectors.counting()));

        model.addAttribute("activePage",              "home");
        model.addAttribute("habitaciones",            habitaciones);
        model.addAttribute("huespedes",               huespedService.listarTodos());
        model.addAttribute("habitacionesDisponibles", disponibles);
        model.addAttribute("habitacionesLibres",      disponibles.size());
        model.addAttribute("totalHabitaciones",       habitaciones.size());
        model.addAttribute("disponiblesPorTipo",      disponiblesPorTipo);
        model.addAttribute("totalPorTipo",            totalPorTipo);
        model.addAttribute("totalHuespedes",          huespedService.listarTodos().size());
        model.addAttribute("reservasActivas",         reservaService.contarActivas());
        return "index";
    }
}
