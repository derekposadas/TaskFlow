package model;

import java.util.Date;

/**
 * 
 * @autor derek
 */

/**
 * Modelo Proyecto — Representa un proyecto de trabajo.
 */
public class Proyecto {
    private int id;
    private String nombre;
    private String descripcion;
    private String estado;       // Activo | Pausado | Finalizado
    private Date fechaInicio;
    private Date fechaFin;
    private int idEquipo;     // 0 si no tiene equipo asignado
    private String nombreEquipo; // Para mostrar en la tabla (JOIN)

    // ── Constructores ─────────────────────────────────────────────────────

    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, String estado, int idEquipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idEquipo = idEquipo;
    }

    public Proyecto(int id, String nombre, String descripcion, String estado,
                    Date fechaInicio, Date fechaFin, int idEquipo, String nombreEquipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
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

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String e) { 
        this.estado = e; 
    }

    public Date getFechaInicio() { 
        return fechaInicio; 
    }
    public void setFechaInicio(Date d) { 
        this.fechaInicio = d; 
    }

    public Date getFechaFin() { 
        return fechaFin; 
    }
    public void setFechaFin(Date d) { 
        this.fechaFin = d; 
    }

    public int getIdEquipo() { 
        return idEquipo; 
    }
    public void setIdEquipo(int id) { 
        this.idEquipo = id; 
    }

    public String getNombreEquipo() { 
        return nombreEquipo; 
    }
    public void setNombreEquipo(String ne) { 
        this.nombreEquipo = ne; 
    }

    // ── Métodes ─────────────────────────────────────────────────
    
    @Override
    public String toString() {
        return nombre;
    }
}
