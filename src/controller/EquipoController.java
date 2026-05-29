package controller;

import dao.EquipoDAO;
import model.Equipo;
import model.Usuario;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * EquipoController — Lógica de negocio para equipos.
 */
public class EquipoController {
    private final EquipoDAO dao = new EquipoDAO();

    public boolean crear(Equipo e) { 
        return dao.insertar(e); 
    }
    public boolean actualizar(Equipo e) { 
        return dao.actualizar(e); 
    }
    public boolean eliminar(int id) { 
        return dao.eliminar(id); 
    }
    public boolean asignarUsuario(int idEquipo, int idUsuario) 
    { return dao.asignarUsuario(idEquipo, idUsuario); 
    }
    public boolean quitarUsuario(int idEquipo, int idUsuario) { 
        return dao.quitarUsuario(idEquipo, idUsuario); 
    }

    public List<Equipo>  listarTodos() { 
        return dao.listarTodos(); 
    }
    public Equipo        obtenerPorId(int id) { 
        return dao.obtenerPorId(id); 
    }
    public List<Usuario> obtenerMiembros(int idEq) { 
        return dao.obtenerMiembros(idEq); 
    }
}
