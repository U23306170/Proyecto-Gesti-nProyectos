package edu.pe.utp.grupo3.hoteldescansoeterno.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "huespedes")
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String telefono;

    @Column(nullable = false, length = 20)
    private String estado; // "Activo", "Pendiente", "Inactivo"

    // @OneToMany — un huésped tiene muchas reservas
    @OneToMany(mappedBy = "huesped", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    public Huesped() {
    }

    public Huesped(String nombre, String apellidoPaterno, String apellidoMaterno, String dni, String email,
            String telefono, String estado) {
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.apellidoPaterno= apellidoPaterno;
        this.apellidoMaterno= apellidoMaterno;
        
    }

    // Helpers
    public String getIniciales() {
    return (nombre.charAt(0) + "" + apellidoPaterno.charAt(0)).toUpperCase();
}
    public String getNombreCompleto() {
    return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
}

    public String getEstadoBadgeClass() {
        return "Activo".equals(estado) ? "bg-success" : "bg-warning text-dark";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String d) {
        this.dni = d;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String t) {
        this.telefono = t;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String e) {
        this.estado = e;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> r) {
        this.reservas = r;
    }
}
