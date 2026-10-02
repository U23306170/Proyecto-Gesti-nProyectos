package edu.pe.utp.grupo3.hoteldescansoeterno.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String userName);
}
