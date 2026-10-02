package edu.pe.utp.grupo3.hoteldescansoeterno.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // hash BCrypt — NUNCA texto plano

    @Column(nullable = false)
    private boolean activo;

    // FetchType.EAGER es obligatorio
    // Spring Security carga los roles FUERA de una transacción JPA
    // Con LAZY lanzaría LazyInitializationException al autenticar
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns        = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles = new ArrayList<>();

    public Usuario() {}

    public Long getId() {
        return idUsuario;
    }

    public void setId(Long id) {
        this.idUsuario = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

}