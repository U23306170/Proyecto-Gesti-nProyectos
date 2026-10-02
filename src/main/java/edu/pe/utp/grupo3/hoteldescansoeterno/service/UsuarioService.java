package edu.pe.utp.grupo3.hoteldescansoeterno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pe.utp.grupo3.hoteldescansoeterno.repository.UsuarioRepository;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
}
