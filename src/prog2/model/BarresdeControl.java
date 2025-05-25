package prog2.model;

import java.io.Serializable;
import prog2.vista.CentralUBException;

public class BarresdeControl implements InComponent, Serializable {

    // Atributs
    private float _grauInsercio;
    private boolean _activat;
    private static final float COST_OPERATIU = 5.0f;

    // Constructor
    public BarresdeControl() {
        this._grauInsercio = 100.0f;
        this._activat = true;
    }

    // Metodes de la interfaz InComponent
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
        return (this._activat) ? COST_OPERATIU : 0.0f;
    }

    public float calculaOutput(float input) {
        return 0.0f;
    }

    @Override
    public void revisa(PaginaIncidencies pIncidencies) throws CentralUBException {
        // En cas de posar un grau que no estigui entre 0 y 100 salta error
        if (this._grauInsercio < 0 || this._grauInsercio > 100) {
            throw new CentralUBException("Error: El grau d'inserció ha d'estar entre 0% i 100%.");
        }
    }

    // Metodes especifics de BarresdeControl
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