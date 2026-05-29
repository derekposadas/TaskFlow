package model;

import java.util.Date;

/**
 * 
 * @autor derek
 */

/**
 * Modelo Usuario — Representa un usuario del sistema.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String rol;
    private Date fechaAlta;

    // ── Constructores ─────────────────────────────────────────────────────

    public Usuario() {}

    public Usuario(int id, String nombre, String email, String rol, Date fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fechaAlta = fechaAlta;
    }

    /** Constructor para crear usuario nuevo (sin ID ni fecha, los asigna Oracle) */
    public Usuario(String nombre, String email, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
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

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String e) { 
        this.email = e; 
    }

    public String getRol() { 
        return rol; 
    }
    public void setRol(String r) { 
        this.rol = r; 
    }

    public Date getFechaAlta() { 
        return fechaAlta; 
    }
    public void setFechaAlta(Date d) { 
        this.fechaAlta = d; 
    }

    // ── Métodes ─────────────────────────────────────────────────
    
    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}
