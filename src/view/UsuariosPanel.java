package view;

import controller.UsuarioController;
import model.Usuario;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * UsuariosPanel — Vista completa de gestión de usuarios.
 * Implementa Refrescable para recargar datos al activarse.
 */
public class UsuariosPanel extends JPanel implements MainFrame.Refrescable {

    private final UsuarioController controller;

    // Componentes de la tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Barra de búsqueda
    private JTextField campoBusqueda;

    // Columnas de la tabla
    private static final String[] COLUMNAS = {"ID", "Nombre", "Email", "Rol", "Fecha Alta"};

    public UsuariosPanel(UsuarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        construir();
    }

    private void construir() {
        // ── Cabecera ──────────────────────────────────────────────────────
        add(crearCabecera(), BorderLayout.NORTH);

        // ── Tabla ─────────────────────────────────────────────────────────
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        UIUtils.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);  // ID estrecho

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDE));
        add(scroll, BorderLayout.CENTER);

        // ── Barra de botones inferior ─────────────────────────────────────
        add(crearBarraBotones(), BorderLayout.SOUTH);

        cargarDatos(null);
    }

    // ── Cabecera con título y búsqueda ────────────────────────────────────

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel titulo = UIUtils.crearTitulo("Usuarios");
        panel.add(titulo, BorderLayout.WEST);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        busqueda.setOpaque(false);
        campoBusqueda = UIUtils.crearCampoTexto(20);
        campoBusqueda.setToolTipText("Buscar por nombre o email...");
        JButton btnBuscar = UIUtils.crearBoton("Buscar");
        JButton btnLimpiar = new JButton("X");
        btnLimpiar.setFont(UIUtils.FUENTE_NORMAL);
        btnLimpiar.setFocusPainted(false);

        btnBuscar.addActionListener(e -> cargarDatos(campoBusqueda.getText().trim()));
        btnLimpiar.addActionListener(e -> { campoBusqueda.setText(""); cargarDatos(null); });
        campoBusqueda.addActionListener(e -> cargarDatos(campoBusqueda.getText().trim()));

        busqueda.add(new JLabel("Buscar: "));
        busqueda.add(campoBusqueda);
        busqueda.add(btnBuscar);
        busqueda.add(btnLimpiar);
        panel.add(busqueda, BorderLayout.EAST);
        return panel;
    }

    // ── Botones de acción ─────────────────────────────────────────────────

    private JPanel crearBarraBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setOpaque(false);

        JButton btnNuevo   = UIUtils.crearBoton("+ Nuevo");
        JButton btnEditar  = UIUtils.crearBoton("Editar");
        JButton btnEliminar = UIUtils.crearBotonPeligro("Eliminar");
        JButton btnRefrescar = UIUtils.crearBoton("↺ Refrescar");

        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnRefrescar.addActionListener(e -> cargarDatos(null));

        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnRefrescar);
        return panel;
    }

    // ── Lógica ────────────────────────────────────────────────────────────

    private void cargarDatos(String busqueda) {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = (busqueda == null || busqueda.isEmpty())
            ? controller.listarTodos()
            : controller.buscar(busqueda);
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                u.getId(), u.getNombre(), u.getEmail(), u.getRol(),
                u.getFechaAlta() != null ? u.getFechaAlta().toString() : ""
            });
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un usuario primero."); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        abrirFormulario(controller.obtenerPorId(id));
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un usuario primero."); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        if (UIUtils.confirmar(this, "¿Eliminar al usuario \"" + nombre + "\"?")) {
            if (controller.eliminar(id)) {
                UIUtils.mostrarExito(this, "Usuario eliminado correctamente.");
                cargarDatos(null);
            } else {
                UIUtils.mostrarError(this, "No se pudo eliminar el usuario.");
            }
        }
    }

    private void abrirFormulario(Usuario usuario) {
        UsuarioFormDialog dialog = new UsuarioFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), controller, usuario);
        dialog.setVisible(true);
        if (dialog.isGuardado()) 
            cargarDatos(null);
    }

    @Override
    public void refrescar() { 
        cargarDatos(null); 
    }
}
