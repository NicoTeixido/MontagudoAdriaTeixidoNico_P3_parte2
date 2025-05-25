package prog2.vista;

import prog2.adaptador.Adaptador;
import prog2.model.PaginaIncidencies;
import java.util.Scanner;
import java.util.List;

/**
 * @author Daniel Ortiz
 */
public class CentralUB {
    public final static float DEMANDA_MAX = 1800;
    public final static float DEMANDA_MIN = 250;
    public final static float VAR_NORM_MEAN = 1000;
    public final static float VAR_NORM_STD = 800;
    public final static long VAR_NORM_SEED = 123;

    /**
     * Generador aleatori de la demanda de potencia
     **/
    private VariableNormal variableNormal;

    /**
     * Demanda de potencia del dia actual
     **/
    private float demandaPotencia;

    /**
     * Adaptador para comunicar la vista con el modelo
     **/
    private Adaptador adaptador;

    /* Constructor*/
    public CentralUB() {
        variableNormal = new VariableNormal(VAR_NORM_MEAN, VAR_NORM_STD, VAR_NORM_SEED);
        demandaPotencia = generaDemandaPotencia();
        adaptador = new Adaptador();


    }

    public void gestioCentralUB() {
        // Mostrar missatge inicial
        System.out.println("Benvingut a la planta PWR de la UB");
        System.out.println("La demanda de potencia electrica avui es de " + demandaPotencia + " unitats");

        Scanner scanner = new Scanner(System.in);
        Menu<OpcioMenuPrincipal> menuPrincipal = new Menu<>("Menu Principal", OpcioMenuPrincipal.values());

        boolean sortir = false;
        while (!sortir) {
            menuPrincipal.mostrarMenu();
            OpcioMenuPrincipal opcio = menuPrincipal.getOpcio(scanner);
            //MENU:
            switch (opcio) {
                case GESTIO_BARRES:
                    gestioBarresControl(scanner);
                    break;
                case GESTIO_REACTOR:
                    gestioReactor(scanner);
                    break;
                case GESTIO_REFRIGERACIO:
                    gestioSistemaRefrigeracio(scanner);
                    break;
                case MOSTRAR_ESTAT_CENTRAL:
                    mostrarEstatCentral();
                    break;
                case MOSTRAR_BITACOLA:
                    mostrarBitacola();
                    break;
                case MOSTRAR_INCIDENCIES:
                    mostrarIncidencies();
                    break;
                case FINALITZAR_DIA:
                    finalitzaDia();
                    System.out.println("Dia finalitzat\n");
                    demandaPotencia = generaDemandaPotencia();
                    System.out.println("La demanda de potencia electrica avui es de " + demandaPotencia + " unitats");
                    break;
                case GUARDAR_DADES:
                    try {
                        guardarDades();
                        System.out.println("Dades guardades correctament.\n");
                    } catch (CentralUBException e) {
                        System.err.println("Error al guardar les dades: " + e.getMessage()); // Print error
                    }
                    break;
                case CARREGAR_DADES:
                    try {
                        carregarDades();
                        System.out.println("Dades carregades correctament.\n");
                    } catch (CentralUBException e) {
                        System.err.println("Error al carregar les dades: " + e.getMessage()); // Print error
                    }
                    break;
                case SORTIR:
                    sortir = true;
                    System.out.println("Sortint de la simulacio.\n");
                    break;
                default:
                    System.out.println("Opcio no valida.\n");
            }
        }
        scanner.close();
    }

    private float generaDemandaPotencia() {
        float valor = Math.round(variableNormal.seguentValor());
        if (valor > DEMANDA_MAX)
            return DEMANDA_MAX;
        else if (valor < DEMANDA_MIN)
            return DEMANDA_MIN;
        else
            return valor;
    }

    private void finalitzaDia() {
        try {
            adaptador.finalitzaDia(demandaPotencia);
        } catch (CentralUBException e) {
            System.err.println("Error al finalitzar el dia: " + e.getMessage());
        }
    }

    private void guardarDades() throws CentralUBException {
        adaptador.guardaDades("central_ub_dades.dat");
    }

    private void carregarDades() throws CentralUBException {
        adaptador.carregaDades("central_ub_dades.dat");
    }

