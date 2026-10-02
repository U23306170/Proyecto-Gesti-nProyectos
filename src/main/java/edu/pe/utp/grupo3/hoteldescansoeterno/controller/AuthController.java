package edu.pe.utp.grupo3.hoteldescansoeterno.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import edu.pe.utp.grupo3.hoteldescansoeterno.security.JwtUtil;
import edu.pe.utp.grupo3.hoteldescansoeterno.service.UsuarioDetailsService;

import java.util.List;
import java.util.Map;

/**
 * API de autenticación JWT.
 *
 * POST /api/auth/login  → recibe {username, password} → devuelve {token, username, roles}
 *
 * Este endpoint es público (/api/auth/** → permitAll en SecurityConfig).
 * No requiere token JWT para ser llamado — es el punto de entrada.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // DTO para recibir credenciales (record de Java — constructor + getters automáticos)
    public record LoginRequest(String username, String password) {}

    // DTO para devolver el token
    public record LoginResponse(String token, String username, List<String> roles) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1. Autenticar con AuthenticationManager
            //    Esto llama internamente a UsuarioDetailsService.loadUserByUsername()
            //    y verifica la contraseña con BCryptPasswordEncoder.matches()
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(), request.password()));

            // 2. Cargar el UserDetails para generar el token
            UserDetails userDetails =
                    usuarioDetailsService.loadUserByUsername(request.username());

            // 3. Generar el JWT con username + roles + expiración
            String token = jwtUtil.generarToken(userDetails);

            // 4. Devolver token + info del usuario como JSON
            return ResponseEntity.ok(new LoginResponse(
                    token,
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
            ));

        } catch (BadCredentialsException e) {
            // Credenciales incorrectas → 401 Unauthorized
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}

