package org.example.startseeing.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String usuario;
    private String accion;

    private LocalDateTime fecha = LocalDateTime.now();

    public Bitacora() {}

    public Bitacora(String usuario, String accion) {
        this.usuario = usuario;
        this.accion = accion;
        this.fecha = LocalDateTime.now();
    }

    // Getters y setters
    public int getId() { return id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

}
