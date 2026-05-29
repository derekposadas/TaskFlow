package view;

import controller.EquipoController;
import controller.UsuarioController;
import model.Equipo;
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
 * EquiposPanel — Vista de gestión de equipos y sus miembros.
 */
public class EquiposPanel extends JPanel implements MainFrame.Refrescable {
    private final EquipoController equipoController;
    private final UsuarioController usuarioController;

    private JTable tablaEquipos;
    private DefaultTableModel modeloEquipos;
    private JTable tablaMiembros;
    private DefaultTableModel modeloMiembros;

    private static final String[] COL_EQUIPOS  = {"ID","Nombre","Descripción","Fecha Alta"};
    private static final String[] COL_MIEMBROS = {"ID","Nombre","Email","Rol"};

    public EquiposPanel(EquipoController ec, UsuarioController uc) {
        this.equipoController = ec;
        this.usuarioController = uc;
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        construir();
    }

    private void construir() {
        add(UIUtils.crearTitulo("Equipos"), BorderLayout.NORTH);

        // Panel dividido: izquierda equipos / derecha miembros
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setOpaque(false);

        // ── Panel izquierdo: lista de equipos ─────────────────────────────
        JPanel panelIzq = new JPanel(new BorderLayout(0, 8));
        panelIzq.setOpaque(false);

        JLabel lblEq = new JLabel("Lista de Equipos");
        lblEq.setFont(UIUtils.FUENTE_SUBTITULO);
        panelIzq.add(lblEq, BorderLayout.NORTH);

        modeloEquipos = new DefaultTableModel(COL_EQUIPOS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEquipos = new JTable(modeloEquipos);
        UIUtils.estilizarTabla(tablaEquipos);
        tablaEquipos.getColumnModel().getColumn(0).setMaxWidth(45);
        tablaEquipos.getSelectionModel().addListSelectionListener(e -> mostrarMiembros());
        panelIzq.add(new JScrollPane(tablaEquipos), BorderLayout.CENTER);
        panelIzq.add(crearBotonesEquipos(), BorderLayout.SOUTH);

        // ── Panel derecho: miembros del equipo ────────────────────────────
        JPanel panelDer = new JPanel(new BorderLayout(0, 8));
        panelDer.setOpaque(false);
        panelDer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel lblMb = new JLabel("Miembros del Equipo");
        lblMb.setFont(UIUtils.FUENTE_SUBTITULO);
        panelDer.add(lblMb, BorderLayout.NORTH);

        modeloMiembros = new DefaultTableModel(COL_MIEMBROS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMiembros = new JTable(modeloMiembros);
        UIUtils.estilizarTabla(tablaMiembros);
        tablaMiembros.getColumnModel().getColumn(0).setMaxWidth(45);
        panelDer.add(new JScrollPane(tablaMiembros), BorderLayout.CENTER);
        panelDer.add(crearBotonesMiembros(), BorderLayout.SOUTH);

        split.setLeftComponent(panelIzq);
        split.setRightComponent(panelDer);
        add(split, BorderLayout.CENTER);

        cargarEquipos();
    }

    private JPanel crearBotonesEquipos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        p.setOpaque(false);
        JButton btnN = UIUtils.crearBoton("+ Nuevo");
        JButton btnE = UIUtils.crearBoton("Editar");
        JButton btnD = UIUtils.crearBotonPeligro("X Eliminar");
        btnN.setPreferredSize(new Dimension(100, 30));
        btnE.setPreferredSize(new Dimension(90, 30));
        btnD.setPreferredSize(new Dimension(100, 30));
        btnN.addActionListener(e -> nuevoEquipo());
        btnE.addActionListener(e -> editarEquipo());
        btnD.addActionListener(e -> eliminarEquipo());
        p.add(btnN); p.add(btnE); p.add(btnD);
        return p;
    }

    private JPanel crearBotonesMiembros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        p.setOpaque(false);
        JButton btnA = UIUtils.crearBoton("+ Añadir");
        JButton btnQ = UIUtils.crearBotonPeligro("X Quitar");
        btnA.setPreferredSize(new Dimension(100, 30));
        btnQ.setPreferredSize(new Dimension(100, 30));
        btnA.addActionListener(e -> asignarMiembro());
        btnQ.addActionListener(e -> quitarMiembro());
        p.add(btnA); p.add(btnQ);
        return p;
    }

