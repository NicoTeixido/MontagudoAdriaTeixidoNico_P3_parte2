package prog2.model;

import java.io.Serializable;

public class PaginaEstat extends PaginaBitacola implements Serializable {
    private final float _grauInsercioBarres;
    private final float _outputReactor;
    private final float _outputRefrigeracio;
    private final float _outputGeneradorVapor;
    private final float _outputTurbina;

    // Constructor
    public PaginaEstat(int dia, float grauInsercioBarres, float outputReactor,
                       float outputRefrigeracio, float outputGeneradorVapor,
                       float outputTurbina) {
        super(dia);
        this._grauInsercioBarres = grauInsercioBarres;
        this._outputReactor = outputReactor;
        this._outputRefrigeracio = outputRefrigeracio;
        this._outputGeneradorVapor = outputGeneradorVapor;
        this._outputTurbina = outputTurbina;
    }

    // Override de toString()
    @Override
    public String toString() {
        return "# Pàgina Estat\n" +
                "- Dia: " + getDia() + "\n" +
                "- Inserció Barres: " + _grauInsercioBarres + " %\n" +
                "- Output Reactor: " + _outputReactor + " Graus\n" +
                "- Output Refrigeració: " + _outputRefrigeracio + " Graus\n" +
                "- Output Generador Vapor: " + _outputGeneradorVapor + " Graus\n" +
                "- Output Turbina: " + _outputTurbina + " Unitats de Potència";
    }
}