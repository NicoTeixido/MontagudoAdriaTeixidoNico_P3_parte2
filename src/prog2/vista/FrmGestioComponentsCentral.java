package prog2.vista;

import prog2.adaptador.Adaptador;
import prog2.model.Dades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // Importar File para guardar/cargar

public class FrmGestioComponentsCentral extends JFrame {

    private Adaptador adaptador;
    private Dades dades;

    // Componentes de la GUI
    private JLabel lblDiaActual;
    private JLabel lblDemandaPotencia;
    private JLabel lblGuanysAcumulats;

    private JSlider sliderBarras;
    private JLabel lblGrauInsercio;
    private JButton btnAplicarBarras;

    private JButton btnActivarReactor;
    private JButton btnDesactivarReactor;
    private JLabel lblEstadoReactor;
    private JLabel lblPotenciaGeneradaReactor; // Cambiado para reflejar potencia generada por el reactor

    // Componentes para el Sistema de Refrigeración
    private JPanel pnlBombasRefrigeracion; // Panel que contendrá los controles de las bombas
    private JButton btnActivarTodasBombas;
    private JButton btnDesactivarTodasBombas;

    // Componentes para la Bitácora/Informes
    private JTextArea textAreaBitacola;
    private JScrollPane scrollBitacola;
    private JButton btnMostrarEstadoCentral;
    private JButton btnMostrarIncidencias;

    private JButton btnFinalitzaDia;
    private JButton btnGuardaDades;
    private JButton btnCarregaDades;


    public FrmGestioComponentsCentral() {
        super("Central Nuclear UB - Gestió");
        this.adaptador = new Adaptador(); // Inicializar el adaptador

        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Tamaño inicial más grande para alojar más componentes
        setResizable(true); // Permitir redimensionar la ventana
        setLayout(new BorderLayout(10, 10)); // Usaremos un BorderLayout principal con espacios

        initComponents(); // Inicializar los componentes de la GUI
        addListeners();   // Añadir los ActionListeners a los componentes
        updateGUI();      // Actualizar el estado inicial de la GUI con los datos del modelo

        setVisible(true); // Hacer visible la ventana
    }

