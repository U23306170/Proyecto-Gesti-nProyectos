package edu.pe.utp.grupo3.hoteldescansoeterno.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.pe.utp.grupo3.hoteldescansoeterno.service.UsuarioDetailsService;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filtro JWT — se ejecuta exactamente una vez por petición HTTP.
 *
 * Solo exige JWT en la API REST ("/api/**", salvo "/api/auth/**" que es
 * pública). Las vistas Thymeleaf siguen autenticándose por sesión/form-login
 * y no pasan por esta validación estricta.
 *
 * Casos que devuelve la API cuando el JWT es obligatorio:
 *   - Sin header/token           → 403 Forbidden ("Token no proporcionado")
 *   - Token expirado             → 403 Forbidden ("Token expirado")
 *   - Token malformado/firma mala→ 403 Forbidden ("Token inválido")
 *   - Usuario del token no existe→ 403 Forbidden ("Usuario no válido")
 *   - Token válido               → continúa autenticado en el SecurityContext
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_PREFIX       = "/api/";
    private static final String API_AUTH_PREFIX  = "/api/auth/";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    /** Las rutas web (sesión) y el login público no pasan por este filtro. */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean esApi = path.startsWith(API_PREFIX);
        boolean esAuthPublica = path.startsWith(API_AUTH_PREFIX);
        return !esApi || esAuthPublica;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest  request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer el header Authorization — obligatorio en la API REST
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            enviarError(response, request, "Token no proporcionado");
            return;
        }

        // 2. Extraer el token (quitar el prefijo "Bearer ")
        final String token = authHeader.substring(7);
        final String username;

        try {
            username = jwtUtil.extraerUsername(token);
        } catch (ExpiredJwtException e) {
            enviarError(response, request, "Token expirado");
            return;
        } catch (JwtException | IllegalArgumentException e) {
            enviarError(response, request, "Token inválido");
            return;
        }

        // 3. Cargar el usuario del token
        final UserDetails userDetails;
        try {
            userDetails = usuarioDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            enviarError(response, request, "Usuario no válido");
            return;
        }

        // 4. Validar firma, username y expiración
        if (!jwtUtil.esValido(token, userDetails)) {
            enviarError(response, request, "Token inválido");
            return;
        }

        // 5. Crear autenticación y registrarla en el SecurityContext
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /** Respuesta JSON uniforme para cualquier fallo de autenticación JWT. */
    private void enviarError(
            HttpServletResponse response,
            HttpServletRequest  request,
            String              mensaje) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = """
            {"timestamp":"%s","status":%d,"error":"Forbidden","message":"%s","path":"%s"}"""
                .formatted(
                        LocalDateTime.now(),
                        HttpServletResponse.SC_FORBIDDEN,
                        escapar(mensaje),
                        escapar(request.getRequestURI()));

        response.getWriter().write(json);
    }

    /** Escapa comillas y barras invertidas para incrustar texto en JSON literal. */
    private String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

