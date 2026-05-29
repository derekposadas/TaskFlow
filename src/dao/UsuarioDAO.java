package dao;

import database.DatabaseConnection;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * UsuarioDAO — Acceso a datos para la tabla USUARIOS.
 */
public class UsuarioDAO {
    private Connection conexion;

    public UsuarioDAO() {
        this.conexion = DatabaseConnection.getInstance().getConnection();
    }

    // ── CREACIÓN ────────────────────────────────────────────────────────────

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, email, rol) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al insertar: " + e.getMessage());
            return false;
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email, rol, fecha_alta FROM usuarios ORDER BY nombre";
        try (Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al listar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca usuarios cuyo nombre o email contenga el texto dado.
     */
    public List<Usuario> buscar(String texto) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email, rol, fecha_alta FROM usuarios " +
                     "WHERE UPPER(nombre) LIKE ? OR UPPER(email) LIKE ? ORDER BY nombre";
        String patron = "%" + texto.toUpperCase() + "%";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al buscar: " + e.getMessage());
        }
        return lista;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT id, nombre, email, rol, fecha_alta FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener por ID: " + e.getMessage());
        }
        return null;
    }

    // ── MODIFICACIÓN ────────────────────────────────────────────────────────────

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, email=?, rol=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // ── ELIMINACIÓN ────────────────────────────────────────────────────────────

    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ── Auxiliar ──────────────────────────────────────────────────────────

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("rol"),
            rs.getDate("fecha_alta")
        );
    }
}