    /**
     * Inicializa y organiza todos los componentes visuales de la GUI.
     */
    private void initComponents() {
        // --- PANEL SUPERIOR: Información General ---
        JPanel pnlInfoGeneral = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5)); // Alinear a la izquierda, espacios
        pnlInfoGeneral.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Margen interior

        lblDiaActual = new JLabel("Dia Actual: N/A");
        lblDemandaPotencia = new JLabel("Demanda Potència Avui: N/A");
        lblGuanysAcumulats = new JLabel("Guanys Acumulats: N/A");

        Font infoFont = new Font("SansSerif", Font.BOLD, 14);
        lblDiaActual.setFont(infoFont);
        lblDemandaPotencia.setFont(infoFont);
        lblGuanysAcumulats.setFont(infoFont);

        pnlInfoGeneral.add(lblDiaActual);
        pnlInfoGeneral.add(new JSeparator(SwingConstants.VERTICAL));
        pnlInfoGeneral.add(lblDemandaPotencia);
        pnlInfoGeneral.add(new JSeparator(SwingConstants.VERTICAL));
        pnlInfoGeneral.add(lblGuanysAcumulats);
        add(pnlInfoGeneral, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Controles Principales (Barras, Reactor, Refrigeración) ---
        JPanel pnlControlesPrincipales = new JPanel(new GridLayout(3, 1, 15, 15)); // 3 filas, 1 columna, con espacios
        pnlControlesPrincipales.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Sub-panel: Gestión de Barras de Control ---
        JPanel pnlBarras = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBarras.setBorder(BorderFactory.createTitledBorder("Gestió Barres de Control"));
        lblGrauInsercio = new JLabel("Grau Inserció: 100%");
        sliderBarras = new JSlider(0, 100, 100); // Min, Max, Valor Inicial
        sliderBarras.setMajorTickSpacing(25);
        sliderBarras.setMinorTickSpacing(5);
        sliderBarras.setPaintTicks(true);
        sliderBarras.setPaintLabels(true);
        sliderBarras.setPreferredSize(new Dimension(300, 50)); // Tamaño preferido para el slider
        btnAplicarBarras = new JButton("Aplicar Inserció");

        pnlBarras.add(lblGrauInsercio);
        pnlBarras.add(sliderBarras);
        pnlBarras.add(btnAplicarBarras);
        pnlControlesPrincipales.add(pnlBarras);

        // --- Sub-panel: Gestión del Reactor ---
        JPanel pnlReactor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlReactor.setBorder(BorderFactory.createTitledBorder("Gestió Reactor"));
        lblEstadoReactor = new JLabel("Estat: Desactivat");
        lblPotenciaGeneradaReactor = new JLabel("Potència Generada: N/A"); // Mostrará la potencia total de la central
        btnActivarReactor = new JButton("Activar Reactor");
        btnDesactivarReactor = new JButton("Desactivar Reactor");

        pnlReactor.add(lblEstadoReactor);
        pnlReactor.add(new JSeparator(SwingConstants.VERTICAL));
        pnlReactor.add(lblPotenciaGeneradaReactor);
        pnlReactor.add(btnActivarReactor);
        pnlReactor.add(btnDesactivarReactor);
        pnlControlesPrincipales.add(pnlReactor);

        // --- Sub-panel: Sistema de Refrigeración (con scrollable para bombas) ---
        JPanel pnlRefrigeracion = new JPanel(new BorderLayout(5, 5));
        pnlRefrigeracion.setBorder(BorderFactory.createTitledBorder("Gestió Sistema de Refrigeració"));

        // Botones globales de activación/desactivación
        JPanel pnlAccionesBombasGlobal = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnActivarTodasBombas = new JButton("Activar Totes les Bombes");
        btnDesactivarTodasBombas = new JButton("Desactivar Totes les Bombes");
        pnlAccionesBombasGlobal.add(btnActivarTodasBombas);
        pnlAccionesBombasGlobal.add(btnDesactivarTodasBombas);
        pnlRefrigeracion.add(pnlAccionesBombasGlobal, BorderLayout.NORTH);

        // Panel para las bombas individuales (se llenará dinámicamente)
        pnlBombasRefrigeracion = new JPanel();
        pnlBombasRefrigeracion.setLayout(new BoxLayout(pnlBombasRefrigeracion, BoxLayout.Y_AXIS)); // Vertical
        JScrollPane scrollBombas = new JScrollPane(pnlBombasRefrigeracion);
        scrollBombas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollBombas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pnlRefrigeracion.add(scrollBombas, BorderLayout.CENTER);
        pnlControlesPrincipales.add(pnlRefrigeracion);

        add(pnlControlesPrincipales, BorderLayout.WEST); // Controles principales a la izquierda

        // --- PANEL DERECHO: Informes y Acciones Globales ---
        JPanel pnlDerecho = new JPanel(new BorderLayout(10, 10));
        pnlDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sub-panel para acciones del día y persistencia
        JPanel pnlAccionesGlobales = new JPanel(new GridLayout(1, 3, 10, 0)); // 1 fila, 3 columnas para botones
        btnFinalitzaDia = new JButton("Finalitzar Dia");
        btnGuardaDades = new JButton("Guardar Dades");
        btnCarregaDades = new JButton("Carregar Dades");
        pnlAccionesGlobales.add(btnFinalitzaDia);
        pnlAccionesGlobales.add(btnGuardaDades);
        pnlAccionesGlobales.add(btnCarregaDades);
        pnlDerecho.add(pnlAccionesGlobales, BorderLayout.NORTH);

        // Sub-panel para Bitácora e Incidencias
        JPanel pnlInformes = new JPanel(new BorderLayout(5, 5));
        pnlInformes.setBorder(BorderFactory.createTitledBorder("Informes i Bitàcola"));
        textAreaBitacola = new JTextArea();
        textAreaBitacola.setEditable(false);
        textAreaBitacola.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scrollBitacola = new JScrollPane(textAreaBitacola);
        pnlInformes.add(scrollBitacola, BorderLayout.CENTER);

        JPanel pnlBotonesInformes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnMostrarEstadoCentral = new JButton("Mostrar Estat Actual");
        btnMostrarIncidencias = new JButton("Mostrar Incidències");
        pnlBotonesInformes.add(btnMostrarEstadoCentral);
        pnlBotonesInformes.add(btnMostrarIncidencias);
        pnlInformes.add(pnlBotonesInformes, BorderLayout.SOUTH);

        pnlDerecho.add(pnlInformes, BorderLayout.CENTER);

        add(pnlDerecho, BorderLayout.EAST); // Panel derecho a la derecha

        // Inicializar las bombas una vez que el panel esté listo
        initBombasRefrigeracion();
    }

    /**
     * Añade los ActionListeners a los componentes interactivos.
     */
    private void addListeners() {
        // Listener para el slider de barras (actualiza la etiqueta en tiempo real)
        sliderBarras.addChangeListener(e -> {
            lblGrauInsercio.setText("Grau Inserció: " + sliderBarras.getValue() + "%");
        });

        // Listener para el botón de aplicar barras
        btnAplicarBarras.addActionListener(e -> {
            try {
                adaptador.setInsercioBarres(sliderBarras.getValue());
                JOptionPane.showMessageDialog(this, "Grau d'inserció de barres actualitzat a " + sliderBarras.getValue() + "%.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                updateGUI(); // Actualizar la GUI después del cambio
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error en les barres de control: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para activar reactor
        btnActivarReactor.addActionListener(e -> {
            try {
                adaptador.activaReactor();
                JOptionPane.showMessageDialog(this, "Reactor activat correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                updateGUI();
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error activant el reactor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para desactivar reactor
        btnDesactivarReactor.addActionListener(e -> {
            adaptador.desactivaReactor();
            JOptionPane.showMessageDialog(this, "Reactor desactivat correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            updateGUI();
        });

        // Listener para activar todas las bombas
        btnActivarTodasBombas.addActionListener(e -> {
            try {
                adaptador.activaTotesBombesRefrigeracio();
                JOptionPane.showMessageDialog(this, "Totes les bombes actives (si no estan fora de servei).", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                updateGUI();
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error activant bombes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para desactivar todas las bombas
        btnDesactivarTodasBombas.addActionListener(e -> {
            adaptador.desactivaTotesBombesRefrigeracio();
            JOptionPane.showMessageDialog(this, "Totes les bombes desactivades.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            updateGUI();
        });

        // Listener para finalizar día
        btnFinalitzaDia.addActionListener(e -> {
            try {
                // Aquí, la demanda de potencia se puede obtener de CentralUB si la lógica de generación está allí.
                // Si la generación está en CentralUB, necesitamos una forma de acceder a ella.
                // Asumiendo que podemos obtener la demanda de potencia del adaptador (si el modelo la genera internamente o la pide):
                // Para la Práctica 3, la demanda de potencia se genera internamente al final del día.
                // Por lo tanto, no necesitamos un input del usuario para la demanda.
                // El método finalitzaDia del adaptador ya debería gestionar la generación de la demanda si es necesario.
                float demandaPotenciaHoy = dades.getCostOperatiuTotal(); // Obtener la demanda del día actual, que se usa en finalitzaDia
                adaptador.finalitzaDia(demandaPotenciaHoy);

                JOptionPane.showMessageDialog(this, "Dia " + (adaptador.getDiaActual() -1) + " finalitzat. Nou dia: " + adaptador.getDiaActual(), "Dia Finalitzat", JOptionPane.INFORMATION_MESSAGE);
                updateGUI();
                textAreaBitacola.setText(adaptador.getBitacolaCompleta()); // Actualizar bitácora completa
            } catch (CentralUBException ex) {
                JOptionPane.showMessageDialog(this, "Error al finalitzar el dia: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para mostrar estado actual
        btnMostrarEstadoCentral.addActionListener(e -> {
            textAreaBitacola.setText(adaptador.getEstatActual());
        });

        // Listener para mostrar incidencias
        btnMostrarIncidencias.addActionListener(e -> {
            textAreaBitacola.setText(adaptador.getIncidencies());
        });

        // Listeners para guardar y cargar datos (implementación simple con JFileChooser)
        btnGuardaDades.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Datos de la Central");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    adaptador.guardaDades(fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Dades guardades correctament.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                } catch (CentralUBException ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar les dades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCarregaDades.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Carregar Datos de la Central");
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try {
                    adaptador.carregaDades(fileToLoad.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Dades carregades correctament.", "Carregar", JOptionPane.INFORMATION_MESSAGE);
                    updateGUI(); // Actualizar toda la GUI con los datos cargados
                    textAreaBitacola.setText(adaptador.getBitacolaCompleta()); // Actualizar bitácora
                } catch (CentralUBException ex) {
                    JOptionPane.showMessageDialog(this, "Error al carregar les dades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Inicializa los controles individuales para cada bomba de refrigeración.
     * Este método se llama una vez al inicio y populate el pnlBombasRefrigeracion.
     */
    private void initBombasRefrigeracion() {
        pnlBombasRefrigeracion.removeAll(); // Limpiar por si acaso
        // Obtener la lista de IDs de las bombas desde el adaptador
        java.util.List<Integer> idsBombas = adaptador.getIdsBombes();

        for (int id : idsBombas) {
            JPanel pnlBomba = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblBomba = new JLabel("Bomba " + id + ":");
            JCheckBox chkActiva = new JCheckBox("Activa");
            JLabel lblEstadoServicio = new JLabel("Estat: "); // Para "Operativa" o "Fuera de servicio"

            // Añadir listener para el checkbox
            chkActiva.addActionListener(e -> {
                try {
                    if (chkActiva.isSelected()) {
                        adaptador.activaBomba(id);
                        JOptionPane.showMessageDialog(this, "Bomba " + id + " activada.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        adaptador.desactivaBomba(id);
                        JOptionPane.showMessageDialog(this, "Bomba " + id + " desactivada.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                    }
                    updateGUI(); // Actualizar GUI después del cambio
                } catch (CentralUBException ex) {
                    JOptionPane.showMessageDialog(this, "Error en Bomba " + id + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    // Revertir el estado del checkbox si hubo un error (ej. fuera de servicio)
                    chkActiva.setSelected(!chkActiva.isSelected());
                    updateGUI(); // Para asegurar que el label se actualiza correctamente
                }
            });

            pnlBomba.add(lblBomba);
            pnlBomba.add(chkActiva);
            pnlBomba.add(lblEstadoServicio);
            pnlBombasRefrigeracion.add(pnlBomba);
        }
        pnlBombasRefrigeracion.revalidate();
        pnlBombasRefrigeracion.repaint();
    }


    /**
     * Método para actualizar todos los campos de la GUI con los datos del modelo.
     * Se debería llamar después de cada acción que cambie el estado del modelo.
     */
    private void updateGUI() {
        // Actualizar información general
        lblDiaActual.setText("Dia Actual: " + adaptador.getDiaActual());
        lblGuanysAcumulats.setText("Guanys Acumulats: " + String.format("%.2f", adaptador.getGuanysAcumulats()) + " Unitats Econòmiques");
        lblDemandaPotencia.setText("Demanda Potència Avui: " + String.format("%.2f", dades.getCostOperatiuTotal()) + " Unitats");

        // Actualizar barras de control
        sliderBarras.setValue((int) dades.getInsercioBarres());
        lblGrauInsercio.setText("Grau Inserció: " + String.format("%.0f", dades.getInsercioBarres()) + "%");

        // Actualizar estado del reactor
        lblEstadoReactor.setText("Estat Reactor: " + (adaptador.getEstatReactor() ? "Activat" : "Desactivat"));
        lblPotenciaGeneradaReactor.setText("Potència Generada (Central): " + String.format("%.2f", adaptador.getPotenciaGenerada()) + " Unitats");

        // Actualizar estado de las bombas de refrigeración
        java.util.List<Boolean> estadosBombas = adaptador.getEstadosBombes();
        java.util.List<Boolean> servicioBombas = adaptador.getServicioBombes();

        for (int i = 0; i < pnlBombasRefrigeracion.g etComponentCount(); i++) {
            JPanel pnlBomba = (JPanel) pnlBombasRefrigeracion.getComponent(i);
            JCheckBox chkActiva = (JCheckBox) pnlBomba.getComponent(1); // El JCheckBox es el segundo componente
            JLabel lblEstadoServicio = (JLabel) pnlBomba.getComponent(2); // El JLabel es el tercer componente

            boolean activada = estadosBombas.get(i);
            boolean fueraDeServicio = servicioBombas.get(i);

            chkActiva.setSelected(activada);
            chkActiva.setEnabled(!fueraDeServicio); // Deshabilitar si está fuera de servicio

            if (fueraDeServicio) {
                lblEstadoServicio.setText("Estat: Fora de servei");
                lblEstadoServicio.setForeground(Color.RED); // Color rojo para fuera de servicio
            } else {
                lblEstadoServicio.setText("Estat: Operativa");
                lblEstadoServicio.setForeground(Color.BLACK); // Color negro para operativa
            }
        }
        // Actualizar la bitácora con la última página
        textAreaBitacola.setText(adaptador.getBitacolaCompleta());
    }

    public static void main(String[] args) {
        // Asegurarse de que la GUI se ejecute en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(FrmGestioComponentsCentral::new);
    }
}