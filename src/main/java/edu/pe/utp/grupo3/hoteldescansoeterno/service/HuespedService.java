package edu.pe.utp.grupo3.hoteldescansoeterno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HuespedDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.RecursoNoEncontradoException;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.ReglaNegocioException;
import edu.pe.utp.grupo3.hoteldescansoeterno.mapper.HotelMapper;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Huesped;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.HuespedRepository;

import java.util.List;

@Service
public class HuespedService {

    @Autowired private HuespedRepository repository;
    @Autowired private HotelMapper       mapper;

    @Transactional(readOnly = true)
    public List<HuespedDTO> listarTodos() {
        return mapper.toDTOListHues(repository.findAll());
    }

    @Transactional(readOnly = true)
    public HuespedDTO buscarPorId(Long id) {
        return mapper.toDTO(repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Huésped no encontrado: " + id)));
    }

    @Transactional
    public void registrar(HuespedDTO dto) {
        // Tercera capa: regla de negocio DNI duplicado
        if (repository.existsByDni(dto.getDni())) {
            throw new ReglaNegocioException(
                "Ya existe un huésped con DNI: " + dto.getDni());
        }
        // Normalizar campos opcionales: "" → null para evitar problemas en BD
        if (dto.getEmail()    != null && dto.getEmail().isBlank())    dto.setEmail(null);
        if (dto.getTelefono() != null && dto.getTelefono().isBlank()) dto.setTelefono(null);

        // Regla de negocio: correo duplicado
        if (dto.getEmail() != null && repository.existsByEmail(dto.getEmail())) {
            throw new ReglaNegocioException(
                "Ya existe un huésped registrado con el correo: " + dto.getEmail());
        }
        // Regla de negocio: teléfono duplicado
        if (dto.getTelefono() != null && repository.existsByTelefono(dto.getTelefono())) {
            throw new ReglaNegocioException(
                "Ya existe un huésped registrado con el teléfono: " + dto.getTelefono());
        }

        repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public void editar(HuespedDTO dto) {

        Huesped huesped = repository.findById(dto.getId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El huésped no existe."));

        // Validar DNI duplicado
        if (repository.existsByDniAndIdNot(
                dto.getDni(),
                dto.getId())) {

            throw new ReglaNegocioException(
                    "El DNI ya está registrado por otro huésped.");
        }

        // Normalizar campos opcionales: "" → null
        if (dto.getEmail()    != null && dto.getEmail().isBlank())    dto.setEmail(null);
        if (dto.getTelefono() != null && dto.getTelefono().isBlank()) dto.setTelefono(null);

        // Validar correo duplicado (excluyendo al propio huésped)
        if (dto.getEmail() != null
                && repository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new ReglaNegocioException(
                    "El correo ya está registrado por otro huésped.");
        }

        // Validar teléfono duplicado (excluyendo al propio huésped)
        if (dto.getTelefono() != null
                && repository.existsByTelefonoAndIdNot(dto.getTelefono(), dto.getId())) {
            throw new ReglaNegocioException(
                    "El teléfono ya está registrado por otro huésped.");
        }

        huesped.setNombre(dto.getNombre());
        huesped.setApellidoPaterno(dto.getApellidoPaterno());
        huesped.setApellidoMaterno(dto.getApellidoMaterno());
        huesped.setDni(dto.getDni());
        huesped.setTelefono(dto.getTelefono());
        huesped.setEmail(dto.getEmail());

        repository.save(huesped);
    }

    @Transactional
    public void actualizar(HuespedDTO dto) {
        repository.findById(dto.getId())
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Huésped no encontrado: " + dto.getId()));
        if (dto.getEmail()    != null && dto.getEmail().isBlank())    dto.setEmail(null);
        if (dto.getTelefono() != null && dto.getTelefono().isBlank()) dto.setTelefono(null);
        repository.save(mapper.toEntity(dto));
    }
}