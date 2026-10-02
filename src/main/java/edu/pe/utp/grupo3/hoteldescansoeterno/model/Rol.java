package edu.pe.utp.grupo3.hoteldescansoeterno.model;


import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Spring Security exige el prefijo ROLE_
    // Si el rol es "ROLE_ADMIN", en la config se usa hasRole("ADMIN")
    @Column(nullable = false, unique = true, length = 30)
    private String nombre;   // "ROLE_ADMIN", "ROLE_USER"

    public Rol() {}
    public Rol(String nombre) { this.nombre = nombre; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

