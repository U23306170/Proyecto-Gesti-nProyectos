package edu.pe.utp.grupo3.hoteldescansoeterno.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String numero;          // "101", "205", "401"

    @Column(nullable = false, length = 20)
    private String tipo;            // "Simple", "Doble", "Pet-friendly"

    @Column(nullable = false)
    private Double precio;          // por noche

    @Column(nullable = false, length = 20)
    private String estado;          // "Disponible", "Ocupada", "Mantenimiento"

    @Column(length = 500)
    private String descripcion;

    // @OneToMany — una habitación tiene muchas reservas
    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    // @ManyToMany — una habitación tiene muchas amenidades
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "habitacion_amenidad",
            joinColumns        = @JoinColumn(name = "habitacion_id"),
            inverseJoinColumns = @JoinColumn(name = "amenidad_id")
    )
    private List<Amenidad> amenidades = new ArrayList<>();

    public Habitacion() {}

    public Habitacion(String numero, String tipo, Double precio,
                      String estado, String descripcion) {
        this.numero      = numero;
        this.tipo        = tipo;
        this.precio      = precio;
        this.estado      = estado;
        this.descripcion = descripcion;
    }


    public boolean isDisponible() { return !"Mantenimiento".equals(estado); }

    public String getHeaderClass() {
        return "Pet-friendly".equals(tipo) ? "bg-warning text-dark" : "bg-primary text-white";
    }

    public String getEstadoBadgeClass() {
        return "Disponible".equals(estado) ? "bg-success" : "bg-dark";
    }

    public String getSelectValue() { return numero + " — " + tipo; }

    public String getSelectLabel() {
        return numero + " — " + tipo + " — S/ " + precio.intValue() + "/noche";
    }

    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }
    public String getNumero()                      { return numero; }
    public void setNumero(String n)                { this.numero = n; }
    public String getTipo()                        { return tipo; }
    public void setTipo(String t)                  { this.tipo = t; }
    public Double getPrecio()                      { return precio; }
    public void setPrecio(Double p)                { this.precio = p; }
    public String getEstado()                      { return estado; }
    public void setEstado(String e)                { this.estado = e; }
    public String getDescripcion()                 { return descripcion; }
    public void setDescripcion(String d)           { this.descripcion = d; }
    public List<Reserva> getReservas()             { return reservas; }
    public void setReservas(List<Reserva> r)       { this.reservas = r; }
    public List<Amenidad> getAmenidades()          { return amenidades; }
    public void setAmenidades(List<Amenidad> a)    { this.amenidades = a; }
}