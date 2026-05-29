package dao;

import database.DatabaseConnection;
import model.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * TareaDAO — Acceso a datos para la tabla TAREAS.
 */
public class TareaDAO {
    private Connection conexion;

    public TareaDAO() {
        this.conexion = DatabaseConnection.getInstance().getConnection();
    }

    // ── CREACIÓN ────────────────────────────────────────────────────────────

    public boolean insertar(Tarea t) {
        String sql = "INSERT INTO tareas (titulo, descripcion, estado, prioridad, " +
                     "fecha_limite, id_proyecto, id_usuario) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getEstado());
            ps.setString(4, t.getPrioridad());
            if (t.getFechaLimite() != null) {
                ps.setDate(5, new java.sql.Date(t.getFechaLimite().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setInt(6, t.getIdProyecto());
            if (t.getIdUsuario() > 0) {
                ps.setInt(7, t.getIdUsuario());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al insertar: " + e.getMessage());
            return false;
        }
    }

    // ── LECTURA ──────────────────────────────────────────────────────────────

    /**
     * Lista todas las tareas con JOIN a proyectos y usuarios.
     */
    public List<Tarea> listarTodas() {
        return ejecutarQuery(
            "SELECT t.id, t.titulo, t.descripcion, t.estado, t.prioridad, " +
            "       t.fecha_inicio, t.fecha_limite, t.id_proyecto, t.id_usuario, " +
            "       p.nombre AS nombre_proyecto, u.nombre AS nombre_usuario " +
            "FROM tareas t " +
            "JOIN proyectos p ON t.id_proyecto = p.id " +
            "LEFT JOIN usuarios u ON t.id_usuario = u.id " +
            "ORDER BY t.fecha_inicio DESC"
        );
    }

    /**
     * Filtra tareas por estado: Pendiente | En Progreso | Completada
     */
    public List<Tarea> listarPorEstado(String estado) {
        List<Tarea> lista = new ArrayList<>();
        String sql = "SELECT t.id, t.titulo, t.descripcion, t.estado, t.prioridad, " +
                     "       t.fecha_inicio, t.fecha_limite, t.id_proyecto, t.id_usuario, " +
                     "       p.nombre AS nombre_proyecto, u.nombre AS nombre_usuario " +
                     "FROM tareas t " +
                     "JOIN proyectos p ON t.id_proyecto = p.id " +
                     "LEFT JOIN usuarios u ON t.id_usuario = u.id " +
                     "WHERE t.estado = ? ORDER BY t.fecha_inicio DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) 
                lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al filtrar por estado: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca tareas por título o descripción.
     */
    public List<Tarea> buscar(String texto) {
        List<Tarea> lista = new ArrayList<>();
        String sql = "SELECT t.id, t.titulo, t.descripcion, t.estado, t.prioridad, " +
                     "       t.fecha_inicio, t.fecha_limite, t.id_proyecto, t.id_usuario, " +
                     "       p.nombre AS nombre_proyecto, u.nombre AS nombre_usuario " +
                     "FROM tareas t " +
                     "JOIN proyectos p ON t.id_proyecto = p.id " +
                     "LEFT JOIN usuarios u ON t.id_usuario = u.id " +
                     "WHERE UPPER(t.titulo) LIKE ? OR UPPER(t.descripcion) LIKE ? " +
                     "ORDER BY t.titulo";
        String patron = "%" + texto.toUpperCase() + "%";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) 
                lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al buscar: " + e.getMessage());
        }
        return lista;
    }

    public Tarea obtenerPorId(int id) {
        List<Tarea> lista = ejecutarQuery(
            "SELECT t.id, t.titulo, t.descripcion, t.estado, t.prioridad, " +
            "       t.fecha_inicio, t.fecha_limite, t.id_proyecto, t.id_usuario, " +
            "       p.nombre AS nombre_proyecto, u.nombre AS nombre_usuario " +
            "FROM tareas t JOIN proyectos p ON t.id_proyecto=p.id " +
            "LEFT JOIN usuarios u ON t.id_usuario=u.id WHERE t.id=" + id
        );
        return lista.isEmpty() ? null : lista.get(0);
    }

    // ── MODIFICACIÓN ────────────────────────────────────────────────────────────

    public boolean actualizar(Tarea t) {
        String sql = "UPDATE tareas SET titulo=?, descripcion=?, estado=?, prioridad=?, " +
                     "fecha_limite=?, id_proyecto=?, id_usuario=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getEstado());
            ps.setString(4, t.getPrioridad());
            if (t.getFechaLimite() != null) {
                ps.setDate(5, new java.sql.Date(t.getFechaLimite().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setInt(6, t.getIdProyecto());
            if (t.getIdUsuario() > 0) {
                ps.setInt(7, t.getIdUsuario());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }
            ps.setInt(8, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método rápido para cambiar solo el estado de una tarea.
     */
    public boolean cambiarEstado(int idTarea, String nuevoEstado) {
        String sql = "UPDATE tareas SET estado=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTarea);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    // ── ELIMINACIÓN ────────────────────────────────────────────────────────────

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tareas WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ── Auxiliares ────────────────────────────────────────────────────────

    private List<Tarea> ejecutarQuery(String sql) {
        List<Tarea> lista = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) 
                lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[TareaDAO] Error en query: " + e.getMessage());
        }
        return lista;
    }

    private Tarea mapear(ResultSet rs) throws SQLException {
        return new Tarea(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("descripcion"),
            rs.getString("estado"),
            rs.getString("prioridad"),
            rs.getDate("fecha_inicio"),
            rs.getDate("fecha_limite"),
            rs.getInt("id_proyecto"),
            rs.getInt("id_usuario"),
            rs.getString("nombre_proyecto"),
            rs.getString("nombre_usuario")
        );
    }
}
