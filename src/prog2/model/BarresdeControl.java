package prog2.model;

import java.io.Serializable;
import prog2.vista.CentralUBException;

public class BarresdeControl implements InComponent, Serializable {

    // Atributos
    private float _grauInsercio; // Entre 0% y 100%
    private boolean _activat;     // Indica si las barras están activadas
    private static final float COST_OPERATIU = 5.0f; // Coste operativo por día (Tabla 1)

    // Constructor
    public BarresdeControl() {
        this._grauInsercio = 100.0f; // Por defecto, barras totalmente insertadas (reactor apagado)
        this._activat = true;        // Las barras siempre están activas (no se menciona desactivación)
    }

    // Métodos de la interfaz InComponent
    @Override
    public void activa() {
        this._activat = true;
    }

    @Override
    public void desactiva() {
        this._activat = false;
    }

    @Override
    public boolean getActivat() {
        return this._activat;
    }

    @Override
    public float getCostOperatiu() {
        return (this._activat) ? COST_OPERATIU : 0.0f; // Coste 0 si no está activo
    }

    public float calculaOutput(float input) {
        // Las barras no generan output (según Figura 2, solo el reactor lo hace)
        return 0.0f;
    }

    @Override
    public void revisa(PaginaIncidencies pIncidencies) throws CentralUBException {
        // No hay incidencias posibles (Tabla 1)
        // Pero si se intenta fijar un grado fuera de [0, 100], lanza excepción
        if (this._grauInsercio < 0 || this._grauInsercio > 100) {
            throw new CentralUBException("Error: El grau d'inserció ha d'estar entre 0% i 100%.");
        }
    }

    // Métodos específicos de BarresdeControl
    public float getGrauInsercio() {
        return this._grauInsercio;
    }

    public void setGrauInsercio(float grau) throws CentralUBException {
        if (grau < 0 || grau > 100) {
            throw new CentralUBException("Error: El grau d'inserció ha d'estar entre 0% i 100%.");
        }
        this._grauInsercio = grau;
    }

    @Override
    public String toString() {
        return "Barres de Control - Grau d'inserció: " + this._grauInsercio + "%";
    }
}