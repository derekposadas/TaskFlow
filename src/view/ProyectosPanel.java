package view;

import controller.ProyectoController;
import controller.EquipoController;
import model.Proyecto;
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
 * ProyectosPanel — Vista para gestión de proyectos.
 */
public class ProyectosPanel extends JPanel implements MainFrame.Refrescable {

    private final ProyectoController proyectoController;
    private final EquipoController equipoController;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField campoBusqueda;

    private static final String[] COLUMNAS = {"ID","Nombre","Estado","Equipo","Fecha Inicio","Fecha Fin"};

    public ProyectosPanel(ProyectoController pc, EquipoController ec) {
        this.proyectoController = pc;
        this.equipoController = ec;
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        construir();
    }

    private void construir() {
        add(crearCabecera(), BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        UIUtils.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDE));
        add(scroll, BorderLayout.CENTER);
        add(crearBarraBotones(), BorderLayout.SOUTH);

        cargarDatos(null);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.add(UIUtils.crearTitulo("Proyectos"), BorderLayout.WEST);

        JPanel busq = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        busq.setOpaque(false);
        campoBusqueda = UIUtils.crearCampoTexto(20);
        JButton btnB = UIUtils.crearBoton("Buscar");
        JButton btnL = new JButton("X");
        btnL.setFont(UIUtils.FUENTE_NORMAL);
        btnL.setFocusPainted(false);
        btnB.addActionListener(e -> cargarDatos(campoBusqueda.getText().trim()));
        btnL.addActionListener(e -> { campoBusqueda.setText(""); cargarDatos(null); });
        campoBusqueda.addActionListener(e -> cargarDatos(campoBusqueda.getText().trim()));
        busq.add(new JLabel("Buscar: ")); busq.add(campoBusqueda); busq.add(btnB); busq.add(btnL);
        panel.add(busq, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearBarraBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setOpaque(false);
        JButton btnNuevo    = UIUtils.crearBoton("+ Nuevo");
        JButton btnEditar   = UIUtils.crearBoton("Editar");
        JButton btnEliminar = UIUtils.crearBotonPeligro("Eliminar");
        JButton btnRefrescar= UIUtils.crearBoton("↺ Refrescar");
        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnRefrescar.addActionListener(e -> cargarDatos(null));
        panel.add(btnNuevo); panel.add(btnEditar); panel.add(btnEliminar); panel.add(btnRefrescar);
        return panel;
    }

    private void cargarDatos(String busqueda) {
        modeloTabla.setRowCount(0);
        List<Proyecto> lista = (busqueda == null || busqueda.isEmpty())
            ? proyectoController.listarTodos()
            : proyectoController.buscar(busqueda);
        for (Proyecto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getEstado(),
                p.getNombreEquipo() != null ? p.getNombreEquipo() : "—",
                p.getFechaInicio() != null ? p.getFechaInicio().toString() : "",
                p.getFechaFin()    != null ? p.getFechaFin().toString()    : "—"
            });
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { UIUtils.mostrarAviso(this, "Selecciona un proyecto."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        abrirFormulario(proyectoController.obtenerPorId(id));
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { UIUtils.mostrarAviso(this, "Selecciona un proyecto."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        if (UIUtils.confirmar(this, "¿Eliminar el proyecto \"" + nombre + "\" y todas sus tareas?")) {
            if (proyectoController.eliminar(id)) {
                UIUtils.mostrarExito(this, "Proyecto eliminado."); cargarDatos(null);
            } else {
                UIUtils.mostrarError(this, "No se pudo eliminar el proyecto.");
            }
        }
    }

    private void abrirFormulario(Proyecto p) {
        ProyectoFormDialog dlg = new ProyectoFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            proyectoController, equipoController, p);
        dlg.setVisible(true);
        if (dlg.isGuardado()) 
            cargarDatos(null);
    }

    @Override public void refrescar() { 
        cargarDatos(null); 
    }
}
