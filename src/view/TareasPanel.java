package view;

import controller.TareaController;
import controller.UsuarioController;
import controller.ProyectoController;
import model.Tarea;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * TareasPanel — Vista completa de gestión de tareas con filtro por estado.
 */
public class TareasPanel extends JPanel implements MainFrame.Refrescable {

    private final TareaController tareaController;
    private final UsuarioController usuarioController;
    private final ProyectoController proyectoController;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField campoBusqueda;
    private JComboBox<String> comboFiltroEstado;

    private static final String[] COLUMNAS = {"ID","Título","Estado","Prioridad","Proyecto","Asignado a","Fecha Límite"};
    private static final String[] FILTROS  = {"Todos","Pendiente","En Progreso","Completada"};

    public TareasPanel(TareaController tc, UsuarioController uc, ProyectoController pc) {
        this.tareaController = tc;
        this.usuarioController = uc;
        this.proyectoController = pc;
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
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);

        // Renderer de colores por estado
        tabla.getColumnModel().getColumn(2).setCellRenderer(new EstadoRenderer());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDE));
        add(scroll, BorderLayout.CENTER);
        add(crearBarraBotones(), BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.add(UIUtils.crearTitulo("[OK] Tareas"), BorderLayout.WEST);

        // Panel derecho: filtro + búsqueda
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        derecha.setOpaque(false);

        comboFiltroEstado = new JComboBox<>(FILTROS);
        comboFiltroEstado.setFont(UIUtils.FUENTE_NORMAL);
        comboFiltroEstado.addActionListener(e -> cargarDatos());

        campoBusqueda = UIUtils.crearCampoTexto(18);
        JButton btnB = UIUtils.crearBoton("Buscar");
        JButton btnL = new JButton("X");
        btnL.setFont(UIUtils.FUENTE_NORMAL); btnL.setFocusPainted(false);
        btnB.addActionListener(e -> buscar());
        btnL.addActionListener(e -> { campoBusqueda.setText(""); cargarDatos(); });
        campoBusqueda.addActionListener(e -> buscar());

        derecha.add(new JLabel("Estado: ")); derecha.add(comboFiltroEstado);
        derecha.add(Box.createHorizontalStrut(10));
        derecha.add(new JLabel("Buscar: ")); derecha.add(campoBusqueda);
        derecha.add(btnB); derecha.add(btnL);
        panel.add(derecha, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearBarraBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setOpaque(false);
        JButton btnNuevo = UIUtils.crearBoton("+ Nueva");
        JButton btnEditar = UIUtils.crearBoton("Editar");
        JButton btnEstado = UIUtils.crearBoton("Estado");
        JButton btnEliminar = UIUtils.crearBotonPeligro("Eliminar");
        JButton btnRefrescar = UIUtils.crearBoton("Refrescar");

        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panel.add(btnNuevo); panel.add(btnEditar); panel.add(btnEstado);
        panel.add(btnEliminar); panel.add(btnRefrescar);
        return panel;
    }

    private void cargarDatos() {
        String filtro = (String) comboFiltroEstado.getSelectedItem();
        List<Tarea> lista;
        if ("Todos".equals(filtro)) {
            lista = tareaController.listarTodas();
        } else {
            lista = tareaController.listarPorEstado(filtro);
        }
        poblarTabla(lista);
    }

    private void buscar() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) { cargarDatos(); return; }
        poblarTabla(tareaController.buscar(texto));
    }

    private void poblarTabla(List<Tarea> lista) {
        modeloTabla.setRowCount(0);
        for (Tarea t : lista) {
            modeloTabla.addRow(new Object[]{
                t.getId(), t.getTitulo(), t.getEstado(), t.getPrioridad(),
                t.getNombreProyecto() != null ? t.getNombreProyecto() : "—",
                t.getNombreUsuario()  != null ? t.getNombreUsuario()  : "Sin asignar",
                t.getFechaLimite()    != null ? t.getFechaLimite().toString() : "—"
            });
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona una tarea."); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        abrirFormulario(tareaController.obtenerPorId(id));
    }

    private void cambiarEstadoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona una tarea."); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String actual = (String) modeloTabla.getValueAt(fila, 2);

        String[] estados = {Tarea.ESTADO_PENDIENTE, Tarea.ESTADO_EN_PROGRESO, Tarea.ESTADO_COMPLETADA};
        String nuevo = (String) JOptionPane.showInputDialog(
            this, "Selecciona el nuevo estado:", "Cambiar Estado",
            JOptionPane.PLAIN_MESSAGE, null, estados, actual);

        if (nuevo != null && !nuevo.equals(actual)) {
            if (tareaController.cambiarEstado(id, nuevo)) {
                UIUtils.mostrarExito(this, "Estado actualizado a: " + nuevo);
                cargarDatos();
            } else {
                UIUtils.mostrarError(this, "No se pudo cambiar el estado.");
            }
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona una tarea."); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String titulo = (String) modeloTabla.getValueAt(fila, 1);
        if (UIUtils.confirmar(this, "¿Eliminar la tarea \"" + titulo + "\"?")) {
            if (tareaController.eliminar(id)) {
                UIUtils.mostrarExito(this, "Tarea eliminada."); 
                cargarDatos();
            } else {
                UIUtils.mostrarError(this, "No se pudo eliminar.");
            }
        }
    }

    private void abrirFormulario(Tarea t) {
        TareaFormDialog dlg = new TareaFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            tareaController, usuarioController, proyectoController, t);
        dlg.setVisible(true);
        if (dlg.isGuardado()) 
            cargarDatos();
    }

    @Override public void refrescar() { 
        cargarDatos(); 
    }

    // ── Renderer coloreado para columna Estado ────────────────────────────
    private static class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            if (!isSelected && value != null) {
                switch (value.toString()) {
                    case "Pendiente":   setForeground(UIUtils.COLOR_PENDIENTE);   break;
                    case "En Progreso": setForeground(UIUtils.COLOR_EN_PROGRESO); break;
                    case "Completada":  setForeground(UIUtils.COLOR_COMPLETADA);  break;
                    default:            setForeground(UIUtils.COLOR_TEXTO);
                }
            }
            return this;
        }
    }
}
