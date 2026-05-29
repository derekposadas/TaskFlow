package controller;

import dao.TareaDAO;
import model.Tarea;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * TareaController — Lógica de negocio para tareas.
 */
public class TareaController {
    private final TareaDAO dao = new TareaDAO();

    public boolean crear(Tarea t) { 
        return dao.insertar(t); 
    }
    public boolean actualizar(Tarea t) { 
        return dao.actualizar(t);
    }
    public boolean eliminar(int id) { 
        return dao.eliminar(id); 
    }
    public boolean cambiarEstado(int id, String estado) { 
        return dao.cambiarEstado(id, estado); 
    }

    public List<Tarea> listarTodas() { 
        return dao.listarTodas(); 
    }
    public List<Tarea> listarPorEstado(String estado) { 
        return dao.listarPorEstado(estado); 
    }
    public List<Tarea> buscar(String texto) { 
        return dao.buscar(texto); 
    }
    public Tarea obtenerPorId(int id) { 
        return dao.obtenerPorId(id); 
    }
}