    private void cargarEquipos() {
        modeloEquipos.setRowCount(0);
        for (Equipo e : equipoController.listarTodos()) {
            modeloEquipos.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getDescripcion(),
                e.getFechaAlta() != null ? e.getFechaAlta().toString() : ""
            });
        }
        modeloMiembros.setRowCount(0);
    }

    private void mostrarMiembros() {
        int fila = tablaEquipos.getSelectedRow();
        modeloMiembros.setRowCount(0);
        if (fila < 0) 
            return;
        int idEquipo = (int) modeloEquipos.getValueAt(fila, 0);
        for (Usuario u : equipoController.obtenerMiembros(idEquipo)) {
            modeloMiembros.addRow(new Object[]{ u.getId(), u.getNombre(), u.getEmail(), u.getRol() });
        }
    }

    private int getEquipoSeleccionado() {
        int fila = tablaEquipos.getSelectedRow();
        if (fila < 0) 
            return -1;
        return (int) modeloEquipos.getValueAt(fila, 0);
    }

    private void nuevoEquipo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del equipo:", "Nuevo Equipo", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) 
            return;
        String desc = JOptionPane.showInputDialog(this, "Descripción (opcional):", "Nuevo Equipo", JOptionPane.PLAIN_MESSAGE);
        if (equipoController.crear(new Equipo(nombre.trim(), desc != null ? desc.trim() : ""))) {
            UIUtils.mostrarExito(this, "Equipo creado."); cargarEquipos();
        } else {
            UIUtils.mostrarError(this, "No se pudo crear el equipo.");
        }
    }

    private void editarEquipo() {
        int id = getEquipoSeleccionado();
        if (id < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un equipo."); 
            return; 
        }
        Equipo e = equipoController.obtenerPorId(id);
        if (e == null) 
            return;
        String nombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", e.getNombre());
        if (nombre == null || nombre.trim().isEmpty()) 
            return;
        e.setNombre(nombre.trim());
        if (equipoController.actualizar(e)) {
            UIUtils.mostrarExito(this, "Equipo actualizado."); 
            cargarEquipos();
        } else {
            UIUtils.mostrarError(this, "No se pudo actualizar.");
        }
    }

    private void eliminarEquipo() {
        int id = getEquipoSeleccionado();
        if (id < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un equipo."); 
            return; 
        }
        String nombre = (String) modeloEquipos.getValueAt(tablaEquipos.getSelectedRow(), 1);
        if (UIUtils.confirmar(this, "¿Eliminar equipo \"" + nombre + "\"?")) {
            if (equipoController.eliminar(id)) {
                UIUtils.mostrarExito(this, "Equipo eliminado."); cargarEquipos();
            } else {
                UIUtils.mostrarError(this, "No se pudo eliminar.");
            }
        }
    }

    private void asignarMiembro() {
        int idEquipo = getEquipoSeleccionado();
        if (idEquipo < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un equipo primero."); 
            return; 
        }
        List<Usuario> todos = usuarioController.listarTodos();
        if (todos.isEmpty()) { 
            UIUtils.mostrarAviso(this, "No hay usuarios disponibles."); 
            return; 
        }
        Usuario[] arr = todos.toArray(new Usuario[0]);
        Usuario sel = (Usuario) JOptionPane.showInputDialog(
            this, "Selecciona usuario:", "Añadir Miembro",
            JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
        if (sel == null) 
            return;
        if (equipoController.asignarUsuario(idEquipo, sel.getId())) {
            UIUtils.mostrarExito(this, sel.getNombre() + " añadido al equipo.");
            mostrarMiembros();
        } else {
            UIUtils.mostrarError(this, "No se pudo añadir (quizás ya es miembro).");
        }
    }

    private void quitarMiembro() {
        int idEquipo = getEquipoSeleccionado();
        if (idEquipo < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un equipo."); 
            return; 
        }
        int filaM = tablaMiembros.getSelectedRow();
        if (filaM < 0) { 
            UIUtils.mostrarAviso(this, "Selecciona un miembro."); 
            return; 
        }
        int idUsuario = (int) modeloMiembros.getValueAt(filaM, 0);
        String nombre = (String) modeloMiembros.getValueAt(filaM, 1);
        if (UIUtils.confirmar(this, "¿Quitar a \"" + nombre + "\" del equipo?")) {
            if (equipoController.quitarUsuario(idEquipo, idUsuario)) {
                UIUtils.mostrarExito(this, nombre + " eliminado del equipo."); mostrarMiembros();
            } else {
                UIUtils.mostrarError(this, "No se pudo quitar al miembro.");
            }
        }
    }

    @Override public void refrescar() { 
        cargarEquipos(); 
    }
}
