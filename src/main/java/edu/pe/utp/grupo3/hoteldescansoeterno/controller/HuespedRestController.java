package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HuespedDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.HuespedService;


@RestController
@RequestMapping("/api/huespedes")
public class HuespedRestController {

    @Autowired
    private HuespedService huespedService;

    

    @GetMapping("/{id}")
    public HuespedDTO buscarPorId(@PathVariable Long id) {
        return huespedService.buscarPorId(id);
    }
}