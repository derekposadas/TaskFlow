package controller;

import dao.UsuarioDAO;
import model.Usuario;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * UsuarioController — Lógica de negocio para usuarios.
 */
public class UsuarioController {
    private final UsuarioDAO dao = new UsuarioDAO();

    public boolean crear(Usuario u) { 
        return dao.insertar(u); 
    }
    public boolean actualizar(Usuario u) { 
        return dao.actualizar(u); 
    }
    public boolean eliminar(int id) { 
        return dao.eliminar(id); 
    }

    public List<Usuario> listarTodos() { 
        return dao.listarTodos(); 
    }
    public List<Usuario> buscar(String texto) { 
        return dao.buscar(texto); 
    }
    public Usuario obtenerPorId(int id) { 
        return dao.obtenerPorId(id); 
    }
}
