package edu.pe.utp.grupo3.hoteldescansoeterno.mapper;

import org.springframework.stereotype.Component;

import edu.pe.utp.grupo3.hoteldescansoeterno.dto.*;
import edu.pe.utp.grupo3.hoteldescansoeterno.model.*;

import java.util.List;

@Component
public class HotelMapper {

    // ── Habitacion ──────────────────────────────────────────
    public HabitacionDTO toDTO(Habitacion h) {
        HabitacionDTO dto = new HabitacionDTO();
        dto.setId(h.getId());
        dto.setNumero(h.getNumero());
        dto.setTipo(h.getTipo());
        dto.setPrecio(h.getPrecio());
        dto.setEstado(h.getEstado());
        dto.setDescripcion(h.getDescripcion());
        if (h.getAmenidades() != null) {
            dto.setAmenidades(h.getAmenidades().stream()
                .map(Amenidad::getNombre).toList());
        }
        return dto;
    }

    public Habitacion toEntity(HabitacionDTO dto) {
        Habitacion h = new Habitacion();
        h.setId(dto.getId());
        h.setNumero(dto.getNumero());
        h.setTipo(dto.getTipo());
        h.setPrecio(dto.getPrecio());
        h.setEstado(dto.getEstado());
        h.setDescripcion(dto.getDescripcion());
        return h;
    }

    public List<HabitacionDTO> toDTOListHab(List<Habitacion> list) {
        return list.stream().map(this::toDTO).toList();
    }

    // ── Huesped ─────────────────────────────────────────────
    public HuespedDTO toDTO(Huesped h) {
        HuespedDTO dto = new HuespedDTO();
        dto.setId(h.getId());
       dto.setNombre(h.getNombre());
    dto.setApellidoPaterno(h.getApellidoPaterno());
    dto.setApellidoMaterno(h.getApellidoMaterno());
        dto.setDni(h.getDni());
        dto.setEmail(h.getEmail());
        dto.setTelefono(h.getTelefono());
        dto.setEstado(h.getEstado());
        return dto;
    }

    public Huesped toEntity(HuespedDTO dto) {
        Huesped h = new Huesped();
        h.setId(dto.getId());
        h.setNombre(dto.getNombre());
        h.setApellidoPaterno(dto.getApellidoPaterno());
h.setApellidoMaterno(dto.getApellidoMaterno());
        h.setDni(dto.getDni());
        h.setEmail(dto.getEmail());
        h.setTelefono(dto.getTelefono());
        h.setEstado(dto.getEstado() != null ? dto.getEstado() : "Activo");
        return h;
    }

    public List<HuespedDTO> toDTOListHues(List<Huesped> list) {
        return list.stream().map(this::toDTO).toList();
    }

    // ── Reserva ─────────────────────────────────────────────
    public ReservaDTO toDTO(Reserva r) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(r.getId());
        if (r.getHuesped() != null) {
            dto.setHuespedId(r.getHuesped().getId());
            dto.setHuespedNombre(r.getHuesped().getNombreCompleto());
        }
        if (r.getHabitacion() != null) {
            dto.setHabitacionId(r.getHabitacion().getId());
            dto.setHabitacionNumero(r.getHabitacion().getNumero());
            dto.setHabitacionTipo(r.getHabitacion().getTipo());
            dto.setHabitacionPrecio(r.getHabitacion().getPrecio());
        }
        dto.setFechaEntrada(r.getFechaEntrada());
        dto.setFechaSalida(r.getFechaSalida());
        dto.setNoches(r.getNoches());
        dto.setTotal(r.getTotal());
        dto.setEstado(r.getEstado());
        dto.setObservaciones(r.getObservaciones());
        return dto;
    }

    public List<ReservaDTO> toDTOListRes(List<Reserva> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
