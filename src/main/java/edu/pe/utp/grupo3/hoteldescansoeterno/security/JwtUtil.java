package edu.pe.utp.grupo3.hoteldescansoeterno.security;


import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad JWT — genera y valida JSON Web Tokens.
 *
 * Flujo:
 *   1. AuthController llama a generarToken(userDetails) → devuelve el JWT
 *   2. JwtAuthenticationFilter llama a extraerUsername(token) y esValido(token, userDetails)
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    // Clave HMAC derivada del string secret de application.yaml
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera el JWT con username, roles e instantes iat/exp.
     * Se llama una sola vez en el login exitoso.
     */
    public String generarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Incluir los roles en el Payload para que el cliente los conozca
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    /** Extrae el username (campo 'sub') del Payload del token. */
    public String extraerUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Verifica que el token sea válido:
     *   - el username coincide con el UserDetails cargado desde la BD
     *   - el token no ha expirado (exp > ahora)
     */
    public boolean esValido(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return username.equals(userDetails.getUsername())
                && !getClaims(token).getExpiration().before(new Date());
    }

    /** Parsea el token y devuelve los Claims (Payload). */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


