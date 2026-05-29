package dao;

import database.DatabaseConnection;
import model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * ProyectoDAO — Acceso a datos para la tabla PROYECTOS.
 */
public class ProyectoDAO {
    private Connection conexion;

    public ProyectoDAO() {
        this.conexion = DatabaseConnection.getInstance().getConnection();
    }

    // ── CREACIÓN ────────────────────────────────────────────────────────────

    public boolean insertar(Proyecto p) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, estado, id_equipo) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getEstado());
            if (p.getIdEquipo() > 0) {
                ps.setInt(4, p.getIdEquipo());
            } else {
                ps.setNull(4, Types.NUMERIC);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al insertar: " + e.getMessage());
            return false;
        }
    }

    // ── LECTURA ──────────────────────────────────────────────────────────────

    /**
     * Lista todos los proyectos con JOIN para mostrar el nombre del equipo.
     */
    public List<Proyecto> listarTodos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.estado, " +
                     "       p.fecha_inicio, p.fecha_fin, p.id_equipo, e.nombre AS nombre_equipo " +
                     "FROM proyectos p LEFT JOIN equipos e ON p.id_equipo = e.id " +
                     "ORDER BY p.nombre";
        try (Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al listar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca proyectos por nombre o descripción.
     */
    public List<Proyecto> buscar(String texto) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.estado, " +
                     "       p.fecha_inicio, p.fecha_fin, p.id_equipo, e.nombre AS nombre_equipo " +
                     "FROM proyectos p LEFT JOIN equipos e ON p.id_equipo = e.id " +
                     "WHERE UPPER(p.nombre) LIKE ? OR UPPER(p.descripcion) LIKE ? " +
                     "ORDER BY p.nombre";
        String patron = "%" + texto.toUpperCase() + "%";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al buscar: " + e.getMessage());
        }
        return lista;
    }

    public Proyecto obtenerPorId(int id) {
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.estado, " +
                     "       p.fecha_inicio, p.fecha_fin, p.id_equipo, e.nombre AS nombre_equipo " +
                     "FROM proyectos p LEFT JOIN equipos e ON p.id_equipo = e.id WHERE p.id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) 
                return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al obtener: " + e.getMessage());
        }
        return null;
    }

    // ── MODIFICACIÓN ────────────────────────────────────────────────────────────

    public boolean actualizar(Proyecto p) {
        String sql = "UPDATE proyectos SET nombre=?, descripcion=?, estado=?, " +
                     "fecha_fin=?, id_equipo=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getEstado());
            if (p.getFechaFin() != null) {
                ps.setDate(4, new java.sql.Date(p.getFechaFin().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            if (p.getIdEquipo() > 0) {
                ps.setInt(5, p.getIdEquipo());
            } else {
                ps.setNull(5, Types.NUMERIC);
            }
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // ── ELIMINACIÓN ────────────────────────────────────────────────────────────

    /**
     * Elimina el proyecto y sus tareas asociadas (en cascada manual).
     */
    public boolean eliminar(int id) {
        try {
            // Primero eliminar tareas del proyecto
            String sqlTareas = "DELETE FROM tareas WHERE id_proyecto=?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlTareas)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            // Luego eliminar el proyecto
            String sqlProyecto = "DELETE FROM proyectos WHERE id=?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlProyecto)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ── Auxiliar ──────────────────────────────────────────────────────────

    private Proyecto mapear(ResultSet rs) throws SQLException {
        return new Proyecto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getString("estado"),
            rs.getDate("fecha_inicio"),
            rs.getDate("fecha_fin"),
            rs.getInt("id_equipo"),
            rs.getString("nombre_equipo")
        );
    }
}
