package prog2.vista.gui;

import prog2.adaptador.Adaptador;
import prog2.model.BombaRefrigerant;
import prog2.model.SistemaRefrigeracio;
import prog2.vista.CentralUBException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class GestionarRefrigeracioDialog extends JDialog {
    // --- Componentes visuales (se vincularán con el .form) ---
    private JPanel contentPane;
    private JLabel lblEstatSistemaGeneral; // Estado general del sistema
    private JList<BombaRefrigerant> lstBombes; // Lista de bombas
    private JTextField txtBombaId; // Campo para ID de bomba
    private JButton btnActivarBomba;
    private JButton btnDesactivarBomba;
    private JButton btnActivarTotes;
    private JButton btnDesactivarTotes;
    private JButton btnAcceptar;
    private JButton btnCancelar;

    // --- Atributo del Adaptador y referencia al outputArea principal ---
    private Adaptador adaptador;
    private JTextArea outputAreaPrincipal;

    private boolean canviAcceptat = false; // Indica si se aceptaron los cambios

    /**
     * Constructor del diálogo para gestionar el sistema de refrigeración.
     * @param parentFrame La ventana padre (CentralUB_GUI).
     * @param adaptador La instancia del Adaptador.
     * @param outputArea La JTextArea de la ventana principal para mensajes.
     */
    public GestionarRefrigeracioDialog(JFrame parentFrame, Adaptador adaptador, JTextArea outputArea) {
        super(parentFrame, "Gestionar Sistema de Refrigeració", true); // Diálogo modal

        this.adaptador = adaptador;
        this.outputAreaPrincipal = outputArea;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnAcceptar);

        // --- Inicializar y mostrar estado de las bombas ---
        actualitzarEstatBombes(); // Llama a un método para rellenar la lista y actualizar labels

        // Listener para la selección de la lista: auto-rellena el ID y activa/desactiva botones
        lstBombes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    BombaRefrigerant selectedBomba = lstBombes.getSelectedValue();
                    if (selectedBomba != null) {
                        txtBombaId.setText(String.valueOf(selectedBomba.getId()));
                        btnActivarBomba.setEnabled(true);
                        btnDesactivarBomba.setEnabled(true);
                    } else {
                        txtBombaId.setText("");
                        btnActivarBomba.setEnabled(false);
                        btnDesactivarBomba.setEnabled(false);
                    }
                }
            }
        });

        // --- Configuración de ActionListeners para los botones ---

        btnActivarBomba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onActivarDesactivarBomba(true);
            }
        });

        btnDesactivarBomba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onActivarDesactivarBomba(false);
            }
        });

        btnActivarTotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onActivarDesactivarTotes(true);
            }
        });

        btnDesactivarTotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onActivarDesactivarTotes(false);
            }
        });

        btnAcceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAcceptar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelar();
            }
        });

        // Call onCancelar() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancelar();
            }
        });

        // Deshabilitar botones de bomba individual al inicio
        btnActivarBomba.setEnabled(false);
        btnDesactivarBomba.setEnabled(false);

        pack(); // Ajusta el tamaño del diálogo
        setLocationRelativeTo(parentFrame); // Centra el diálogo sobre la ventana principal
    }

    /**
     * Actualiza el estado general del sistema y la lista de bombas.
     */
    private void actualitzarEstatBombes() {
        try {
            // Actualizar estado general del sistema
            lblEstatSistemaGeneral.setText("Estat general: " + adaptador.getEstatSistemaRefrigeracio());

            // Rellenar la JList con las bombas
            DefaultListModel<BombaRefrigerant> model = new DefaultListModel<>();
            List<BombaRefrigerant> bombes = adaptador.mostraSistemaRefrigeracio().getBombes(); // Asumo que Dades.mostraSistemaRefrigeracio() devuelve un SistemaRefrigeracio con getBombes()
            if (bombes != null) {
                for (BombaRefrigerant bomba : bombes) {
                    model.addElement(bomba);
                }
            }
            lstBombes.setModel(model);

        } catch (Exception e) {
            outputAreaPrincipal.append("WARNING: No s'ha pogut obtenir l'estat del sistema de refrigeració: " + e.getMessage() + "\n");
            lblEstatSistemaGeneral.setText("Estat general: N/A (Error)");
            // Deshabilitar controles si hay error
            btnActivarBomba.setEnabled(false);
            btnDesactivarBomba.setEnabled(false);
            btnActivarTotes.setEnabled(false);
            btnDesactivarTotes.setEnabled(false);
            btnAcceptar.setEnabled(false);
        }
    }

    /**
     * Activa o desactiva una bomba específica.
     * @param activar true para activar, false para desactivar.
     */
    private void onActivarDesactivarBomba(boolean activar) {
        try {
            int idBomba = Integer.parseInt(txtBombaId.getText());
            if (activar) {
                adaptador.activaBomba(idBomba);
                outputAreaPrincipal.append("\nBomba " + idBomba + " activada.\n");
            } else {
                adaptador.desactivaBomba(idBomba);
                outputAreaPrincipal.append("\nBomba " + idBomba + " desactivada.\n");
            }
            actualitzarEstatBombes(); // Refrescar la GUI
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID de bomba invàlid. Introdueix un número.", "Error d'entrada", JOptionPane.ERROR_MESSAGE);
        } catch (CentralUBException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Bomba", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR inesperat: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    /**
     * Activa o desactiva todas las bombas.
     * @param activar true para activar todas, false para desactivar todas.
     */
    private void onActivarDesactivarTotes(boolean activar) {
        try {
            if (activar) {
                adaptador.activaTotesBombesRefrigeracio();
                outputAreaPrincipal.append("\nTotes les bombes de refrigeració activades.\n");
            } else {
                adaptador.desactivaTotesBombesRefrigeracio();
                outputAreaPrincipal.append("\nTotes les bombes de refrigeració desactivades.\n");
            }
            actualitzarEstatBombes(); // Refrescar la GUI
        } catch (CentralUBException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Bombes", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR inesperat: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    private void onAcceptar() {
        // En este diálogo, los cambios se aplican inmediatamente al modelo.
        // El botón "Acceptar" simplemente cierra el diálogo.
        canviAcceptat = true;
        dispose();
    }

    private void onCancelar() {
        canviAcceptat = false;
        dispose();
    }

    /**
     * Retorna si el usuario aceptó los cambios o canceló.
     * @return true si se aceptaron los cambios, false si se canceló.
     */
    public boolean isCanviAcceptat() {
        return canviAcceptat;
    }
}