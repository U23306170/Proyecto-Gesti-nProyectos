package edu.pe.utp.grupo3.hoteldescansoeterno.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Muestra el formulario de login.
     *
     * Solo se necesita el GET — Spring Security procesa
     * el POST /login automáticamente usando:
     *   1. UsernamePasswordAuthenticationFilter
     *   2. UsuarioDetailsService.loadUserByUsername()
     *   3. BCryptPasswordEncoder.matches()
     *
     * No hay ningún @PostMapping("/login") necesario.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Se muestra cuando el usuario está logueado pero
    // intenta acceder a una ruta sin el rol necesario
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "error/403";
    }
}