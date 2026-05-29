package view;

import controller.TareaController;
import controller.UsuarioController;
import controller.ProyectoController;
import model.Tarea;
import model.Usuario;
import model.Proyecto;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 
 * @autor derek
 */

/**
 * TareaFormDialog — Diálogo para crear o editar una tarea.
 */
public class TareaFormDialog extends JDialog {

    private final TareaController tareaController;
    private final UsuarioController usuarioController;
    private final ProyectoController proyectoController;
    private final Tarea tareaExistente;
    private boolean guardado = false;

    private JTextField campoTitulo;
    private JTextArea campoDescripcion;
    private JComboBox<String> comboEstado;
    private JComboBox<String> comboPrioridad;
    private JComboBox<Object> comboProyecto;
    private JComboBox<Object> comboUsuario;

    private static final String[] ESTADOS = {Tarea.ESTADO_PENDIENTE, Tarea.ESTADO_EN_PROGRESO, Tarea.ESTADO_COMPLETADA};
    private static final String[] PRIORIDADES = {Tarea.PRIORIDAD_BAJA, Tarea.PRIORIDAD_MEDIA, Tarea.PRIORIDAD_ALTA};

    public TareaFormDialog(JFrame padre, TareaController tc, UsuarioController uc,
                            ProyectoController pc, Tarea t) {
        super(padre, t == null ? "Nueva Tarea" : "Editar Tarea", true);
        this.tareaController    = tc;
        this.usuarioController  = uc;
        this.proyectoController = pc;
        this.tareaExistente     = t;
        construir();
        if (t != null) 
            rellenar(t);
    }

    private void construir() {
        setLayout(new BorderLayout());
        setSize(480, 420);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5,5,5,5);

        // Título
        g.gridx=0; g.gridy=0; g.weightx=0; form.add(new JLabel("Título:"), g);
        g.gridx=1; g.weightx=1; campoTitulo = UIUtils.crearCampoTexto(25); form.add(campoTitulo, g);

        // Descripción
        g.gridx=0; g.gridy=1; g.weightx=0; g.anchor=GridBagConstraints.NORTH;
        form.add(new JLabel("Descripción:"), g);
        g.gridx=1; g.weightx=1; g.anchor=GridBagConstraints.CENTER;
        campoDescripcion = UIUtils.crearAreaTexto(3, 25);
        JScrollPane sc = new JScrollPane(campoDescripcion);
        sc.setPreferredSize(new Dimension(250, 65)); form.add(sc, g);

        // Estado
        g.gridx=0; g.gridy=2; g.weightx=0; form.add(new JLabel("Estado:"), g);
        g.gridx=1; comboEstado = new JComboBox<>(ESTADOS); comboEstado.setFont(UIUtils.FUENTE_NORMAL);
        form.add(comboEstado, g);

        // Prioridad
        g.gridx=0; g.gridy=3; g.weightx=0; form.add(new JLabel("Prioridad:"), g);
        g.gridx=1; comboPrioridad = new JComboBox<>(PRIORIDADES); comboPrioridad.setFont(UIUtils.FUENTE_NORMAL);
        form.add(comboPrioridad, g);

        // Proyecto (obligatorio)
        g.gridx=0; g.gridy=4; g.weightx=0; form.add(new JLabel("Proyecto *:"), g);
        g.gridx=1; comboProyecto = new JComboBox<>(); comboProyecto.setFont(UIUtils.FUENTE_NORMAL);
        for (Proyecto p : proyectoController.listarTodos()) comboProyecto.addItem(p);
        form.add(comboProyecto, g);

        // Usuario asignado (opcional)
        g.gridx=0; g.gridy=5; g.weightx=0; form.add(new JLabel("Asignar a:"), g);
        g.gridx=1; comboUsuario = new JComboBox<>(); comboUsuario.setFont(UIUtils.FUENTE_NORMAL);
        comboUsuario.addItem("— Sin asignar —");
        for (Usuario u : usuarioController.listarTodos()) comboUsuario.addItem(u);
        form.add(comboUsuario, g);

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

    private void rellenar(Tarea t) {
        campoTitulo.setText(t.getTitulo());
        campoDescripcion.setText(t.getDescripcion());
        comboEstado.setSelectedItem(t.getEstado());
        comboPrioridad.setSelectedItem(t.getPrioridad());
        // Seleccionar proyecto
        for (int i = 0; i < comboProyecto.getItemCount(); i++) {
            Object item = comboProyecto.getItemAt(i);
            if (item instanceof Proyecto && ((Proyecto) item).getId() == t.getIdProyecto()) {
                comboProyecto.setSelectedIndex(i); break;
            }
        }
        // Seleccionar usuario
        for (int i = 0; i < comboUsuario.getItemCount(); i++) {
            Object item = comboUsuario.getItemAt(i);
            if (item instanceof Usuario && ((Usuario) item).getId() == t.getIdUsuario()) {
                comboUsuario.setSelectedIndex(i); break;
            }
        }
    }

    private void guardar() {
        String titulo = campoTitulo.getText().trim();
        if (titulo.isEmpty()) { 
            UIUtils.mostrarAviso(this, "El título es obligatorio."); 
            return; 
        }

        Object selProyecto = comboProyecto.getSelectedItem();
        if (!(selProyecto instanceof Proyecto)) { 
            UIUtils.mostrarAviso(this, "Selecciona un proyecto."); 
            return; 
        }

        int idProyecto = ((Proyecto) selProyecto).getId();
        int idUsuario = 0;
        Object selUsuario = comboUsuario.getSelectedItem();
        if (selUsuario instanceof Usuario) 
            idUsuario = ((Usuario) selUsuario).getId();

        String estado = (String) comboEstado.getSelectedItem();
        String prioridad = (String) comboPrioridad.getSelectedItem();
        String desc = campoDescripcion.getText().trim();

        boolean ok;
        if (tareaExistente == null) {
            ok = tareaController.crear(new Tarea(titulo, desc, estado, prioridad, null, idProyecto, idUsuario));
        } else {
            tareaExistente.setTitulo(titulo);
            tareaExistente.setDescripcion(desc);
            tareaExistente.setEstado(estado);
            tareaExistente.setPrioridad(prioridad);
            tareaExistente.setIdProyecto(idProyecto);
            tareaExistente.setIdUsuario(idUsuario);
            ok = tareaController.actualizar(tareaExistente);
        }

        if (ok) {
            guardado = true;
            UIUtils.mostrarExito(this, "Tarea guardada."); dispose();
        } else {
            UIUtils.mostrarError(this, "No se pudo guardar la tarea.");
        }
    }

    public boolean isGuardado() { 
        return guardado; 
    }
}
