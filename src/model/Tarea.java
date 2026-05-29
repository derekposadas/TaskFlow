package model;

import java.util.Date;

/**
 * 
 * @autor derek
 */

/**
 * Modelo Tarea — Representa una tarea asignada dentro de un proyecto.
 */
public class Tarea {
    // Estados posibles de una tarea
    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_EN_PROGRESO = "En Progreso";
    public static final String ESTADO_COMPLETADA = "Completada";

    // Prioridades posibles
    public static final String PRIORIDAD_BAJA = "Baja";
    public static final String PRIORIDAD_MEDIA = "Media";
    public static final String PRIORIDAD_ALTA = "Alta";

    private int id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private Date  fechaInicio;
    private Date  fechaLimite;
    private int idProyecto;
    private int idUsuario;      // 0 si no asignada
    private String nombreProyecto; // Para mostrar en tabla (JOIN)
    private String nombreUsuario;  // Para mostrar en tabla (JOIN)

    // ── Constructores ─────────────────────────────────────────────────────

    public Tarea() {}

    public Tarea(String titulo, String descripcion, String estado,
                 String prioridad, Date fechaLimite, int idProyecto, int idUsuario) {
        this.titulo       = titulo;
        this.descripcion  = descripcion;
        this.estado       = estado;
        this.prioridad    = prioridad;
        this.fechaLimite  = fechaLimite;
        this.idProyecto   = idProyecto;
        this.idUsuario    = idUsuario;
    }

    public Tarea(int id, String titulo, String descripcion, String estado, String prioridad,
                 Date fechaInicio, Date fechaLimite, int idProyecto, int idUsuario,
                 String nombreProyecto, String nombreUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.nombreProyecto = nombreProyecto;
        this.nombreUsuario = nombreUsuario;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getTitulo() { 
        return titulo; 
    }
    public void setTitulo(String t) { 
        this.titulo = t; 
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

    public String getPrioridad() { 
        return prioridad; 
    }
    public void setPrioridad(String p) { 
        this.prioridad = p; 
    }

    public Date getFechaInicio() { 
        return fechaInicio; 
    }
    public void setFechaInicio(Date d) { 
        this.fechaInicio = d; 
    }

    public Date getFechaLimite() { 
        return fechaLimite; 
    }
    public void setFechaLimite(Date d) { 
        this.fechaLimite = d; 
    }

    public int getIdProyecto() { 
        return idProyecto; 
    }
    public void setIdProyecto(int id) { 
        this.idProyecto = id; 
    }

    public int getIdUsuario() { 
        return idUsuario; 
    }
    public void setIdUsuario(int id) { 
        this.idUsuario = id; 
    }

    public String getNombreProyecto() { 
        return nombreProyecto; 
    }
    public void setNombreProyecto(String np) { 
        this.nombreProyecto = np; 
    }

    public String getNombreUsuario() { 
        return nombreUsuario; 
    }
    public void setNombreUsuario(String nu) { 
        this.nombreUsuario = nu; 
    }

    // ── Métodes ─────────────────────────────────────────────────
    
    @Override
    public String toString() {
        return titulo;
    }
}
