package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HuespedDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HuespedService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/huespedes")
public class HuespedController {

    @Autowired private HuespedService huespedService;

    @GetMapping
    public String huespedes(Model model) {
        model.addAttribute("activePage", "huespedes");
        model.addAttribute("huespedes",  huespedService.listarTodos());
        return "huespedes";
    }

    @PostMapping("/registrar")
    public String registrarHuesped(
            @Valid @ModelAttribute HuespedDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            String errores = result.getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(". "));
            redirectAttrs.addFlashAttribute("mensajeError", errores);
            return "redirect:/huespedes";
        }

        dto.setEstado("Activo");

        try {
            huespedService.registrar(dto);
            redirectAttrs.addFlashAttribute("mensajeExito",
                "Huésped '" + dto.getNombre() + "' registrado correctamente.");
        } catch (RuntimeException ex) {
            redirectAttrs.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/huespedes";
    }

    @PostMapping("/editar")
    public String editarHuesped(
            @Valid @ModelAttribute HuespedDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            String errores = result.getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(". "));
            redirectAttrs.addFlashAttribute("mensajeError", errores);
            return "redirect:/huespedes";
        }

        try {
            huespedService.editar(dto);
            redirectAttrs.addFlashAttribute("mensajeExito",
                "Huésped '" + dto.getNombre() + "' actualizado correctamente.");
        } catch (RuntimeException ex) {
            redirectAttrs.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/huespedes";
    }
}