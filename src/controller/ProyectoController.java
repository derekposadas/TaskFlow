package controller;

import dao.ProyectoDAO;
import model.Proyecto;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * ProyectoController — Lógica de negocio para proyectos.
 */
public class ProyectoController {
    private final ProyectoDAO dao = new ProyectoDAO();

    public boolean crear(Proyecto p) { 
        return dao.insertar(p); 
    }
    public boolean actualizar(Proyecto p) { 
        return dao.actualizar(p); 
    }
    public boolean eliminar(int id) { 
        return dao.eliminar(id); 
    }

    public List<Proyecto> listarTodos() { 
        return dao.listarTodos(); 
    }
    public List<Proyecto> buscar(String texto) { 
        return dao.buscar(texto); 
    }
    public Proyecto obtenerPorId(int id) { 
        return dao.obtenerPorId(id); 
    }
}
