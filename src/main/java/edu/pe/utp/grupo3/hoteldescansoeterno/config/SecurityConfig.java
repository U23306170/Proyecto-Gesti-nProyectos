package edu.pe.utp.grupo3.hoteldescansoeterno.config;

import edu.pe.utp.grupo3.hoteldescansoeterno.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Filtro JWT — se agrega a la cadena antes del filtro de formulario
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas — accesibles sin login
                        .requestMatchers("/habitaciones/**","/nosotros","/login", "/css/**", "/js/**", "/img/**", "/").permitAll()
                        // Endpoint publico de login JWT
                        .requestMatchers("/api/auth/**").permitAll()
                        // API REST protegida con JWT - solo ADMIN
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/huespedes/editar/**"
                        ).hasAnyRole("ADMIN","RECEPCIONISTA")
                        .requestMatchers(
                                "/reservas/registrar"
                        ).hasAnyRole("ADMIN","RECEPCIONISTA")
                        .requestMatchers(
                                "/reservas/anular"
                        ).hasRole("ADMIN")

                        // Todo lo demás requiere que el usuario esté autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // URL GET donde vive el formulario de login
                        .loginPage("/login")
                        // URL POST que Spring Security procesa automáticamente
                        // No se necesita @PostMapping en ningún Controller
                        .loginProcessingUrl("/login")
                        // Después de login exitoso → redirigir a /productos
                        .defaultSuccessUrl("/reservas", true)
                        // Si las credenciales fallan → /login?error
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        // Spring procesa el POST /logout automáticamente
                        .logoutUrl("/logout")
                        // Después del logout → /login?logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Usuario logueado sin permiso → página personalizada en lugar de 403 genérico
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                )
                // CSRF desactivado solo para la API REST
                // Las vistas web siguen teniendo CSRF activo via th:action
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // La sesión sigue activa para las vistas Thymeleaf
                // La API REST usa tokens stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // Agregar el filtro JWT ANTES del filtro de username/password
                // Para que si viene un token válido, el usuario ya esté en el SecurityContext
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
