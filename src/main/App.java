package main;

import view.MainFrame;
import javax.swing.*;

/**
 * 
 * @autor derek
 */

/**
 * App — Punto de entrada de la aplicación TaskFlow.
 */
public class App {

    public static void main(String[] args) {
        // Aplicar Look & Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el L&F por defecto de Java (Metal)
            System.out.println("L&F no disponible, usando Metal.");
        }

        // Lanzar en el EDT (buena práctica obligatoria en Swing)
        SwingUtilities.invokeLater(() -> {
            MainFrame ventana = new MainFrame();
            ventana.setVisible(true);
        });
    }
}
