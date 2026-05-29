package dao;

import database.DatabaseConnection;
import model.Equipo;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * EquipoDAO — Acceso a datos para las tablas EQUIPOS y EQUIPO_USUARIOS.
 */
public class EquipoDAO {
    private Connection conexion;

    public EquipoDAO() {
        this.conexion = DatabaseConnection.getInstance().getConnection();
    }

    // ── CREACIÓN ────────────────────────────────────────────────────────────

    public boolean insertar(Equipo e) {
        String sql = "INSERT INTO equipos (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error al insertar: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Asigna un usuario a un equipo (tabla equipo_usuarios).
     */
    public boolean asignarUsuario(int idEquipo, int idUsuario) {
        String sql = "INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al asignar usuario: " + e.getMessage());
            return false;
        }
    }

    // ── LECTURA ──────────────────────────────────────────────────────────────

    public List<Equipo> listarTodos() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, fecha_alta FROM equipos ORDER BY nombre";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public Equipo obtenerPorId(int id) {
        String sql = "SELECT id, nombre, descripcion, fecha_alta FROM equipos WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) 
                return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al obtener: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve la lista de usuarios que pertenecen a un equipo.
     */
    public List<Usuario> obtenerMiembros(int idEquipo) {
        List<Usuario> miembros = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.email, u.rol, u.fecha_alta " +
                     "FROM usuarios u " +
                     "JOIN equipo_usuarios eu ON u.id = eu.id_usuario " +
                     "WHERE eu.id_equipo = ? ORDER BY u.nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                miembros.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("rol"),
                    rs.getDate("fecha_alta")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al obtener miembros: " + e.getMessage());
        }
        return miembros;
    }

    // ── MODIFICACIÓN ────────────────────────────────────────────────────────────

    public boolean actualizar(Equipo e) {
        String sql = "UPDATE equipos SET nombre=?, descripcion=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setInt(3, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error al actualizar: " + ex.getMessage());
            return false;
        }
    }

    // ── ELIMINACIÓN ────────────────────────────────────────────────────────────

    public boolean eliminar(int id) {
        try {
            // Eliminar relaciones primero
            String sqlRel = "DELETE FROM equipo_usuarios WHERE id_equipo=?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlRel)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            String sqlEq = "DELETE FROM equipos WHERE id=?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlEq)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public boolean quitarUsuario(int idEquipo, int idUsuario) {
        String sql = "DELETE FROM equipo_usuarios WHERE id_equipo=? AND id_usuario=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EquipoDAO] Error al quitar usuario: " + e.getMessage());
            return false;
        }
    }

    // ── Auxiliar ──────────────────────────────────────────────────────────

    private Equipo mapear(ResultSet rs) throws SQLException {
        return new Equipo(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDate("fecha_alta")
        );
    }
}
