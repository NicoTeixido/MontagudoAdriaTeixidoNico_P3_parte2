package prog2.model;

import java.io.Serializable;
import prog2.vista.CentralUBException;

public class Reactor implements InComponent, Serializable {
    private boolean _activat;
    private float _temperatura;
    private static final float COST_OPERATIU = 35.0f;
    static final float TEMP_MAXIMA = 1000.0f;

    public Reactor() {
        this._activat = false; // Inicialment desactivat
        this._temperatura = 25.0f; // Temperatura ambient
    }

    // Getters i Setters
    public float getTemperatura() { return _temperatura; }
    public void setTemperatura(float temp) { this._temperatura = Math.max(temp, 25.0f); }

    // Implementacio de InComponent
    @Override
    public void activa() {
         _activat = true;
    }

    @Override public void desactiva() { _activat = false; }
    @Override public boolean getActivat() { return _activat; }

    @Override
    public float getCostOperatiu() {
        return (_activat) ? COST_OPERATIU : 0.0f;
    }

    @Override
    public float calculaOutput(float grauInsercioBarres) {
        if (!_activat) {
            return _temperatura;
        } else {
             return _temperatura + (100.0f - grauInsercioBarres) * 10.0f;
        }
    }

    @Override
    public void revisa(PaginaIncidencies p) {

        if (_temperatura > TEMP_MAXIMA) {
            desactiva();
            p.afegeixIncidencia("Reactor desactivat per superar " + TEMP_MAXIMA + "C (temperatura: " + _temperatura + "C)");

        }
    }


    @Override
    public String toString() {
        return "Reactor - Activat: " + _activat + ", Temp: " + _temperatura + "C";
    }
}