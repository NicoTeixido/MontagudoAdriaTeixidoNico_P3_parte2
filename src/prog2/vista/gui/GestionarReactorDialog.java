package prog2.vista.gui;

import prog2.adaptador.Adaptador;
import prog2.vista.CentralUBException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GestionarReactorDialog extends JDialog {
    // --- Componentes visuales (se vincularán con el .form) ---
    private JPanel contentPane;
    private JLabel lblEstatActualReactor; // Para mostrar el estado actual
    private JRadioButton rbActivarReactor;
    private JRadioButton rbDesactivarReactor;
    private JButton btnAcceptar;
    private JButton btnCancelar;
    // La declaración de ButtonGroup como un campo de la clase es necesaria
    // aunque lo instanciemos y usemos en el código.
    private ButtonGroup reactorButtonGroup;

    // --- Atributo del Adaptador y referencia al outputArea principal ---
    private Adaptador adaptador;
    private JTextArea outputAreaPrincipal;

    private boolean canviAcceptat = false; // Indica si se aceptaron los cambios

    /**
     * Constructor del diálogo para gestionar el reactor.
     * @param parentFrame La ventana padre (CentralUB_GUI).
     * @param adaptador La instancia del Adaptador.
     * @param outputArea La JTextArea de la ventana principal para mensajes.
     */
    public GestionarReactorDialog(JFrame parentFrame, Adaptador adaptador, JTextArea outputArea) {
        super(parentFrame, "Gestionar Estat Reactor", true); // Diálogo modal (bloquea la ventana padre)

        this.adaptador = adaptador;
        this.outputAreaPrincipal = outputArea;

        setContentPane(contentPane);
        setModal(true); // Ya se establece en el constructor super, pero lo reafirmo
        getRootPane().setDefaultButton(btnAcceptar); // El botón por defecto al presionar Enter

        // --- CÓDIGO PARA CREAR Y ASIGNAR EL BUTTONGROUP MANUALMENTE ---
        // Instancia el ButtonGroup
        reactorButtonGroup = new ButtonGroup();
        // Añade los JRadioButtons al grupo
        reactorButtonGroup.add(rbActivarReactor);
        reactorButtonGroup.add(rbDesactivarReactor);
        // --- FIN DEL CÓDIGO MANUAL ---

        // Leer el estado actual del reactor y configurar los RadioButtons
        try {
            String estatReactor = adaptador.getEstatReactor(); // Debería devolver "ACTIU" o "INACTIU"
            lblEstatActualReactor.setText("Estat actual del Reactor: " + estatReactor);

            if ("ACTIU".equalsIgnoreCase(estatReactor)) {
                rbActivarReactor.setSelected(true);
            } else {
                rbDesactivarReactor.setSelected(true);
            }
        } catch (Exception e) {
            outputAreaPrincipal.append("WARNING: No s'ha pogut obtenir l'estat actual del reactor: " + e.getMessage() + "\n");
            lblEstatActualReactor.setText("Estat actual del Reactor: N/A (Error)");
            // Deshabilitar opciones si no se puede leer el estado
            rbActivarReactor.setEnabled(false);
            rbDesactivarReactor.setEnabled(false);
            btnAcceptar.setEnabled(false);
        }

        // --- Configuración de ActionListeners para los botones ---
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

        // Manejar el cierre del diálogo al hacer clic en la "X" de la ventana
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Previene el cierre automático
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancelar(); // Llama a onCancelar para una salida consistente
            }
        });

        pack(); // Ajusta el tamaño del diálogo a sus componentes preferidos
        setLocationRelativeTo(parentFrame); // Centra el diálogo sobre la ventana principal
    }

    /**
     * Lógica a ejecutar cuando se presiona el botón "Acceptar".
     * Activa o desactiva el reactor según la opción seleccionada.
     */
    private void onAcceptar() {
        try {
            if (rbActivarReactor.isSelected()) {
                adaptador.activaReactor();
                outputAreaPrincipal.append("\nReactor activat amb èxit.\n");
            } else if (rbDesactivarReactor.isSelected()) {
                adaptador.desactivaReactor();
                outputAreaPrincipal.append("\nReactor desactivat amb èxit.\n");
            } else {
                // Esto no debería ocurrir si los JRadioButtons están en un ButtonGroup y uno se selecciona por defecto
                JOptionPane.showMessageDialog(this, "Selecciona una opció (Activar/Desactivar).", "Advertència", JOptionPane.WARNING_MESSAGE);
                return; // No cerrar el diálogo hasta que se seleccione una opción o se cancele
            }
            outputAreaPrincipal.append(adaptador.getEstatActual() + "\n"); // Mostrar estado actualizado en la consola principal
            canviAcceptat = true; // Indicar que la operación fue aceptada
            dispose(); // Cierra el diálogo
        } catch (CentralUBException ex) {
            // Manejo de excepciones específicas de la lógica de negocio
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al gestionar el Reactor", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            // Manejo de cualquier otra excepción inesperada
            JOptionPane.showMessageDialog(this, "Error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR inesperat: " + ex.getMessage() + "\n");
            ex.printStackTrace(); // Imprime la traza completa para depuración
        }
    }

    /**
     * Lógica a ejecutar cuando se presiona el botón "Cancel·lar" o la "X".
     * Cierra el diálogo sin aplicar cambios.
     */
    private void onCancelar() {
        canviAcceptat = false; // Indicar que la operación fue cancelada
        dispose(); // Cierra el diálogo
    }

    /**
     * Retorna si el usuario aceptó los cambios o canceló.
     * @return true si se aceptaron los cambios, false si se canceló.
     */
    public boolean isCanviAcceptat() {
        return canviAcceptat;
    }
}