package edu.pe.utp.grupo3.hoteldescansoeterno.dto;

import java.time.LocalDate;

// DTO minimo para el calendario de disponibilidad publico.
// Se serializa como String ISO (yyyy-MM-dd) para que el JS del cliente
// no dependa de como Jackson formatee LocalDate.
public class RangoOcupadoDTO {

    private String entrada;
    private String salida;

    public RangoOcupadoDTO() {}

    public RangoOcupadoDTO(LocalDate entrada, LocalDate salida) {
        this.entrada = entrada.toString();
        this.salida  = salida.toString();
    }

    public String getEntrada()             { return entrada; }
    public void setEntrada(String e)       { this.entrada = e; }
    public String getSalida()              { return salida; }
    public void setSalida(String s)        { this.salida = s; }
}