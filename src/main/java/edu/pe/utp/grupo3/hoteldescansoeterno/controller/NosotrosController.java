package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NosotrosController {

    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("activePage", "nosotros");
        return "nosotros";
    }
}