package prog2.model;

import java.io.Serializable;

public class Turbina implements InComponent, Serializable {
    private boolean _activat;
    private static final float COST_OPERATIU = 20.0f; // Taula 1
    private static final float TEMP_MINIMA = 100.0f;  // Temperatura de vaporització

    public Turbina() {
        this._activat = true; // Sempre activada (Taula 1)
    }

    // Implementació de InComponent
    @Override public void activa() { _activat = true; }
    @Override public void desactiva() { _activat = false; }
    @Override public boolean getActivat() { return _activat; }

    @Override
    public float getCostOperatiu() {
        return (_activat) ? COST_OPERATIU : 0.0f;
    }

    @Override
    public float calculaOutput(float input) {
        if (!_activat || input < TEMP_MINIMA) return 0.0f;
        return input * 2.0f; // Conversió simplificada: 1°C = 2 unitats de potència
    }

    @Override
    public void revisa(PaginaIncidencies p) {
        // No té incidències (Taula 1)
    }

    @Override
    public String toString() {
        return "Turbina - Activat: " + _activat;
    }
}