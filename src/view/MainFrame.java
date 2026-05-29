package view;

import utils.UIUtils;
import controller.UsuarioController;
import controller.ProyectoController;
import controller.TareaController;
import controller.EquipoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @autor derek
 */

/**
 * MainFrame — Ventana principal de la aplicación TaskFlow.
 */
public class MainFrame extends JFrame {
    // Paneles de sección (se crean una vez y se reutilizan)
    private UsuariosPanel panelUsuarios;
    private ProyectosPanel panelProyectos;
    private TareasPanel panelTareas;
    private EquiposPanel panelEquipos;
    private DashboardPanel panelDashboard;

    // Contenedor central que muestra el panel activo
    private JPanel panelContenido;

    // Botones del sidebar
    private JButton btnDashboard;
    private JButton btnUsuarios;
    private JButton btnProyectos;
    private JButton btnTareas;
    private JButton btnEquipos;

    public MainFrame() {
        inicializarControladores();
        inicializarVentana();
        construirUI();
        mostrarPanel(panelDashboard, btnDashboard);
    }

    private void inicializarControladores() {
        // Crear controladores (instancian sus DAOs internamente)
        UsuarioController uc = new UsuarioController();
        ProyectoController pc = new ProyectoController();
        TareaController tc = new TareaController();
        EquipoController ec = new EquipoController();

        // Crear paneles de vista pasándoles los controladores
        panelDashboard = new DashboardPanel();
        panelUsuarios = new UsuariosPanel(uc);
        panelProyectos = new ProyectosPanel(pc, ec);
        panelTareas = new TareasPanel(tc, uc, pc);
        panelEquipos = new EquiposPanel(ec, uc);
    }

    private void inicializarVentana() {
        setTitle("TaskFlow — Gestor de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setPreferredSize(new Dimension(1100, 720));
        setLocationRelativeTo(null); // Centrar en pantalla
        getContentPane().setBackground(UIUtils.COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // ── Sidebar izquierdo ─────────────────────────────────────────────
        JPanel sidebar = crearSidebar();
        add(sidebar, BorderLayout.WEST);

        // ── Panel de contenido (centro) ───────────────────────────────────
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(UIUtils.COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelContenido, BorderLayout.CENTER);

        pack();
    }

    /**
     * Construye el sidebar con el logo y los botones de navegación.
     */
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo / título
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(UIUtils.COLOR_SIDEBAR);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        JLabel lblLogo = new JLabel("TaskFlow");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        logoPanel.add(lblLogo);
        sidebar.add(logoPanel);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 75, 95));
        sep.setMaximumSize(new Dimension(200, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        // Botones de navegación
        btnDashboard = crearBotonSidebar("Dashboard");
        btnUsuarios = crearBotonSidebar("Usuarios");
        btnProyectos = crearBotonSidebar("Proyectos");
        btnTareas = crearBotonSidebar("[OK]  Tareas");
        btnEquipos = crearBotonSidebar("Equipos");

        sidebar.add(btnDashboard);
        sidebar.add(btnUsuarios);
        sidebar.add(btnProyectos);
        sidebar.add(btnTareas);
        sidebar.add(btnEquipos);

        sidebar.add(Box.createVerticalGlue());

        // Versión
        JLabel lblVersion = new JLabel("v1.0 · TaskFlow");
        lblVersion.setFont(UIUtils.FUENTE_PEQUEÑA);
        lblVersion.setForeground(new Color(100, 115, 135));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblVersion.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        sidebar.add(lblVersion);

        // Eventos de clic
        btnDashboard.addActionListener(e -> mostrarPanel(panelDashboard,btnDashboard));
        btnUsuarios.addActionListener(e -> mostrarPanel(panelUsuarios,btnUsuarios));
        btnProyectos.addActionListener(e -> mostrarPanel(panelProyectos,btnProyectos));
        btnTareas.addActionListener(e -> mostrarPanel(panelTareas,btnTareas));
        btnEquipos.addActionListener(e -> mostrarPanel(panelEquipos,btnEquipos));

        return sidebar;
    }

    /**
     * Crea un botón con estilo de sidebar.
     */
    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(UIUtils.FUENTE_SIDEBAR);
        btn.setForeground(UIUtils.COLOR_SIDEBAR_TEXT);
        btn.setBackground(UIUtils.COLOR_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(UIUtils.COLOR_SIDEBAR_SEL))
                    btn.setBackground(new Color(50, 62, 82));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(UIUtils.COLOR_SIDEBAR_SEL))
                    btn.setBackground(UIUtils.COLOR_SIDEBAR);
            }
        });
        return btn;
    }

    /**
     * Cambia el panel visible en el contenido principal.
     */
    private JButton botonActivo = null;

    private void mostrarPanel(JPanel panel, JButton boton) {
        // Quitar selección anterior
        if (botonActivo != null) {
            botonActivo.setBackground(UIUtils.COLOR_SIDEBAR);
            botonActivo.setForeground(UIUtils.COLOR_SIDEBAR_TEXT);
        }
        // Marcar el botón activo
        boton.setBackground(UIUtils.COLOR_SIDEBAR_SEL);
        boton.setForeground(Color.WHITE);
        botonActivo = boton;

        // Reemplazar panel
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();

        // Refrescar datos si el panel tiene ese método
        if (panel instanceof Refrescable) {
            ((Refrescable) panel).refrescar();
        }
    }

    /** Interfaz para paneles que necesitan refrescarse al activarse */
    public interface Refrescable {
        void refrescar();
    }
}
