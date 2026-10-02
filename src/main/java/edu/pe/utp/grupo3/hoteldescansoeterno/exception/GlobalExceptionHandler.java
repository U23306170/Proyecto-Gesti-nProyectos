package edu.pe.utp.grupo3.hoteldescansoeterno.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Manejador global de excepciones de negocio.
 * Captura excepciones lanzadas desde cualquier @Controller
 * y las convierte en mensajes flash para el usuario.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String handleRecursoNoEncontrado(
            RecursoNoEncontradoException e,
            RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public String handleReglaNegocio(
            ReglaNegocioException e,
            RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        return "redirect:/reservas";
    }
}
