package prog2.model;

import prog2.vista.CentralUBException;
import java.io.Serializable;

public class BombaRefrigerant implements InBombaRefrigerant, Serializable {
    private final int _id;
    private boolean _activat;
    private boolean _foraDeServei;
    private final VariableUniforme _generadorAleatori;
    private static final float CAPACITAT = 100.0f; // Capacitat fixa
    private static final float COST_OPERATIU = 130.0f; // Cost per bomba

    // Constructor
    public BombaRefrigerant(int id, VariableUniforme generadorAleatori) {
        this._id = id;
        this._activat = false;
        this._foraDeServei = false;
        this._generadorAleatori = generadorAleatori;
    }

    // Implementacio de InBombaRefrigerant
    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void activa() throws CentralUBException {
        if (_foraDeServei) {
            throw new CentralUBException("Bomba " + _id + " no es pot activar: esta fora de servei");
        }
        _activat = true;
    }

    @Override
    public void desactiva() {
        _activat = false;
    }

    @Override
    public boolean getActivat() {
        return _activat;
    }

    @Override
    public void revisa(PaginaIncidencies p) {
        if (_generadorAleatori.seguentValor() < 25) { // 25% probabilitat
            _foraDeServei = true;
            _activat = false; // Es desactiva automaticament
            p.afegeixIncidencia("Bomba refrigerant " + _id + " fora de servei");
        }
    }

    @Override
    public boolean getForaDeServei() {
        return _foraDeServei;
    }

    @Override
    public float getCapacitat() {
        return (_activat && !_foraDeServei) ? CAPACITAT : 0.0f;
    }

    @Override
    public float getCostOperatiu() {
        return (_activat && !_foraDeServei) ? COST_OPERATIU : 0.0f;
    }

    // Metodes toString
    @Override
    public String toString() {
        return String.format("Bomba %d [Activa: %b, Fora de servei: %b]",
                _id, _activat, _foraDeServei);
    }
}