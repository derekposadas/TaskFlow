package view;

import utils.UIUtils;
import javax.swing.*;
import java.awt.*;

/**
 * 
 * @autor derek
 */

/**
 * DashboardPanel — Panel de bienvenida con resumen visual.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 20));
        setOpaque(false);
        construir();
    }

    private void construir() {
        // ── Título ────────────────────────────────────────────────────────
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setOpaque(false);
        JLabel titulo = UIUtils.crearTitulo("Dashboard");
        JLabel subtitulo = new JLabel(" — Bienvenido a TaskFlow");
        subtitulo.setFont(UIUtils.FUENTE_NORMAL);
        subtitulo.setForeground(Color.GRAY);
        panelTitulo.add(titulo);
        panelTitulo.add(subtitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // ── Tarjetas de resumen ───────────────────────────────────────────
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
        panelTarjetas.setOpaque(false);

        panelTarjetas.add(crearTarjeta("Usuarios",    "Gestiona los miembros\ndel equipo de trabajo",  UIUtils.COLOR_ACENTO));
        panelTarjetas.add(crearTarjeta("Proyectos",   "Organiza tus proyectos\nactivos y pausados",     new Color(70, 130, 100)));
        panelTarjetas.add(crearTarjeta("Tareas",      "Crea, asigna y sigue\nel estado de las tareas", new Color(180, 100, 40)));
        panelTarjetas.add(crearTarjeta("Equipos",     "Forma equipos y asigna\ntus colaboradores",     new Color(100, 60, 150)));

        add(panelTarjetas, BorderLayout.CENTER);

        // ── Pie informativo ───────────────────────────────────────────────
        JPanel piepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        piepanel.setOpaque(false);
        JLabel info = new JLabel("Usa el menú lateral para navegar entre las secciones.");
        info.setFont(UIUtils.FUENTE_PEQUEÑA);
        info.setForeground(Color.GRAY);
        piepanel.add(info);
        add(piepanel, BorderLayout.SOUTH);
    }

    private JPanel crearTarjeta(String titulo, String descripcion, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Barra de color izquierda
        JPanel barra = new JPanel();
        barra.setPreferredSize(new Dimension(6, 0));
        barra.setBackground(color);
        tarjeta.add(barra, BorderLayout.WEST);

        // Contenido
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(UIUtils.FUENTE_SUBTITULO);
        lblTitulo.setForeground(UIUtils.COLOR_TEXTO);

        JLabel lblDesc = new JLabel("<html>" + descripcion.replace("\n", "<br>") + "</html>");
        lblDesc.setFont(UIUtils.FUENTE_NORMAL);
        lblDesc.setForeground(Color.GRAY);

        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(lblDesc);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }
}
