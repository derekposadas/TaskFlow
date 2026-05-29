package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 * @autor derek
 */

/**
 * DatabaseConnection — Singleton JDBC para Oracle.
 * Si la conexión falla, muestra un diálogo con el error real
 * en lugar de cerrar la aplicación en silencio.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String USUARIO = "TASKFLOW";
    private static final String PASSWORD = "1234";

    private static DatabaseConnection instancia;
    private Connection conexion;

    private DatabaseConnection() {
        conectar();
    }

    public static DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            conectar();
        }
        return conexion;
    }

    private void conectar() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[DB] Conexión establecida con Oracle.");
        } catch (ClassNotFoundException e) {
            String msg = "Driver Oracle no encontrado.\n\n"
                       + "Asegúrate de que ojdbc8.jar está en la carpeta lib/\n"
                       + "y que el script lo incluye en el classpath.\n\n"
                       + "Detalle: " + e.getMessage();
            System.err.println("[DB ERROR] " + msg);
            mostrarErrorConexion(msg);
        } catch (SQLException e) {
            String msg = "No se pudo conectar a Oracle.\n\n"
                       + "Comprueba:\n"
                       + "  • Que Oracle esté arrancado\n"
                       + "  • URL:      " + URL + "\n"
                       + "  • Usuario:  " + USUARIO + "\n"
                       + "  • Contraseña: (la configurada)\n\n"
                       + "Error SQL [" + e.getErrorCode() + "]: " + e.getMessage();
            System.err.println("[DB ERROR] " + msg);
            mostrarErrorConexion(msg);
        }
    }

    /**
     * Muestra un JOptionPane con el error, ya sea desde el EDT o fuera de él.
     * No cierra la aplicación — la ventana sigue abierta para que puedas
     * configurar la conexión sin tener que relanzar.
     */
    private void mostrarErrorConexion(String mensaje) {
        Runnable mostrar = () ->
            JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error de conexión — TaskFlow",
                JOptionPane.ERROR_MESSAGE
            );

        if (SwingUtilities.isEventDispatchThread()) {
            mostrar.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(mostrar);
            } catch (Exception ex) {
                System.err.println("[DB] No se pudo mostrar el diálogo de error: " + ex.getMessage());
            }
        }
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[DB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
