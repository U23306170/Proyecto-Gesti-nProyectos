package edu.pe.utp.grupo3.hoteldescansoeterno.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class HabitacionDTO {

    private Long id;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 10, message = "Máximo 10 caracteres")
    private String numero;

    @NotBlank(message = "Selecciona el tipo")
    private String tipo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "1.0", message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotBlank(message = "Selecciona el estado")
    private String estado;

    @Size(max = 500, message = "Máximo 500 caracteres")
    private String descripcion;

    private List<String> amenidades;


    private boolean ocupadaHoy;

    public HabitacionDTO() {}


    public boolean isDisponible()       { return !"Mantenimiento".equals(estado); }
    public int getPrecioInt()           { return precio != null ? precio.intValue() : 0; }
    public String getAmenidadesStr()    {
        return (amenidades == null || amenidades.isEmpty())
                ? "" : String.join(",", amenidades);
    }
    public String getHeaderClass()      { return "Pet-friendly".equals(tipo) ? "bg-warning text-dark" : "bg-primary text-white"; }


    public String getEstadoMostrado() {
        if ("Mantenimiento".equals(estado)) return "Mantenimiento";
        return ocupadaHoy ? "Ocupada hoy" : "Disponible";
    }
    public String getEstadoBadgeClass() {
        if ("Mantenimiento".equals(estado)) return "bg-dark";
        return ocupadaHoy ? "bg-warning text-dark" : "bg-success";
    }
    public String getSelectValue()      { return numero + " — " + tipo; }
    public String getSelectLabel()      {
        return numero + " — " + tipo + " — S/ " + getPrecioInt() + "/noche";
    }

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }
    public String getNumero()                 { return numero; }
    public void setNumero(String n)           { this.numero = n; }
    public String getTipo()                   { return tipo; }
    public void setTipo(String t)             { this.tipo = t; }
    public Double getPrecio()                 { return precio; }
    public void setPrecio(Double p)           { this.precio = p; }
    public String getEstado()                 { return estado; }
    public void setEstado(String e)           { this.estado = e; }
    public String getDescripcion()            { return descripcion; }
    public void setDescripcion(String d)      { this.descripcion = d; }
    public List<String> getAmenidades()       { return amenidades; }
    public void setAmenidades(List<String> a) { this.amenidades = a; }
    public boolean isOcupadaHoy()             { return ocupadaHoy; }
    public void setOcupadaHoy(boolean o)      { this.ocupadaHoy = o; }
}