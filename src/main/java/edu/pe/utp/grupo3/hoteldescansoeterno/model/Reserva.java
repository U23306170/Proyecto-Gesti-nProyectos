package edu.pe.utp.grupo3.hoteldescansoeterno.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne — muchas reservas pertenecen a un huésped
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "huesped_id", nullable = false)
    private Huesped huesped;

    // @ManyToOne — muchas reservas pertenecen a una habitación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false)
    private Integer noches;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false, length = 20)
    private String estado;   // "Pendiente", "Confirmada", "Anulada"

    @Column(length = 300)
    private String observaciones;

    public Reserva() {}

    public Reserva(Huesped huesped, Habitacion habitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida,
                   Integer noches, Double total,
                   String estado, String observaciones) {
        this.huesped       = huesped;
        this.habitacion    = habitacion;
        this.fechaEntrada  = fechaEntrada;
        this.fechaSalida   = fechaSalida;
        this.noches        = noches;
        this.total         = total;
        this.estado        = estado;
        this.observaciones = observaciones;
    }

    // Helpers
    public boolean isAnulada() {
        return "Anulada".equals(estado) || "Cancelada".equals(estado);
    }

    public String getEstadoDisplay() {
        return "Cancelada".equals(estado) ? "Anulada" : estado;
    }

    public String getTotalFormatted() {
        return String.format("S/ %.2f", total);
    }

    public String getEstadoBadgeClass() {
        return switch (estado) {
            case "Confirmada" -> "bg-success";
            case "Pendiente"  -> "bg-warning text-dark";
            case "Anulada", "Cancelada" -> "bg-danger";
            default           -> "bg-secondary";
        };
    }

    // Nombres de conveniencia para las vistas
    public String getNombreHuesped() {
        return huesped != null ? huesped.getNombre() : "";
    }

    public String getNumeroHabitacion() {
        return habitacion != null
            ? habitacion.getNumero() + " — " + habitacion.getTipo()
            : "";
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public Huesped getHuesped()                { return huesped; }
    public void setHuesped(Huesped h)          { this.huesped = h; }
    public Habitacion getHabitacion()          { return habitacion; }
    public void setHabitacion(Habitacion h)    { this.habitacion = h; }
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
