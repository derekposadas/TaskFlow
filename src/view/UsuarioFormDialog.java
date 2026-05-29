package view;

import controller.UsuarioController;
import model.Usuario;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @autor derek
 */

/**
 * UsuarioFormDialog — Diálogo modal para crear o editar un usuario.
 */
public class UsuarioFormDialog extends JDialog {

    private final UsuarioController controller;
    private final Usuario usuarioExistente; // null si es nuevo
    private boolean guardado = false;

    // Campos del formulario
    private JTextField campoNombre;
    private JTextField campoEmail;
    private JComboBox<String> comboRol;

    private static final String[] ROLES = {"Admin", "Desarrollador", "Diseñador", "Tester", "Miembro"};

    public UsuarioFormDialog(JFrame padre, UsuarioController controller, Usuario usuario) {
        super(padre, usuario == null ? "Nuevo Usuario" : "Editar Usuario", true);
        this.controller = controller;
        this.usuarioExistente = usuario;
        construir();
        if (usuario != null) 
            rellenarFormulario(usuario);
    }

    private void construir() {
        setLayout(new BorderLayout(10, 10));
        setSize(420, 280);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // ── Formulario ────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoNombre = UIUtils.crearCampoTexto(20);
        form.add(campoNombre, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoEmail = UIUtils.crearCampoTexto(20);
        form.add(campoEmail, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        comboRol = new JComboBox<>(ROLES);
        comboRol.setFont(UIUtils.FUENTE_NORMAL);
        form.add(comboRol, gbc);

        add(form, BorderLayout.CENTER);

        // ── Botones ───────────────────────────────────────────────────────
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botones.setBackground(Color.WHITE);
        JButton btnGuardar  = UIUtils.crearBoton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(UIUtils.FUENTE_NORMAL);
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        add(botones, BorderLayout.SOUTH);
    }

    private void rellenarFormulario(Usuario u) {
        campoNombre.setText(u.getNombre());
        campoEmail.setText(u.getEmail());
        comboRol.setSelectedItem(u.getRol());
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String rol = (String) comboRol.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty()) {
            UIUtils.mostrarAviso(this, "Nombre y Email son obligatorios.");
            return;
        }
        if (!email.contains("@")) {
            UIUtils.mostrarAviso(this, "El email no parece válido.");
            return;
        }

        boolean ok;
        if (usuarioExistente == null) {
            // Nuevo usuario
            ok = controller.crear(new Usuario(nombre, email, rol));
        } else {
            // Actualizar existente
            usuarioExistente.setNombre(nombre);
            usuarioExistente.setEmail(email);
            usuarioExistente.setRol(rol);
            ok = controller.actualizar(usuarioExistente);
        }

        if (ok) {
            guardado = true;
            UIUtils.mostrarExito(this, "Usuario guardado correctamente.");
            dispose();
        } else {
            UIUtils.mostrarError(this, "No se pudo guardar el usuario.\nVerifica los datos.");
        }
    }

    public boolean isGuardado() { 
        return guardado; 
    }
}
