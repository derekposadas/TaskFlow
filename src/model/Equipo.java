package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @autor derek
 */

/**
 * Modelo Equipo — Representa un equipo de trabajo.
 */
public class Equipo {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaAlta;
    private List<Usuario> miembros;  // Lista de usuarios del equipo

    // ── Constructores ─────────────────────────────────────────────────────

    public Equipo() {
        miembros = new ArrayList<>();
    }

    public Equipo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.miembros = new ArrayList<>();
    }

    public Equipo(int id, String nombre, String descripcion, Date fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.miembros = new ArrayList<>();
    }

    // ── Getters y Setters ─────────────────────────────────────────────────

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String n) { 
        this.nombre = n; 
    }

    public String getDescripcion() { 
        return descripcion;
    }
    public void setDescripcion(String d) { 
        this.descripcion = d; 
    }

    public Date getFechaAlta() { 
        return fechaAlta; 
    }
    public void setFechaAlta(Date d) { 
        this.fechaAlta = d; 
    }

    public List<Usuario> getMiembros() { 
        return miembros; 
    }
    public void setMiembros(List<Usuario> m) { 
        this.miembros = m; 
    }

    // ── Métodos ─────────────────────────────────────────────────
    
    public void agregarMiembro(Usuario u) { 
        miembros.add(u); 
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
