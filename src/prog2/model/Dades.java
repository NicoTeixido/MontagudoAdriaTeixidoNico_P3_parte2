package prog2.model;

import prog2.vista.CentralUBException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Dades implements InDades, Serializable {
    // Constants
    public final static long VAR_UNIF_SEED = 123;
    public final static float GUANYS_INICIALS = 0;
    public final static float PREU_UNITAT_POTENCIA = 1;
    public final static float PENALITZACIO_EXCES_POTENCIA = 250;

    // Atributs
    private final VariableUniforme variableUniforme;
    private float insercioBarres;
    private final Reactor reactor;
    private final SistemaRefrigeracio sistemaRefrigeracio;
    private final GeneradorVapor generadorVapor;
    private final Turbina turbina;
    private final Bitacola bitacola; // This is the single Bitacola instance
    private int dia;
    private float guanysAcumulats;

    public Dades() {
        this.variableUniforme = new VariableUniforme(VAR_UNIF_SEED);
        this.insercioBarres = 100; // 100% per defecte (reactor aturat)
        this.reactor = new Reactor();
        this.sistemaRefrigeracio = new SistemaRefrigeracio();
        this.generadorVapor = new GeneradorVapor();
        this.turbina = new Turbina();
        this.bitacola = new Bitacola(); // FIXED: Initialized Bitacola without a day parameter
        this.dia = 1;
        this.guanysAcumulats = GUANYS_INICIALS;

        // Configuracio inicial dels components
        this.reactor.desactiva();
        this.generadorVapor.activa();
        this.turbina.activa();
        this.sistemaRefrigeracio.desactiva();

        // Afegir 4 bombes refrigerants
        for (int i = 0; i < 4; i++) {
            BombaRefrigerant bomba = new BombaRefrigerant(i, this.variableUniforme);
            this.sistemaRefrigeracio.afegirBomba(bomba);
        }
    }

    /* **********************
     * IMPLEMENTACIO InDades *
     *********************** */

    @Override
    public float getInsercioBarres() {
        return this.insercioBarres;
    }

    @Override
    public void setInsercioBarres(float grau) throws CentralUBException {
        if (grau < 0 || grau > 100) {
            throw new CentralUBException("Error: El grau d'insercio ha d'estar entre 0% i 100%");
        }
        this.insercioBarres = grau;
    }

    @Override
    public void activaReactor() throws CentralUBException {
        if (this.reactor.getTemperatura() > Reactor.TEMP_MAXIMA) {
            throw new CentralUBException("Error: No es pot activar el reactor. Temperatura massa alta (>1000C)");
        }
        this.reactor.activa();
    }

    @Override
    public void desactivaReactor() {
        this.reactor.desactiva();
    }

    @Override
    public Reactor mostraReactor() {
        return this.reactor;
    }

    @Override
    public void activaBomba(int id) throws CentralUBException {
        if (id < 0 || id >= this.sistemaRefrigeracio.getBombes().size()) { // Use getBombes().size() for robustness
            throw new CentralUBException("Error: L'ID de la bomba ha d'estar entre 0 i " + (this.sistemaRefrigeracio.getBombes().size() - 1));
        }
        // FIXED: Activate specific pump, not the entire system always.
        this.sistemaRefrigeracio.getBombes().get(id).activa();
    }

    @Override
    public void desactivaBomba(int id) {
        if (id < 0 || id >= this.sistemaRefrigeracio.getBombes().size()) { // Use getBombes().size() for robustness
            System.err.println("Error: L'ID de la bomba ha d'estar entre 0 i " + (this.sistemaRefrigeracio.getBombes().size() - 1));
            return; // Or throw an unchecked exception if preferred
        }
        // FIXED: Deactivate specific pump, not the entire system always.
        this.sistemaRefrigeracio.getBombes().get(id).desactiva();
    }

    // NEW: Method to activate the entire refrigeration system
    public void activaSistemaRefrigeracio() throws CentralUBException {
        this.sistemaRefrigeracio.activa();
    }

    // NEW: Method to deactivate the entire refrigeration system
    public void desactivaSistemaRefrigeracio() {
        this.sistemaRefrigeracio.desactiva();
    }


    @Override
    public SistemaRefrigeracio mostraSistemaRefrigeracio() {
        return this.sistemaRefrigeracio;
    }

    @Override
    public float calculaPotencia() {
        if (!this.reactor.getActivat()) return 0.0f;

        float outputReactor = this.reactor.calculaOutput(this.insercioBarres);
        float outputRefrigeracio = this.sistemaRefrigeracio.calculaOutput(outputReactor);
        float outputGenerador = this.generadorVapor.calculaOutput(outputRefrigeracio);
        return this.turbina.calculaOutput(outputGenerador);
    }

    @Override
    public float getGuanysAcumulats() {
        return this.guanysAcumulats;
    }

    @Override
    public PaginaEstat mostraEstat() {
       float currentReactorTemp = this.reactor.getTemperatura(); // Initial input for refrigeration chain
        float refrigerationOutput = this.sistemaRefrigeracio.calculaOutput(currentReactorTemp);
        float generatorInput = currentReactorTemp - refrigerationOutput; // Temperature after cooling
        float generatorOutput = this.generadorVapor.calculaOutput(generatorInput);
        float turbinaOutput = this.turbina.calculaOutput(generatorOutput);


        return new PaginaEstat(
                this.dia,
                this.insercioBarres,
                this.reactor.getActivat() ? this.reactor.calculaOutput(this.insercioBarres) : 0, // Reactor output is still heat generated
                refrigerationOutput,
                generatorOutput,
                turbinaOutput
        );
    }


    @Override
    public Bitacola mostraBitacola() {
        return this.bitacola;
    }

    @Override
    public List<PaginaIncidencies> mostraIncidencies() {
        return this.bitacola.getIncidencies();
    }

    @Override
    public Bitacola finalitzaDia(float demandaPotencia) throws CentralUBException {
        // 1. Actualitza economia
        PaginaEconomica paginaEconomica = actualitzaEconomia(demandaPotencia);

        // 2. Genera pagina d'estat (abans de refrigerar)
        PaginaEstat paginaEstat = mostraEstat();

        // 3. Refrigerar reactor
        refrigeraReactor();

        // 4. Revisar components
        PaginaIncidencies paginaIncidencies = new PaginaIncidencies(this.dia);
        revisaComponents(paginaIncidencies);

        // 5. Actualitzar dia
        this.dia++;

        // 6. Afegir a bitacola (using the single instance _bitacola)
        this.bitacola.afegeixPagina(paginaEconomica);
        this.bitacola.afegeixPagina(paginaEstat);
        if (!paginaIncidencies.getIncidencies().isEmpty()) { // Only add if there are actual incidents
            this.bitacola.afegeixPagina(paginaIncidencies);
        }

        // FIXED: Removed redundant Bitacola creation and return; just return the main _bitacola instance
        return this.bitacola;
    }

    /* **********************
     * METODES PRIVATS *
     *********************** */

    private PaginaEconomica actualitzaEconomia(float demandaPotencia) {
        float potenciaGenerada = calculaPotencia();
        float percentatgeSatisfet = (demandaPotencia > 0)
                ? (Math.min(potenciaGenerada, demandaPotencia) / demandaPotencia) * 100
                : 0;

        float beneficis = Math.min(potenciaGenerada, demandaPotencia) * PREU_UNITAT_POTENCIA;
        float penalitzacio = (potenciaGenerada > demandaPotencia) ? PENALITZACIO_EXCES_POTENCIA : 0;
        float costOperatiu = getCostOperatiuTotal();

        float guanys = beneficis - penalitzacio - costOperatiu;
        this.guanysAcumulats += guanys;

        return new PaginaEconomica(
                this.dia, demandaPotencia, potenciaGenerada, percentatgeSatisfet,
                beneficis, penalitzacio, costOperatiu, this.guanysAcumulats
        );
    }

    private void refrigeraReactor() {
        float calorExtreta = this.sistemaRefrigeracio.calculaOutput(this.reactor.getTemperatura());
        float novaTemperatura = this.reactor.getTemperatura() - calorExtreta;
        this.reactor.setTemperatura(Math.max(novaTemperatura, 25)); // Minim 25C
    }

    private void revisaComponents(PaginaIncidencies paginaIncidencies) {
       this.reactor.revisa(paginaIncidencies);
        this.sistemaRefrigeracio.revisa(paginaIncidencies);
       }

    public float getCostOperatiuTotal() {
        return this.reactor.getCostOperatiu() +
                this.sistemaRefrigeracio.getCostOperatiu() +
                this.generadorVapor.getCostOperatiu() +
                this.turbina.getCostOperatiu();
    }
}