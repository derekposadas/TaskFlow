package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * 
 * @autor derek
 */

/**
 * UIUtils — Constantes de colores, fuentes y métodos de ayuda para la interfaz.
 * Centraliza el estilo visual de toda la aplicación.
 */
public class UIUtils {
    // ── Paleta de colores ─────────────────────────────────────────────────
    public static final Color COLOR_FONDO = new Color(245, 247, 250); // Gris muy claro
    public static final Color COLOR_SIDEBAR = new Color(37,  47,  65);  // Azul oscuro
    public static final Color COLOR_SIDEBAR_TEXT = new Color(200, 210, 225); // Texto sidebar
    public static final Color COLOR_SIDEBAR_SEL  = new Color(66,  99, 150);  // Ítem seleccionado
    public static final Color COLOR_ACENTO = new Color(66,  99, 150);  // Azul medio
    public static final Color COLOR_BOTON = new Color(66,  99, 150);
    public static final Color COLOR_BOTON_HOVER = new Color(45,  75, 120);
    public static final Color COLOR_BOTON_PELIGRO= new Color(190,  60,  50);
    public static final Color COLOR_TEXTO = new Color(40,   40,  50);
    public static final Color COLOR_BORDE = new Color(210, 215, 225);
    public static final Color COLOR_HEADER_TABLA = new Color(55,  70,  90);
    public static final Color COLOR_FILA_PAR = new Color(250, 251, 253);
    public static final Color COLOR_FILA_IMPAR = Color.WHITE;

    // Colores de estado
    public static final Color COLOR_PENDIENTE = new Color(200, 120,  30);
    public static final Color COLOR_EN_PROGRESO = new Color( 40, 110, 180);
    public static final Color COLOR_COMPLETADA = new Color( 40, 140,  70);

    // ── Fuentes ───────────────────────────────────────────────────────────
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FUENTE_SUBTITULO= new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FUENTE_SIDEBAR = new Font("Segoe UI", Font.BOLD,  13);

    // ── Métodos de creación de componentes estilizados ────────────────────

    /**
     * Crea un JButton con el estilo principal de la app.
     */
    public static JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(COLOR_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    /**
     * Crea un JButton de color rojo (para acciones destructivas).
     */
    public static JButton crearBotonPeligro(String texto) {
        JButton btn = crearBoton(texto);
        btn.setBackground(COLOR_BOTON_PELIGRO);
        return btn;
    }

    /**
     * Estiliza un JTextField con bordes suaves.
     */
    public static JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return campo;
    }

    /**
     * Estiliza un JTextArea.
     */
    public static JTextArea crearAreaTexto(int filas, int cols) {
        JTextArea area = new JTextArea(filas, cols);
        area.setFont(FUENTE_NORMAL);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return area;
    }

    /**
     * Estiliza un JComboBox.
     */
    public static JComboBox<?> crearComboBox(Object[] items) {
        JComboBox<Object> combo = new JComboBox<>(items);
        combo.setFont(FUENTE_NORMAL);
        combo.setBackground(Color.WHITE);
        return combo;
    }

    /**
     * Aplica estilo profesional a una JTable.
     */
    public static void estilizarTabla(JTable tabla) {
        // Cabecera
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_HEADER_TABLA);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setReorderingAllowed(false);

        // Filas
        tabla.setFont(FUENTE_NORMAL);
        tabla.setRowHeight(28);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setSelectionBackground(new Color(180, 200, 230));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setShowVerticalLines(false);
        tabla.setFocusable(false);

        // Alternar colores de fila
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? COLOR_FILA_PAR : COLOR_FILA_IMPAR);
                    setForeground(COLOR_TEXTO);
                }
                return this;
            }
        });
    }

    /**
     * Crea una etiqueta de título de sección.
     */
    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }

    /**
     * Crea un panel con fondo blanco y borde suave.
     */
    public static JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    /**
     * Muestra un diálogo de confirmación.
     * @return true si el usuario confirmó.
     */
    public static boolean confirmar(Component padre, String mensaje) {
        int res = JOptionPane.showConfirmDialog(
            padre, mensaje, "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        return res == JOptionPane.YES_OPTION;
    }

    /** Muestra un mensaje de éxito */
    public static void mostrarExito(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Operación exitosa",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /** Muestra un mensaje de error */
    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra un aviso */
    public static void mostrarAviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Aviso",
            JOptionPane.WARNING_MESSAGE);
    }
}
