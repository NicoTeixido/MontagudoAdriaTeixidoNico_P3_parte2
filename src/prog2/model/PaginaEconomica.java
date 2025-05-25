package prog2.model;

import java.io.Serializable;

public class PaginaEconomica extends PaginaBitacola implements Serializable {
    private final float _demandaPotencia;
    private final float _potenciaGenerada;
    private final float _percentatgeSatisfet;
    private final float _beneficis;
    private final float _penalitzacio;
    private final float _costOperatiu;
    private final float _guanysAcumulats;

    // Constructor
    public PaginaEconomica(int dia, float demandaPotencia, float potenciaGenerada,
                           float percentatgeSatisfet, float beneficis,
                           float penalitzacio, float costOperatiu,
                           float guanysAcumulats) {
        super(dia);
        this._demandaPotencia = demandaPotencia;
        this._potenciaGenerada = potenciaGenerada;
        this._percentatgeSatisfet = percentatgeSatisfet;
        this._beneficis = beneficis;
        this._penalitzacio = penalitzacio;
        this._costOperatiu = costOperatiu;
        this._guanysAcumulats = guanysAcumulats;
    }

    // Override de toString()
    @Override
    public String toString() {
        return "# Pàgina Econòmica\n" +
                "- Dia: " + getDia() + "\n" +
                "- Demanda de Potència: " + _demandaPotencia + "\n" +
                "- Potència Generada: " + _potenciaGenerada + "\n" +
                "- Demanda Satisfeta: " + _percentatgeSatisfet + " %\n" +
                "- Beneficis: " + _beneficis + " Unitats Econòmiques\n" +
                "- Penalització Excés: " + _penalitzacio + " Unitats Econòmiques\n" +
                "- Cost Operatiu: " + _costOperatiu + " Unitats Econòmiques\n" +
                "- Guanys Acumulats: " + _guanysAcumulats + " Unitats Econòmiques";
    }
}