    private void gestioBarresControl(Scanner scanner) {
        System.out.println("\n--- Gestio de Barres de Control ---");
        System.out.print("Introdueix el nou grau d'insercio (0-100%): ");
        if (scanner.hasNextFloat()) {
            float grau = scanner.nextFloat();
            scanner.nextLine();
            try {
                adaptador.setInsercioBarres(grau);
                System.out.println("Grau d'insercio establert a " + grau + "%");
            } catch (CentralUBException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Entrada no valida.");
            scanner.nextLine();
        }
    }

    private void gestioReactor(Scanner scanner) {
        System.out.println("\n--- Gestio del Reactor ---");
        Menu<OpcioGestioReactor> menuReactor = new Menu<>("Opcions del Reactor", OpcioGestioReactor.values());
        menuReactor.mostrarMenu();
        OpcioGestioReactor opcioReactor = menuReactor.getOpcio(scanner);

        switch (opcioReactor) {
            case ACTIVAR:
                try {
                    adaptador.activaReactor();
                    System.out.println("Reactor activat.");
                } catch (CentralUBException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case DESACTIVAR:
                adaptador.desactivaReactor();
                System.out.println("Reactor desactivat.");
                break;
            case MOSTRAR_ESTAT:
                System.out.println("Estat del Reactor: " + adaptador.getEstatReactor());
                break;
            default:
                System.out.println("Opcio no valida.");
        }
    }

    private void gestioSistemaRefrigeracio(Scanner scanner) {
        System.out.println("\n--- Gestio del Sistema de Refrigeracio ---");
        Menu<OpcioGestioRefrigeracio> menuRefrigeracio = new Menu<>("Opcions de Refrigeracio", OpcioGestioRefrigeracio.values());
        menuRefrigeracio.mostrarMenu();
        OpcioGestioRefrigeracio opcioRefrigeracio = menuRefrigeracio.getOpcio(scanner);

        switch (opcioRefrigeracio) {
            case ACTIVAR_TOTES:
                try {
                    adaptador.activaTotesBombesRefrigeracio();
                    System.out.println("Totes les bombes activades.");
                } catch (CentralUBException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case DESACTIVAR_TOTES:
                adaptador.desactivaTotesBombesRefrigeracio();
                System.out.println("Totes les bombes desactivades.");
                break;
            case ACTIVAR_BOMBA:
                System.out.print("Introdueix l'ID de la bomba a activar (0-3): ");
                if (scanner.hasNextInt()) {
                    int idActivar = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        adaptador.activaBomba(idActivar);
                        System.out.println("Bomba " + idActivar + " activada.");
                    } catch (CentralUBException e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    System.out.println("Entrada no valida.");
                    scanner.nextLine();
                }
                break;
            case DESACTIVAR_BOMBA:
                System.out.print("Introdueix l'ID de la bomba a desactivar (0-3): ");
                if (scanner.hasNextInt()) {
                    int idDesactivar = scanner.nextInt();
                    scanner.nextLine();
                    adaptador.desactivaBomba(idDesactivar);
                    System.out.println("Bomba " + idDesactivar + " desactivada.");
                } else {
                    System.out.println("Entrada no valida.");
                    scanner.nextLine();
                }
                break;
            case MOSTRAR_ESTAT:
                System.out.println("Estat del Sistema de Refrigeracio: " + adaptador.getEstatSistemaRefrigeracio());
                break;
            default:
                System.out.println("Opcio no valida.");
        }
    }

    private void mostrarEstatCentral() {
        System.out.println("\n--- Estat Actual de la Central ---");
        System.out.println(adaptador.getEstatActual());
        System.out.println("Potencia Generada: " + adaptador.getPotenciaGenerada() + " unitats");
        System.out.println("Guanys Acumulats: " + adaptador.getGuanysAcumulats() + " unitats economiques");
    }

    private void mostrarBitacola() {
        System.out.println("\n--- Bitacola Completa ---");
        System.out.println(adaptador.getBitacolaCompleta());
    }

    private void mostrarIncidencies() {
        System.out.println("\n--- Llista d'Incidencies ---");
        System.out.println(adaptador.getIncidencies());
    }

    public enum OpcioMenuPrincipal {
        GESTIO_BARRES("Gestionar Barres de Control"),
        GESTIO_REACTOR("Gestionar Reactor"),
        GESTIO_REFRIGERACIO("Gestionar Sistema de Refrigeracio"),
        MOSTRAR_ESTAT_CENTRAL("Mostrar Estat Actual de la Central"),
        MOSTRAR_BITACOLA("Mostrar Bitacola Completa"),
        MOSTRAR_INCIDENCIES("Mostrar Incidencies"),
        FINALITZAR_DIA("Finalitzar Dia"),
        GUARDAR_DADES("Guardar Dades"),
        CARREGAR_DADES("Carregar Dades"),
        SORTIR("Sortir");

        private final String descripcio;

        OpcioMenuPrincipal(String descripcio) {
            this.descripcio = descripcio;
        }

        @Override
        public String toString() {
            return descripcio;
        }
    }

    public enum OpcioGestioReactor {
        ACTIVAR("Activar Reactor"),
        DESACTIVAR("Desactivar Reactor"),
        MOSTRAR_ESTAT("Mostrar Estat del Reactor");

        private final String descripcio;

        OpcioGestioReactor(String descripcio) {
            this.descripcio = descripcio;
        }

        @Override
        public String toString() {
            return descripcio;
        }
    }

    public enum OpcioGestioRefrigeracio {
        ACTIVAR_TOTES("Activar Totes les Bombes"),
        DESACTIVAR_TOTES("Desactivar Totes les Bombes"),
        ACTIVAR_BOMBA("Activar Bomba per ID"),
        DESACTIVAR_BOMBA("Desactivar Bomba per ID"),
        MOSTRAR_ESTAT("Mostrar Estat del Sistema de Refrigeracio");

        private final String descripcio;

        OpcioGestioRefrigeracio(String descripcio) {
            this.descripcio = descripcio;
        }

        @Override
        public String toString() {
            return descripcio;
        }
    }
}