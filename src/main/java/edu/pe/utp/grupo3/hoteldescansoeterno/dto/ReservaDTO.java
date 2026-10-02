package edu.pe.utp.grupo3.hoteldescansoeterno.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservaDTO {

    private Long id;

    @NotNull(message = "Selecciona un huésped")
    private Long huespedId;
    private String huespedNombre;

    @NotNull(message = "Selecciona una habitación")
    private Long habitacionId;
    private String habitacionNumero;
    private String habitacionTipo;
    private Double habitacionPrecio;

    @NotNull(message = "La fecha de entrada es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEntrada;

    @NotNull(message = "La fecha de salida es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaSalida;

    private Integer noches;
    private Double  total;

    private String estado;

    // Observaciones es opcional — no tiene @NotBlank
    
    private String observaciones;

    public ReservaDTO() {}

    // ── Helpers para las vistas ──────────────────────────────

    public boolean isAnulada() {
        return "Anulada".equals(estado) || "Cancelada".equals(estado);
    }

    public String getEstadoDisplay() {
        return "Cancelada".equals(estado) ? "Anulada" : estado;
    }

    public String getTotalFormatted() {
        return total != null ? String.format("S/ %.2f", total) : "S/ 0.00";
    }

    public String getEstadoBadgeClass() {
        if (estado == null) return "bg-secondary";
        return switch (estado) {
            case "Confirmada" -> "bg-success";
            case "Pendiente"  -> "bg-warning text-dark";
            case "Anulada", "Cancelada" -> "bg-danger";
            default           -> "bg-secondary";
        };
    }

    public String getFechaEntradaDisplay() {
        return fechaEntrada != null
            ? fechaEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getFechaSalidaDisplay() {
        return fechaSalida != null
            ? fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getFechaEntradaISO() {
        return fechaEntrada != null ? fechaEntrada.toString() : "";
    }

    public String getFechaSalidaISO() {
        return fechaSalida != null ? fechaSalida.toString() : "";
    }

    // ── Getters y Setters ────────────────────────────────────

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public Long getHuespedId()                 { return huespedId; }
    public void setHuespedId(Long h)           { this.huespedId = h; }
    public String getHuespedNombre()           { return huespedNombre; }
    public void setHuespedNombre(String h)     { this.huespedNombre = h; }
    public Long getHabitacionId()              { return habitacionId; }
    public void setHabitacionId(Long h)        { this.habitacionId = h; }
    public String getHabitacionNumero()        { return habitacionNumero; }
    public void setHabitacionNumero(String h)  { this.habitacionNumero = h; }
    public String getHabitacionTipo()          { return habitacionTipo; }
    public void setHabitacionTipo(String h)    { this.habitacionTipo = h; }
    public Double getHabitacionPrecio()        { return habitacionPrecio; }
    public void setHabitacionPrecio(Double h)  { this.habitacionPrecio = h; }
    public LocalDate getFechaEntrada()         { return fechaEntrada; }
    public void setFechaEntrada(LocalDate f)   { this.fechaEntrada = f; }
    public LocalDate getFechaSalida()          { return fechaSalida; }
    public void setFechaSalida(LocalDate f)    { this.fechaSalida = f; }
    public Integer getNoches()                 { return noches; }
    public void setNoches(Integer n)           { this.noches = n; }
    public Double getTotal()                   { return total; }
    public void setTotal(Double t)             { this.total = t; }
    public String getEstado()                  { return estado; }
    public void setEstado(String e)            { this.estado = e; }
    public String getObservaciones()           { return observaciones; }
    public void setObservaciones(String o)     { this.observaciones = o; }
}
