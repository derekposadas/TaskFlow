package view;

import controller.ProyectoController;
import controller.EquipoController;
import model.Proyecto;
import model.Equipo;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ProyectoFormDialog — Diálogo para crear o editar un proyecto.
 */
public class ProyectoFormDialog extends JDialog {

    private final ProyectoController proyectoController;
    private final EquipoController equipoController;
    private final Proyecto proyectoExistente;
    private boolean guardado = false;

    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JComboBox<String> comboEstado;
    private JComboBox<Object> comboEquipo;

    private static final String[] ESTADOS = {"Activo", "Pausado", "Finalizado"};

    public ProyectoFormDialog(JFrame padre, ProyectoController pc, EquipoController ec, Proyecto p) {
        super(padre, p == null ? "Nuevo Proyecto" : "Editar Proyecto", true);
        this.proyectoController = pc;
        this.equipoController = ec;
        this.proyectoExistente = p;
        construir();
        if (p != null) 
            rellenar(p);
    }

    private void construir() {
        setLayout(new BorderLayout());
        setSize(460, 360);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6,5,6,5);

        // Nombre
        g.gridx=0; g.gridy=0; g.weightx=0; form.add(new JLabel("Nombre:"), g);
        g.gridx=1; g.weightx=1; campoNombre = UIUtils.crearCampoTexto(25); form.add(campoNombre, g);

        // Descripción
        g.gridx=0; g.gridy=1; g.weightx=0; g.anchor=GridBagConstraints.NORTH;
        form.add(new JLabel("Descripción:"), g);
        g.gridx=1; g.weightx=1; g.anchor=GridBagConstraints.CENTER;
        campoDescripcion = UIUtils.crearAreaTexto(4, 25);
        JScrollPane scroll = new JScrollPane(campoDescripcion);
        scroll.setPreferredSize(new Dimension(250, 80));
        form.add(scroll, g);

        // Estado
        g.gridx=0; g.gridy=2; g.weightx=0; form.add(new JLabel("Estado:"), g);
        g.gridx=1; g.weightx=1; comboEstado = new JComboBox<>(ESTADOS);
        comboEstado.setFont(UIUtils.FUENTE_NORMAL); form.add(comboEstado, g);

        // Equipo
        g.gridx=0; g.gridy=3; g.weightx=0; form.add(new JLabel("Equipo:"), g);
        g.gridx=1; g.weightx=1;
        List<Equipo> equipos = equipoController.listarTodos();
        comboEquipo = new JComboBox<>();
        comboEquipo.setFont(UIUtils.FUENTE_NORMAL);
        comboEquipo.addItem("— Sin equipo —");
        for (Equipo e : equipos) comboEquipo.addItem(e);
        form.add(comboEquipo, g);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bot.setBackground(Color.WHITE);
        JButton btnC = new JButton("Cancelar"); btnC.setFont(UIUtils.FUENTE_NORMAL); btnC.setFocusPainted(false);
        JButton btnG = UIUtils.crearBoton("Guardar");
        btnC.addActionListener(e -> dispose());
        btnG.addActionListener(e -> guardar());
        bot.add(btnC); bot.add(btnG);
        add(bot, BorderLayout.SOUTH);
    }

    private void rellenar(Proyecto p) {
        campoNombre.setText(p.getNombre());
        campoDescripcion.setText(p.getDescripcion());
        comboEstado.setSelectedItem(p.getEstado());
        // Seleccionar equipo
        for (int i = 0; i < comboEquipo.getItemCount(); i++) {
            Object item = comboEquipo.getItemAt(i);
            if (item instanceof Equipo && ((Equipo) item).getId() == p.getIdEquipo()) {
                comboEquipo.setSelectedIndex(i); break;
            }
        }
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) { 
            UIUtils.mostrarAviso(this, "El nombre es obligatorio."); 
            return;
        }

        String desc = campoDescripcion.getText().trim();
        String estado = (String) comboEstado.getSelectedItem();
        int idEquipo = 0;
        Object selEquipo = comboEquipo.getSelectedItem();
        if (selEquipo instanceof Equipo) 
            idEquipo = ((Equipo) selEquipo).getId();

        boolean ok;
        if (proyectoExistente == null) {
            ok = proyectoController.crear(new Proyecto(nombre, desc, estado, idEquipo));
        } else {
            proyectoExistente.setNombre(nombre);
            proyectoExistente.setDescripcion(desc);
            proyectoExistente.setEstado(estado);
            proyectoExistente.setIdEquipo(idEquipo);
            ok = proyectoController.actualizar(proyectoExistente);
        }

        if (ok) {
            guardado = true;
            UIUtils.mostrarExito(this, "Proyecto guardado."); 
            dispose();
        } else {
            UIUtils.mostrarError(this, "No se pudo guardar el proyecto.");
        }
    }

    public boolean isGuardado() { 
        return guardado; 
    }
}
