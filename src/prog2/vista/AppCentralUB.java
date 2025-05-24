package prog2.vista;

import prog2.adaptador.Adaptador;
import prog2.vista.VariableNormal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // Importar para el JFileChooser

public class AppCentralUB extends JFrame {

    // Constantes para la generación de demanda de potencia (copiadas de CentralUB.java)
    public final static float DEMANDA_MAX = 1800;
    public final static float DEMANDA_MIN = 250;
    public final static float VAR_NORM_MEAN = 1000;
    public final static float VAR_NORM_STD = 800;
    public final static long VAR_NORM_SEED = 123;

    // Atributos de la GUI
    private Adaptador adaptador;
    private VariableNormal variableNormal; // Para generar la demanda de potencia diaria
    private float demandaPotenciaDiaActual;

    // Componentes de la GUI
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JButton btnGestionComponentes;
    private JButton btnGestionEconomica;
    private JButton btnGestionBitacola;
    private JButton btnFinalizarDia;
    private JLabel lblDiaActual;
    private JLabel lblDemandaPotencia;
    private JLabel lblPotenciaGenerada;
    private JLabel lblGananciasAcumuladas;

    /**
     * Constructor de la clase AppCentralUB.
     * Configura la ventana principal de la aplicación.
     */
    public AppCentralUB() {
        // Inicializar el Adaptador y las variables de simulación
        adaptador = new Adaptador();
        variableNormal = new VariableNormal(VAR_NORM_MEAN, VAR_NORM_STD, VAR_NORM_SEED);
        demandaPotenciaDiaActual = generarDemandaPotencia(); // Generar la primera demanda

        // Configuración básica del JFrame
        setTitle("Central Nuclear UB - Simulación");
        setSize(800, 600); // Tamaño inicial de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Configurar el JMenuBar
        configurarMenuBar();

        // Configurar el panel principal con los botones de acción
        configurarPanelPrincipal();

        // Actualizar la información inicial en la interfaz
        actualizarInformacionCentral();
    }

    /**
     * Genera la demanda de potencia utilizando la VariableNormal.
     * Se asegura de que esté dentro de los límites DEMANDA_MIN y DEMANDA_MAX.
     */
    private float generarDemandaPotencia() {
        float demanda = variableNormal.seguentValor();
        return Math.max(DEMANDA_MIN, Math.min(DEMANDA_MAX, demanda));
    }

