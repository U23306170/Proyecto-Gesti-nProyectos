package edu.pe.utp.grupo3.hoteldescansoeterno.dto;

import jakarta.validation.constraints.*;

public class HuespedDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(
    regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
    message = "Los nombres solo pueden contener letras."
)
    private String nombre;
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido paterno debe tener entre 3 y 50 caracteres")
    @Pattern(
    regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
    message = "El apellido paterno solo puede contener letras."
)
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido materno debe tener entre 3 y 50 caracteres")
    @Pattern(
    regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
    message = "El apellido materno solo puede contener letras."
)
    private String apellidoMaterno;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dni;

    // @Pattern acepta cadena vacía O formato email válido
    // El form HTML envía "" cuando el campo está vacío — @Email fallaría en ese
    // caso
    @NotBlank(message = "El correo es obligatorio")
    @Pattern(regexp = "^$|^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$", message = "Ingresa un correo electrónico válido")
    private String email;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String telefono;

    private String estado;

    public HuespedDTO() {
    }

    // ── Helpers para las vistas ──────────────────────────────
    public String getIniciales() {
        return (nombre.charAt(0) + "" + apellidoPaterno.charAt(0)).toUpperCase();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    public String getEstadoBadgeClass() {
        return "Activo".equals(estado) ? "bg-success" : "bg-warning text-dark";
    }

    // ── Getters y Setters ────────────────────────────────────

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
}
