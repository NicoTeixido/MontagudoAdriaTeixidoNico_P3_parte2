package prog2.vista.gui;

import prog2.adaptador.Adaptador;
import prog2.vista.CentralUBException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GestionarBarresDialog extends JDialog {
    // --- Componentes visuales (se vincularán con el .form) ---
    private JPanel contentPane; // El panel principal del diálogo
    private JSlider sliderGrauInsercio; // Slider para el grado de inserción
    private JLabel lblGrauActual; // Etiqueta para mostrar el valor del slider (valor seleccionado por el slider)
    private JButton btnAcceptar;
    private JButton btnCancelar;
    private JLabel lblGrauInsercioInicial; // Para mostrar el valor de las barras al abrir el diálogo (valor actual del sistema)

    // --- Atributo del Adaptador y referencia al outputArea principal ---
    private Adaptador adaptador;
    private JTextArea outputAreaPrincipal; // Referencia al outputArea de la ventana principal

    // Variable para saber si se ha aceptado el cambio (true) o cancelado (false)
    private boolean canviAcceptat = false;

    /**
     * Constructor del diálogo para gestionar las barras de control.
     * @param parentFrame La ventana padre (CentralUB_GUI).
     * @param adaptador La instancia del Adaptador.
     * @param outputArea La JTextArea de la ventana principal para mensajes.
     */
    public GestionarBarresDialog(JFrame parentFrame, Adaptador adaptador, JTextArea outputArea) {
        super(parentFrame, "Gestionar Barres de Control", true); // true para hacerlo modal (bloquea la ventana padre)

        this.adaptador = adaptador;
        this.outputAreaPrincipal = outputArea;

        setContentPane(contentPane);
        setModal(true); // Ya se establece en el constructor super, pero lo reafirmo
        getRootPane().setDefaultButton(btnAcceptar); // El botón por defecto al presionar Enter

        // Configuración básica del slider: rango 0-100 para porcentaje
        sliderGrauInsercio.setMinimum(0);
        sliderGrauInsercio.setMaximum(100);
        sliderGrauInsercio.setMajorTickSpacing(10); // Marcas cada 10%
        sliderGrauInsercio.setMinorTickSpacing(1);  // Sub-marcas cada 1%
        sliderGrauInsercio.setPaintTicks(true); // Mostrar las marcas
        sliderGrauInsercio.setPaintLabels(true); // Mostrar etiquetas numéricas

        // --- Lectura del valor actual del grado de inserción y configuración del slider ---
        try {
            // Obtener el grado actual de inserción de las barras usando el adaptador
            float grauActual = adaptador.getGrauInsercioBarres(); // Este método devuelve un float entre 0.0 y 1.0 (o 0 y 100 si así lo definiste en Dades)

            // Convertir el valor a un entero entre 0 y 100 para el JSlider
            int grauActualPercent = (int) (grauActual); // Si Dades ya devuelve 0-100, no se multiplica. Si devuelve 0.0-1.0, sería (grauActual * 100)

            // Asumiendo que getGrauInsercioBarres() de Dades devuelve un valor de 0 a 100 directamente.
            // Si devuelve de 0.0 a 1.0, entonces la línea sería: int grauActualPercent = (int) (grauActual * 100);

            // Establecer el valor inicial del slider
            sliderGrauInsercio.setValue(grauActualPercent);

            // Actualizar las etiquetas para mostrar el valor inicial
            lblGrauInsercioInicial.setText("Grau inicial: " + grauActualPercent + "%");
            lblGrauActual.setText("Grau seleccionat: " + grauActualPercent + "%");

        } catch (Exception e) {
            // Si hay un error al obtener el grado, mostrar un aviso y deshabilitar controles
            outputAreaPrincipal.append("WARNING: No s'ha pogut obtenir el grau d'inserció actual de les barres: " + e.getMessage() + "\n");
            lblGrauInsercioInicial.setText("Grau inicial: N/A (Error)");
            lblGrauActual.setText("Grau seleccionat: N/A (Error)");
            sliderGrauInsercio.setEnabled(false); // Deshabilita el slider si hay error
            btnAcceptar.setEnabled(false); // Deshabilita el botón de aceptar si hay error
        }


        // Listener para el slider: actualiza la etiqueta lblGrauActual al mover el slider
        sliderGrauInsercio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblGrauActual.setText("Grau seleccionat: " + sliderGrauInsercio.getValue() + "%");
            }
        });

        // Listener para el botón Aceptar
        btnAcceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAcceptar();
            }
        });

        // Listener para el botón Cancelar
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelar();
            }
        });

        // Manejar el cierre del diálogo al hacer clic en la "X"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancelar(); // Llama a onCancelar para una salida consistente
            }
        });

        // Habilitar o deshabilitar Acceptar al inicio si el Adaptador no se inicializó bien
        // (Aunque ya está cubierto en el try-catch de arriba, es una doble seguridad)
        if (adaptador == null) {
            btnAcceptar.setEnabled(false);
        }

        pack(); // Ajusta el tamaño del diálogo a sus componentes
        setLocationRelativeTo(parentFrame); // Centra el diálogo sobre la ventana principal
    }

    /**
     * Lógica a ejecutar cuando se presiona el botón "Acceptar".
     * Aplica el cambio de grado de inserción de las barras.
     */
    private void onAcceptar() {
        try {
            // Obtener el valor del slider y convertirlo a un float en el rango 0.0-1.0 si Dades espera eso
            // O dejarlo tal cual si Dades espera 0-100.
            // Según tu Dades.setInsercioBarres(float grau), espera un grado (0-100).
            float grauAjustat = (float) sliderGrauInsercio.getValue();

            adaptador.setInsercioBarres(grauAjustat);
            outputAreaPrincipal.append("\nBarres de control ajustades a: " + grauAjustat + "%\n");
            outputAreaPrincipal.append(adaptador.getEstatActual() + "\n"); // Mostrar estado actualizado
            canviAcceptat = true; // Indicar que el cambio fue aceptado
            dispose(); // Cierra el diálogo
        } catch (CentralUBException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Barres de Control", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputAreaPrincipal.append("ERROR inesperat: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    /**
     * Lógica a ejecutar cuando se presiona el botón "Cancel·lar" o la "X".
     * Cierra el diálogo sin aplicar cambios.
     */
    private void onCancelar() {
        canviAcceptat = false; // Indicar que el cambio fue cancelado
        dispose(); // Cierra el diálogo sin aplicar cambios
    }

    /**
     * Retorna si el usuario aceptó los cambios o canceló.
     * @return true si se aceptaron los cambios, false si se canceló.
     */
    public boolean isCanviAcceptat() {
        return canviAcceptat;
    }
}