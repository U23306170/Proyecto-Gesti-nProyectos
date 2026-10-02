package edu.pe.utp.grupo3.hoteldescansoeterno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.HabitacionDTO;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.RecursoNoEncontradoException;
import edu.pe.utp.grupo3.hoteldescansoeterno.exception.ReglaNegocioException;
import edu.pe.utp.grupo3.hoteldescansoeterno.mapper.HotelMapper;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Amenidad;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.Habitacion;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.AmenidadRepository;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.HabitacionRepository;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.ReservaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitacionService {

    @Autowired private HabitacionRepository habitacionRepository;
    @Autowired private AmenidadRepository   amenidadRepository;
    @Autowired private ReservaRepository    reservaRepository;
    @Autowired private HotelMapper          mapper;

    @Transactional(readOnly = true)
    public List<HabitacionDTO> listarTodas() {
        List<HabitacionDTO> dtos = mapper.toDTOListHab(habitacionRepository.findAll());
        marcarOcupadasHoy(dtos);
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<HabitacionDTO> listarDisponibles() {
        List<HabitacionDTO> dtos = mapper.toDTOListHab(
                habitacionRepository.findDisponiblesConAmenidades());
        marcarOcupadasHoy(dtos);
        return dtos;
    }

    @Transactional(readOnly = true)
    public HabitacionDTO buscarPorId(Long id) {
        HabitacionDTO dto = mapper.toDTO(
                habitacionRepository.findByIdConAmenidades(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Habitación no encontrada: " + id)));
        dto.setOcupadaHoy(reservaRepository.existeReservaActivaHoy(dto.getId(), LocalDate.now()));
        return dto;
    }


    private void marcarOcupadasHoy(List<HabitacionDTO> dtos) {
        LocalDate hoy = LocalDate.now();
        for (HabitacionDTO dto : dtos) {
            dto.setOcupadaHoy(reservaRepository.existeReservaActivaHoy(dto.getId(), hoy));
        }
    }

    @Transactional
    public void guardar(HabitacionDTO dto) {
        if (dto.getId() == null
                && habitacionRepository.existsByNumero(dto.getNumero())) {
            throw new ReglaNegocioException(
                    "Ya existe una habitación con el número: " + dto.getNumero());
        }
        Habitacion entity = mapper.toEntity(dto);
        if (dto.getAmenidades() != null) {
            List<Amenidad> amenidades = dto.getAmenidades().stream()
                    .filter(n -> n != null && !n.isBlank())
                    .map(nombre -> amenidadRepository.findByNombre(nombre)
                            .orElseGet(() -> amenidadRepository.save(new Amenidad(nombre))))
                    .toList();
            entity.setAmenidades(amenidades);
        }
        habitacionRepository.save(entity);
    }

    @Transactional
    public void cambiarEstado(Long id, String estado) {
        Habitacion h = habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada: " + id));
        h.setEstado(estado);
        habitacionRepository.save(h);
    }
}