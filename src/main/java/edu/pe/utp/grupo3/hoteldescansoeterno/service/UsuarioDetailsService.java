package edu.pe.utp.grupo3.hoteldescansoeterno.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pe.utp.grupo3.hoteldescansoeterno.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException(
                    "Usuario desactivado: " + username);
        }

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r.getNombre()))
                .toList();

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),   // hash BCrypt — Spring llama a matches()
                usuario.isActivo(),      // enabled
                true,                    // accountNonExpired
                true,                    // credentialsNonExpired
                true,                    // accountNonLocked
                authorities
        );
    }
}
