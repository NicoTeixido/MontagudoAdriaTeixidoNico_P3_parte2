package prog2.model;

import java.io.Serializable;

public class GeneradorVapor implements InComponent, Serializable {
    private boolean _activat;
    private static final float COST_OPERATIU = 25.0f; // Taula 1
    private static final float EFICIENCIA = 0.9f;     // 90% d'eficiència
    private static final float TEMP_AMBIENT = 25.0f;

    public GeneradorVapor() {
        this._activat = true; // Sempre activat (Taula 1)
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
        if (!_activat) return 0.0f;
        return (input - TEMP_AMBIENT) * EFICIENCIA + TEMP_AMBIENT; // Output = (Input - 25) * 0.9 + 25
    }

    @Override
    public void revisa(PaginaIncidencies p) {
        // No té incidències (Taula 1)
    }

    @Override
    public String toString() {
        return "Generador de Vapor - Activat: " + _activat;
    }
}
