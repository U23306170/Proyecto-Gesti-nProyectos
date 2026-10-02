package edu.pe.utp.grupo3.hoteldescansoeterno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Rol;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Usuario;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.RolRepository;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.UsuarioRepository;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) return;

        // Crear los roles — prefijo ROLE_ es obligatorio
        Rol rolAdmin = rolRepository.save(new Rol("ROLE_ADMIN"));
        Rol rolRecepcionista  = rolRepository.save(new Rol("ROLE_RECEPCIONISTA"));

        // Usuario administrador
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        // encode() hashea con BCrypt — generate diferente cada vez
        admin.setPassword(passwordEncoder.encode("administrador26+"));
        admin.setActivo(true);
        // Tiene ambos roles — semana 14 aprovecha esto para autorización
        admin.setRoles(List.of(rolAdmin, rolRecepcionista));
        usuarioRepository.save(admin);

        // Usuario recepcionista — solo ROLE_RECEPCIONISTA
        Usuario vendedor = new Usuario();
        vendedor.setUsername("charly");
        vendedor.setPassword(passwordEncoder.encode("charlyza+26"));
        vendedor.setActivo(true);
        vendedor.setRoles(List.of(rolRecepcionista));
        usuarioRepository.save(vendedor);

        System.out.println(">> Usuarios: admin/administrador26+ — charly/charlyza+26");
    }


}
