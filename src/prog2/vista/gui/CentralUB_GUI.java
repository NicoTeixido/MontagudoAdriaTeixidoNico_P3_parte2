package prog2.vista.gui;

import prog2.adaptador.Adaptador; // Asegúrate de que esta ruta sea correcta
import prog2.vista.CentralUBException; // Importa tu excepción personalizada
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CentralUB_GUI extends JFrame {

    // --- Componentes visuales (Se vincularán con el .form) ---
    private JPanel mainPanel;
    private JTextArea outputArea;
    private JButton btnGestionarBarres;
    private JButton btnGestionarReactor;
    private JButton btnGestionarRefrigeracio;
    private JButton btnMostrarEstat;
    private JButton btnMostrarBitacola;
    private JButton btnGestionarIncidencies;
    private JButton btnFinalitzarDia;
    private JButton btnGuardarDades;
    private JButton btnCarregarDades;
    private JButton btnSortir;

    // --- Atributo del Adaptador ---
    private Adaptador adaptador;

    /**
     * Constructor de la ventana principal de la GUI.
     */
    public CentralUB_GUI() {
        // Configuración básica de la ventana
        setTitle("Central Nuclear UB - Simulació Gràfica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel); // Asegúrate de que 'mainPanel' es el nombre del Field name de tu panel raíz en el .form
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Inicializar el adaptador y mostrar el estado inicial
        try {
            adaptador = new Adaptador();
            // Adaptador no tiene getMissatgeBenvinguda directamente, usamos el estado actual
            outputArea.append("Central Nuclear UB - Iniciada amb èxit.\n");
            outputArea.append("Estat inicial: " + adaptador.getEstatActual() + "\n");
            outputArea.append("Dia actual: " + adaptador.getDiaActual() + "\n");
        } catch (Exception e) {
            outputArea.append("ERROR: No s'ha pogut inicialitzar el sistema: " + e.getMessage() + "\n");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error crític a l'iniciar la simulació: " + e.getMessage(),
                    "Error d'Inicialització",
                    JOptionPane.ERROR_MESSAGE);
            // Si hay un error crítico, deshabilitar todos los botones excepto salir
            btnSortir.setEnabled(true);
            btnGestionarBarres.setEnabled(false);
            btnGestionarReactor.setEnabled(false);
            btnGestionarRefrigeracio.setEnabled(false);
            btnMostrarEstat.setEnabled(false);
            btnMostrarBitacola.setEnabled(false);
            btnGestionarIncidencies.setEnabled(false);
            btnFinalitzarDia.setEnabled(false);
            btnGuardarDades.setEnabled(false);
            btnCarregarDades.setEnabled(false);
        }

        // --- Configuración de ActionListeners para los botones ---

        btnSortir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(mainPanel,
                        "Esteu segurs que voleu sortir de l'aplicació?",
                        "Sortir de l'aplicació",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // NOTA: Para los botones de gestión, se abrirán nuevas ventanas o diálogos en futuras implementaciones.
        // Por ahora, solo muestran un mensaje en el área de salida.

        btnGestionarBarres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear e inicializar el diálogo
                GestionarBarresDialog dialog = new GestionarBarresDialog(CentralUB_GUI.this, adaptador, outputArea);
                dialog.setVisible(true); // Mostrar el diálogo y esperar a que se cierre

                // Una vez que el diálogo se cierra
                if (dialog.isCanviAcceptat()) {
                    // Si el cambio fue aceptado, el diálogo ya habrá actualizado el outputArea
                    outputArea.append("\nCanvi de barres processat.\n");
                } else {
                    outputArea.append("\nGestió de barres cancel·lada.\n");
                }
               }
        });

        btnGestionarReactor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.append("\n[Acció] Gestionar Reactor (PENDENT d'implementar finestra)...");
                // TODO: Aquí se abrirá una nueva ventana/JDialog para activaReactor/desactivaReactor
                // Ejemplo de cómo usar activaReactor/desactivaReactor:
                /*
                try {
                    // Aquí podrías preguntar si activar o desactivar
                    adaptador.activaReactor(); // o adaptador.desactivaReactor();
                    outputArea.append("\nEstat del Reactor: " + adaptador.getEstatReactor() + "\n");
                } catch (CentralUBException ex) {
                    outputArea.append("ERROR: " + ex.getMessage() + "\n");
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error Reactor", JOptionPane.ERROR_MESSAGE);
                }
                */
            }
        });

        btnGestionarRefrigeracio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.append("\n[Acció] Gestionar Refrigeració (PENDENT d'implementar finestra)...");
                // TODO: Aquí se abrirá una nueva ventana/JDialog para activaBomba/desactivaBomba
                // Ejemplo de cómo usar:
                /*
                try {
                    // Preguntar si activar/desactivar y qué bomba o todas
                    adaptador.activaBomba(1); // o adaptador.activaTotesBombesRefrigeracio();
                    outputArea.append("\nEstat del Sistema de Refrigeració: " + adaptador.getEstatSistemaRefrigeracio() + "\n");
                } catch (CentralUBException ex) {
                    outputArea.append("ERROR: " + ex.getMessage() + "\n");
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error Refrigeració", JOptionPane.ERROR_MESSAGE);
                }
                */
            }
        });

        btnMostrarEstat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.append("\n--- Estat Actual de la Central ---\n");
                outputArea.append(adaptador.getEstatActual() + "\n"); // Usamos getEstatActual()
                outputArea.append("Potència generada: " + adaptador.getPotenciaGenerada() + "\n");
                outputArea.append("Guanys acumulats: " + adaptador.getGuanysAcumulats() + "\n");
            }
        });

        btnMostrarBitacola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.append("\n--- Quadern de Bitacola ---\n");
                outputArea.append(adaptador.getBitacolaCompleta() + "\n"); // Usamos getBitacolaCompleta()
            }
        });

        btnGestionarIncidencies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.append("\n--- Incidències Registrades ---\n");
                outputArea.append(adaptador.getIncidencies() + "\n"); // Usamos getIncidencies()
            }
        });

        btnFinalitzarDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solicitar la demanda de potencia al usuario, ya que finalitzaDia la requiere
                String demandaStr = JOptionPane.showInputDialog(mainPanel, "Introdueix la demanda de potència pel dia (número decimal):");
                if (demandaStr == null) { // Usuario canceló
                    outputArea.append("\nFinalització del dia cancel·lada.\n");
                    return;
                }

                try {
                    float demanda = Float.parseFloat(demandaStr);
                    outputArea.append("\n[Acció] Finalitzant el dia actual amb demanda: " + demanda + "...\n");
                    adaptador.finalitzaDia(demanda); // Llamada correcta con el parámetro
                    outputArea.append("Dia finalitzat amb èxit.\n");
                    outputArea.append("Nou estat: " + adaptador.getEstatActual() + "\n");
                    outputArea.append("Nou dia actual: " + adaptador.getDiaActual() + "\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Demanda de potència invàlida. Si us plau, introdueix un número decimal.", "Error d'entrada", JOptionPane.ERROR_MESSAGE);
                    outputArea.append("ERROR: Demanda de potència invàlida: " + ex.getMessage() + "\n");
                } catch (CentralUBException ex) {
                    outputArea.append("ERROR al finalitzar el dia: " + ex.getMessage() + "\n");
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error al finalitzar dia", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) { // Captura cualquier otra excepción inesperada
                    outputArea.append("ERROR inesperat al finalitzar el dia: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "Error inesperat: " + ex.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnGuardarDades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Puedes usar un JFileChooser aquí para que el usuario elija la ruta,
                    // pero por simplicidad, usaremos un nombre de archivo fijo por ahora.
                    adaptador.guardaDades("dades_central.ser"); // Usamos guardaDades()
                    outputArea.append("\nDades de la central guardades amb èxit en 'dades_central.ser'.\n");
                } catch (CentralUBException ex) {
                    outputArea.append("ERROR al guardar les dades: " + ex.getMessage() + "\n");
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error al guardar dades", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    outputArea.append("ERROR inesperat al guardar les dades: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "Error inesperat: " + ex.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCarregarDades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Puedes usar un JFileChooser aquí para que el usuario elija la ruta
                    adaptador.carregaDades("dades_central.ser"); // Usamos carregaDades()
                    outputArea.append("\nDades de la central carregades amb èxit des de 'dades_central.ser'.\n");
                    outputArea.append("Estat actual: " + adaptador.getEstatActual() + "\n");
                    outputArea.append("Dia actual: " + adaptador.getDiaActual() + "\n");
                } catch (CentralUBException ex) {
                    outputArea.append("ERROR al carregar les dades: " + ex.getMessage() + "\n");
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error al carregar dades", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) { // Captura posibles IOException, ClassNotFoundException, etc.
                    outputArea.append("ERROR inesperat al carregar les dades: " + ex.getMessage() + "\n");
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "Error inesperat: " + ex.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Punto de entrada principal para la aplicación GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CentralUB_GUI().setVisible(true);
            }
        });
    }
}