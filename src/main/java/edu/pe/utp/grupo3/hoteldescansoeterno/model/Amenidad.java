package edu.pe.utp.grupo3.hoteldescansoeterno.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "amenidades")
public class Amenidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;  // "WiFi", "TV 32\"", "Jacuzzi", etc.

    // Lado inverso del @ManyToMany
    @ManyToMany(mappedBy = "amenidades", fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Amenidad() {}
    public Amenidad(String nombre) { this.nombre = nombre; }

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }
    public String getNombre()                    { return nombre; }
    public void setNombre(String n)              { this.nombre = n; }
    public List<Habitacion> getHabitaciones()    { return habitaciones; }
    public void setHabitaciones(List<Habitacion> h) { this.habitaciones = h; }
}