    /**
     * Configura la barra de menú de la aplicación.
     */
    private void configurarMenuBar() {
        menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem itemGuardar = new JMenuItem("Guardar Datos");
        itemGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });

        JMenuItem itemCargar = new JMenuItem("Cargar Datos");
        itemCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos();
            }
        });

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });

        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemCargar);
        menuArchivo.addSeparator(); // Separador visual
        menuArchivo.add(itemSalir);

        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }

    /**
     * Configura el panel principal con los botones de funcionalidad.
     */
    private void configurarPanelPrincipal() {
        mainPanel = new JPanel(new GridBagLayout()); // Usamos GridBagLayout para un diseño más flexible y centrado
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen alrededor

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbc.weightx = 1.0; // Distribuir el espacio horizontalmente
        gbc.gridx = 0; // Columna 0 para los labels y botones

        // Información de la central (labels)
        lblDiaActual = new JLabel("Día Actual: 1");
        lblDiaActual.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        mainPanel.add(lblDiaActual, gbc);

        lblDemandaPotencia = new JLabel("Demanda de Potencia (hoy): " + String.format("%.2f", demandaPotenciaDiaActual) + " MW");
        lblDemandaPotencia.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 1;
        mainPanel.add(lblDemandaPotencia, gbc);

        lblPotenciaGenerada = new JLabel("Potencia Generada (ayer): 0.00 MW");
        lblPotenciaGenerada.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 2;
        mainPanel.add(lblPotenciaGenerada, gbc);

        lblGananciasAcumuladas = new JLabel("Ganancias Acumuladas: 0.00 Unidades Económicas");
        lblGananciasAcumuladas.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = 3;
        mainPanel.add(lblGananciasAcumuladas, gbc);

        // Separador visual
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 20, 0); // Más espacio para el separador
        mainPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(10, 10, 10, 10); // Restaurar insets

        // Botones de funcionalidad
        btnGestionComponentes = new JButton("Gestión Componentes Central");
        btnGestionComponentes.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnGestionComponentes.setPreferredSize(new Dimension(250, 40)); // Tamaño preferido
        gbc.gridy = 5;
        mainPanel.add(btnGestionComponentes, gbc);

        btnGestionEconomica = new JButton("Gestión Económica");
        btnGestionEconomica.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnGestionEconomica.setPreferredSize(new Dimension(250, 40));
        gbc.gridy = 6;
        mainPanel.add(btnGestionEconomica, gbc);

        btnGestionBitacola = new JButton("Gestión Bitácora");
        btnGestionBitacola.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnGestionBitacola.setPreferredSize(new Dimension(250, 40));
        gbc.gridy = 7;
        mainPanel.add(btnGestionBitacola, gbc);

        btnFinalizarDia = new JButton("Finalizar Día");
        btnFinalizarDia.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnFinalizarDia.setPreferredSize(new Dimension(250, 50)); // Más grande y llamativo
        btnFinalizarDia.setBackground(new Color(153, 204, 255)); // Un color suave
        btnFinalizarDia.setOpaque(true);
        btnFinalizarDia.setBorderPainted(false);
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 10, 10, 10); // Más espacio antes del botón de finalizar día
        mainPanel.add(btnFinalizarDia, gbc);


        // Añadir acciones a los botones (por ahora solo stub, se implementarán en futuras ventanas)
        btnGestionComponentes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Ventana de Gestión de Componentes"));
        btnGestionEconomica.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Ventana de Gestión Económica"));
        btnGestionBitacola.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Ventana de Gestión de Bitácora"));
        btnFinalizarDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarDia();
            }
        });

        add(mainPanel, BorderLayout.CENTER); // Añadir el panel al centro del JFrame
    }

    /**
     * Actualiza la información mostrada en la ventana principal.
     */
    private void actualizarInformacionCentral() {
        lblDiaActual.setText("Día Actual: " + adaptador.getDiaActual());
        lblDemandaPotencia.setText("Demanda de Potencia (hoy): " + String.format("%.2f", demandaPotenciaDiaActual) + " MW");
        lblPotenciaGenerada.setText("Potencia Generada (ayer): " + String.format("%.2f", adaptador.getPotenciaGenerada()) + " MW");
        lblGananciasAcumuladas.setText("Ganancias Acumuladas: " + String.format("%.2f", adaptador.getGuanysAcumulats()) + " Unidades Económicas");
    }

    /**
     * Lógica para finalizar el día.
     */
    private void finalizarDia() {
        try {
            // El adaptador ya maneja la lógica de actualizar la bitácora y todo lo demás
            adaptador.finalitzaDia(demandaPotenciaDiaActual);
            JOptionPane.showMessageDialog(this, "Día finalizado correctamente. Se ha añadido la página económica y de estado a la bitácora.", "Día Finalizado", JOptionPane.INFORMATION_MESSAGE);

            // Generar nueva demanda para el siguiente día
            demandaPotenciaDiaActual = generarDemandaPotencia();

            // Actualizar la información mostrada en la GUI
            actualizarInformacionCentral();

        } catch (CentralUBException ex) {
            JOptionPane.showMessageDialog(this, "Error al finalizar el día: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lógica para guardar datos de la central.
     */
    private void guardarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar datos de la central");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                adaptador.guardaDades(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Datos guardados correctamente en: " + fileToSave.getAbsolutePath(), "Guardar Datos", JOptionPane.INFORMATION_MESSAGE);
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lógica para cargar datos de la central.
     */
    private void cargarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar datos de la central");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                adaptador.carregaDades(fileToLoad.getAbsolutePath());
                // Después de cargar, la demanda del día actual podría ser diferente
                // o la central podría estar en un día distinto.
                // Para simplificar, generamos una nueva demanda al cargar, o se podría
                // añadir la demanda al modelo si la tuvieras persistida.
                demandaPotenciaDiaActual = generarDemandaPotencia(); // Re-generar demanda para el nuevo día si se cargó un estado
                actualizarInformacionCentral();
                JOptionPane.showMessageDialog(this, "Datos cargados correctamente desde: " + fileToLoad.getAbsolutePath(), "Cargar Datos", JOptionPane.INFORMATION_MESSAGE);
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método main para iniciar la aplicación GUI.
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        // Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppCentralUB().setVisible(true);
            }
        });
    }
}